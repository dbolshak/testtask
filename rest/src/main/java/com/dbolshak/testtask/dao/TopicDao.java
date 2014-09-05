package com.dbolshak.testtask.dao;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: dbolshak
 * Date: 9/5/14
 * Time: 8:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TopicDao {
    Collection<String> findAllTopics();
    void removeTopic(String topic);
    void clear();
    void removeTimeStamp(String timeStamp, String topic);
    void addTimeStamp(String timeStamp, String topic);
    String lastTimeStamp();
}
