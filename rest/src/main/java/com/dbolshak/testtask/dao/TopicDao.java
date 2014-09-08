package com.dbolshak.testtask.dao;

import java.util.Collection;

public interface TopicDao {
    /**
     * returns all available topics
     *
     * @return
     */
    Collection<String> findAllTopics();

    /**
     * Removes all available information
     */
    void clear();

    /**
     * removes specified timestamp for topic
     *
     * @param timeStamp
     * @param topic
     */
    void removeTimeStamp(String timeStamp, String topic);

    /**
     * Adds timestamp to topic
     *
     * @param timeStamp
     * @param topic
     */
    void addTimeStamp(String timeStamp, String topic);

    /**
     * Returns time stamp of the latest running for specified topic
     *
     * @param topic
     * @return
     */
    String findLastRunningFor(String topic);

    /**
     * Returns all information about specified topic and timestamp or null if not found.
     *
     * @param topic
     * @param timeStamp
     * @return
     */
    TimeStampContent findTimeStampContent(String topic, String timeStamp);

    /**
     * Check that specified topic exists
     *
     * @param topic
     * @return
     */
    boolean topicExists(String topic);
}
