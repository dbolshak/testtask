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

    private final ConcurrentMap<TimeStamp, Future<TimeStampContent>> lru =
            new ConcurrentLinkedHashMap.Builder<TimeStamp, Future<TimeStampContent>>().maximumWeightedCapacity(1_000_000).build();
    @Autowired
    private Computable fileReader;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public TimeStampContent compute(final TimeStamp timeStamp) {
        while (true) {
            Future<TimeStampContent> f = lru.get(timeStamp);
            if (f == null) {
                Callable<TimeStampContent> callable = new Callable<TimeStampContent>() {
                    public TimeStampContent call() throws InterruptedException, IOException, ExecutionException {
                        return fileReader.compute(timeStamp);
                    }
                };
                FutureTask<TimeStampContent> ft = new FutureTask<>(callable);
                f = lru.putIfAbsent(timeStamp, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                LOG.warn("Compute task is cancelled by exception for file " + timeStamp, e);
                lru.remove(timeStamp, f);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationRuntimeException("Unknown exception occurred while handling content in lru for file " + timeStamp, e);
            }
        }
    }

    @Override
    public void remove(TimeStamp timeStamp) {
        lru.remove(timeStamp);
    }

    @Override
    public TimeStampContent get(TimeStamp timeStamp) {
        return compute(timeStamp);
    }
}
