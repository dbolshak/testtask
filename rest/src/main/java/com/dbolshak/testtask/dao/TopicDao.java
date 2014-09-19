package com.dbolshak.testtask.dao;

import java.util.Collection;

public interface TopicDao {
    /**
     * returns all available topics
     *
     * @return all available topics as a Collection of Strings
     */
    Collection<String> findAllTopics();

    /**
     * Removes all available information from internal structures
     */
    void clear();

    /**
     * removes specified timestamp for topic
     *
     * @param timeStamp timestamp which is needed to be removed
     * @param topic topic for which we need to remove a specified timestamp
     */
    void removeTimeStamp(String timeStamp, String topic);

    /**
     * Adds timestamp to topic
     *
     * @param timeStamp timestamp which is needed to be added
     * @param topic topic for which we need to add a specified timestamp
     */
    void addTimeStamp(String timeStamp, String topic);

    /**
     * Returns time stamp of the last run for specified topic
     *
     * @param topic a topic for what the latest time stamp must be returned
     * @return the latest timestamp as ("1984-12-19-00-00-00") or empty string if specified topic does not have timestamps
     */
    String findLastRun(String topic);

    /**
     * Returns all information about specified topic and timestamp or null if not found.
     *
     * @param topic a topic for which we need to get timestamp content
     * @param timeStamp a timestamp for which we need to get timestamp content
     * @return TimeStampContent for specified topic and timeStamp.
     */
    TimeStampContent findTimeStampContent(String topic, String timeStamp);

    /**
     * Check that specified topic exists
     *
     * @param topic a topic which need to be checked for existence.
     * @return true if topic exists.
     */
    boolean topicExists(String topic);
}
