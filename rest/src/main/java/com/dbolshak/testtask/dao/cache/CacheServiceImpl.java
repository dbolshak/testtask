package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.dao.Computable;
import com.dbolshak.testtask.dao.TimeStampContent;
import com.dbolshak.testtask.fs.FileSystemService;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.*;

@Service
public class CacheServiceImpl implements Computable, CacheService {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(CacheServiceImpl.class);

    private final ConcurrentMap<String, Future<TimeStampContent>> lru =
            new ConcurrentLinkedHashMap.Builder<String, Future<TimeStampContent>>().maximumWeightedCapacity(1_000_000).build();
    @Autowired
    private Computable fileReader;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public TimeStampContent compute(final String file) {
        while (true) {
            Future<TimeStampContent> f = lru.get(file);
            if (f == null) {
                Callable<TimeStampContent> callable = new Callable<TimeStampContent>() {
                    public TimeStampContent call() throws InterruptedException, IOException, ExecutionException {
                        return fileReader.compute(file);
                    }
                };
                FutureTask<TimeStampContent> ft = new FutureTask<>(callable);
                f = lru.putIfAbsent(file, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                LOG.warn("Compute task is cancelled by exception for file " + file, e);
                lru.remove(file, f);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationRuntimeException("Unknown exception occurred while handling content in lru for file " + file, e);
            }
        }
    }

    @Override
    public void remove(String file) {
        lru.remove(file);
    }

    @Override
    public void remove(TimeStamp timeStamp) {
        remove(fileSystemService.getAbsoluteFileName(timeStamp));
    }

    @Override
    public TimeStampContent get(String file) {
        return compute(file);
    }

    @Override
    public TimeStampContent get(TimeStamp timeStamp) {
        return get(fileSystemService.getAbsoluteFileName(timeStamp));
    }
}
