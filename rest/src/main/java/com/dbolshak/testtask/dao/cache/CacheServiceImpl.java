package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.Computable;
import com.dbolshak.testtask.dao.TimeStampInfo;
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

    private final ConcurrentMap<String, Future<TimeStampInfo>> cache =
            new ConcurrentLinkedHashMap.Builder<String, Future<TimeStampInfo>>().maximumWeightedCapacity(1_000_000).build();
    @Autowired
    private Computable fileReader;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public TimeStampInfo compute(final String file) {
        while (true) {
            Future<TimeStampInfo> f = cache.get(file);
            if (f == null) {
                Callable<TimeStampInfo> eval = new Callable<TimeStampInfo>() {
                    public TimeStampInfo call() throws InterruptedException, IOException, ExecutionException {
                        return fileReader.compute(file);
                    }
                };
                FutureTask<TimeStampInfo> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(file, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                LOG.warn("Compute task is cancelled by exception for file " + file, e);
                cache.remove(file, f);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationRuntimeException("Unknown exception occurred while handling content in cache for file " + file, e);
            }
        }
    }

    @Override
    public void remove(String file) {
        cache.remove(file);
    }

    @Override
    public void remove(String topic, String timeStamp) {
        remove(fileSystemService.getAbsolutFileName(topic, timeStamp));
    }

    @Override
    public TimeStampInfo get(String file) {
        return compute(file);
    }

    @Override
    public TimeStampInfo get(String topic, String timeStamp) {
        return get(fileSystemService.getAbsolutFileName(topic, timeStamp));
    }
}
