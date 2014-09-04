package com.dbolshak.testtask.rest.service;

import java.util.Collection;

/**
 * Created by dbolshak on 04.09.2014.
 */
public interface TopicDao {
    /***
     * @return all available topics
     */
    Collection<String> findAll();

    /**
     * @param topic a topic
     * @return timestamp of specified topic
     */
    long getLastRunning(String topic);

    /**
     * Returns full available information for specified running and topic.
     * @param topic
     * @param timestamp
     */
    void getFullInfoForRunning(String topic, long timestamp);
}
