package com.dbolshak.testtask.dao;

import org.apache.commons.vfs2.FileSystemException;

/**
 * This interface provides basic functionality for events propagating to their listeners
 */
public interface TopicChangingNotifier {
    /**
     * initializes our notifier
     */
    void init() throws FileSystemException;

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
