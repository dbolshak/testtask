package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.Computable;
import com.dbolshak.testtask.dao.TimeStampContent;
import com.dbolshak.testtask.fs.FileSystemService;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOG = LoggerFactory.getLogger(CacheServiceImpl.class);
    private final ConcurrentMap<String, Future<TimeStampContent>> cache =
            new ConcurrentLinkedHashMap.Builder<String, Future<TimeStampContent>>().maximumWeightedCapacity(1_000_000).build();

    @Autowired
    private Computable fileReader;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public TimeStampContent compute(final String file) {
        while (true) {
            Future<TimeStampContent> f = cache.get(file);
            if (f == null) {
                Callable<TimeStampContent> callable = new Callable<TimeStampContent>() {
                    public TimeStampContent call() throws InterruptedException, IOException, ExecutionException {
                        return fileReader.compute(file);
                    }
                };
                FutureTask<TimeStampContent> ft = new FutureTask<>(callable);
                f = cache.putIfAbsent(file, ft);
                if (f == null) {
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            } catch (CancellationException e) {
                LOG.warn ("Future was cancelled by exception, removing all cache for " + file, e);
                cache.remove(file, f);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationRuntimeException("Exception occurs in cache while handling " + file, e);
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
    public TimeStampContent get(String file){
        return compute(file);
    }

    @Override
    public TimeStampContent get(String topic, String timeStamp) {
        return get(fileSystemService.getAbsoluteFileName(topic, timeStamp));
    }
}
