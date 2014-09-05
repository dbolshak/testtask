package com.dbolshak.testtask.dao;

/**
 * Created with IntelliJ IDEA.
 * User: dbolshak
 * Date: 9/5/14
 * Time: 7:44 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TopicChangingNotifier {
    /**
     * Sets a directory where topics are located
     * @param baseDir absolute or relative path to a directory
     */
    void setBaseDir(String baseDir);

    /**
     * Adds a listener, which will be notified about changes in baseDir
     * @param topicChangingListener
     */
    void addListener(TopicChangingListener topicChangingListener);

    /**
     * Removes a listener from subscription
     * @param topicChangingListener
     */
    void deleteListener(TopicChangingListener topicChangingListener);
}
