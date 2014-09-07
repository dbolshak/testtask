package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.dao.Computable;
import com.dbolshak.testtask.dao.TimeStampInfo;
import com.dbolshak.testtask.utils.Helper;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.*;

@Component
public class CacheServiceImpl implements Computable, CacheService {
    private final ConcurrentMap<String, Future<TimeStampInfo>> cache =
            new ConcurrentLinkedHashMap.Builder<String, Future<TimeStampInfo>>().maximumWeightedCapacity(1_000_000).build();
    @Autowired private BaseDirProvider baseDirProvider;
    @Autowired private Computable fileReader;

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
        remove(Helper.getFileName(baseDirProvider.getBaseDir(), topic, timeStamp));
    }

    @Override
    public TimeStampInfo get(String file) throws InterruptedException, ExecutionException {
        return compute(file);
    }

    @Override
    public TimeStampInfo get(String topic, String timeStamp) throws InterruptedException, ExecutionException {
        return get(Helper.getFileName(baseDirProvider.getBaseDir(), topic, timeStamp));
    }
}
