package com.dbolshak.testtask.dao;

/**
 * This interface provides a basic API for working with listeners
 */
public interface TopicChangingNotifier {
    /**
     * initializes out notifier.
     */
    void init();

    /**
     * Adds a listener, which will be notified about changes in baseDir
     *
     * @param topicChangingListener instance of TopicChangingListener which wants to get notification about file system changes.
     */
    void addListener(TopicChangingListener topicChangingListener);

    /**
     * Removes a listener from subscription
     *
     * @param topicChangingListener instance of TopicChangingListener which does not want to get notification about file system changes any more.
     */
    void removeListener(TopicChangingListener topicChangingListener);
}
