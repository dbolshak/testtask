package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.TimeStamp;

public interface TopicCacheDao extends TopicDao {
    /**
     * Removes all available information from internal structures
     */
    void clear();

    /**
     * removes specified timestamp for topic
     *
     * @param timeStamp timestamp which is needed to be removed
     */
    void removeTimeStamp(TimeStamp timeStamp);

    /**
     * Adds timestamp to topic
     *
     * @param timeStamp timestamp which is needed to be added.
     */
    void addTimeStamp(TimeStamp timeStamp);
}
