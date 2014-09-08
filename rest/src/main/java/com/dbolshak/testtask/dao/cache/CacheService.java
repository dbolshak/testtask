package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.TimeStampContent;

/**
 * Provides a basic API to get cached content or remove it.
 */
public interface CacheService {
    /**
     * Removes a cached value based on absolute file path
     *
     * @param file absolute file name.
     */
    void remove(String file);

    /**
     * Removes a cached value based on topic and timeStamp
     *
     * @param topic topic for which we don't need to keep cached content any more for specified time stamp.
     * @param timeStamp timeStamp which was cached and now must be removed from cache.
     */
    void remove(String topic, String timeStamp);

    /**
     * Returns value from cache. In case of cache missing it loads it to cache.
     *
     * @param file absolute file name for which we want to get content from cache.
     * @return content of specified file.
     */
    TimeStampContent get(String file);

    /**
     * Returns value from cache. In case of cache missing it loads it to cache.
     *
     * @param topic topic for which we need to get cached content for specified time stamp.
     * @param timeStamp timeStamp for which we want to get content.
     * @return content of specified file.
     */
    TimeStampContent get(String topic, String timeStamp);
}
