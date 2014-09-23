package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.model.TimeStamp;
import com.dbolshak.testtask.model.TimeStampContent;

/**
 * Provides a basic API to get cached content or remove it.
 */
interface CacheService {
    /**
     * Removes a cached value based on topic and timeStamp
     *
     * @param timeStamp timeStamp which was cached and now must be removed from cache.
     */
    void remove(TimeStamp timeStamp);

    /**
     * Returns value from cache. In case of cache missing it loads it to cache.
     *
     * @param timeStamp timeStamp for which we want to get content.
     * @return content of specified file.
     */
    TimeStampContent get(TimeStamp timeStamp);
}
