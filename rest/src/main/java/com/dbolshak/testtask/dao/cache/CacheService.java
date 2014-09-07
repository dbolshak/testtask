package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.TimeStampContent;

import java.util.concurrent.ExecutionException;

/**
 * Created by dbolshak on 05.09.2014.
 */
public interface CacheService {
    /**
     * Removes a cached value based on absolute file path
     *
     * @param file specified a file for which we don't need to keep its content in cache
     */
    void remove(String file);

    /**
     * Removes a cached value based on topic and timeStamp
     */
    void remove(String topic, String timeStamp);

    /**
     * Returns value from cache. In case of cache missing it loads it to cache.
     *
     * @param file a file content of which must be fetched from cache (or cached in case of cache missing)
     * @return
     * @throws InterruptedException
     */
    TimeStampContent get(String file);

    TimeStampContent get(String topic, String timeStamp);
}
