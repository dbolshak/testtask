package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.TimeStampInfo;

import java.util.concurrent.ExecutionException;

/**
 * Created by dbolshak on 05.09.2014.
 */
public interface CacheService {
    /**
     * Removes a cached value based on absolute file path
     *
     * @param file
     */
    void remove(String file);

    /**
     * Removes a cached value based on topic and timeStamp
     */
    void remove(String topic, String timeStamp);

    /**
     * Returns value from cache. In case of cache missing it loads it to cache.
     *
     * @param file
     * @return
     * @throws InterruptedException
     */
    TimeStampInfo get(String file) throws InterruptedException, ExecutionException;

    TimeStampInfo get(String topic, String timeStamp) throws InterruptedException, ExecutionException;
}
