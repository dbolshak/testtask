package com.dbolshak.testtask.dao;

/**
 * This interface provides a basic API for working with listeners
 */
public interface TopicChangingNotifier {
    /**
     * inits out notifier
     */
    void init();

    /**
     * Adds a listener, which will be notified about changes in baseDir
     *
     * @param topicChangingListener
     */
    void addListener(TopicChangingListener topicChangingListener);

    /**
     * Removes a listener from subscription
     *
     * @param topicChangingListener
     */
    void deleteListener(TopicChangingListener topicChangingListener);
}
