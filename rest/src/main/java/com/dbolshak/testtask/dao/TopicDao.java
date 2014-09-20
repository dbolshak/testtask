package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.TimeStamp;

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
     */
    void removeTimeStamp(TimeStamp timeStamp);

    /**
     * Adds timestamp to topic
     *
     * @param timeStamp timestamp which is needed to be added.
     */
    void addTimeStamp(TimeStamp timeStamp);

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
     * @param timeStamp a timestamp for which we need to get timestamp content
     * @return TimeStampContent for specified topic and timeStamp.
     */
    TimeStampContent findTimeStampContent(TimeStamp timeStamp);

    /**
     * Check that specified topic exists
     *
     * @param topic a topic which need to be checked for existence.
     * @return true if topic exists.
     */
    boolean topicExists(String topic);
}
