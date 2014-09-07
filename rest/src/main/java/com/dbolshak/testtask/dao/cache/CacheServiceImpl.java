package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.Computable;
import com.dbolshak.testtask.dao.TimeStampInfo;
import com.dbolshak.testtask.fs.FileSystemService;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Component
public class CacheServiceImpl implements Computable, CacheService {
    private final ConcurrentMap<String, Future<TimeStampInfo>> cache =
            new ConcurrentLinkedHashMap.Builder<String, Future<TimeStampInfo>>().maximumWeightedCapacity(1_000_000).build();

    @Autowired
    private Computable fileReader;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public TimeStampInfo compute(final String file) throws InterruptedException, ExecutionException {
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
                cache.remove(file, f);
            }
        }
    }

    @Override
    public void remove(String file) {
        cache.remove(file);
    }

    @Override
    public void remove(String topic, String timeStamp) {
        remove(fileSystemService.getAbsoluteFileName(topic, timeStamp));
    }

    @Override
    public TimeStampInfo get(String file) throws InterruptedException, ExecutionException {
        return compute(file);
    }

    @Override
    public TimeStampInfo get(String topic, String timeStamp) throws InterruptedException, ExecutionException {
        return get(fileSystemService.getAbsoluteFileName(topic, timeStamp));
    }
}
