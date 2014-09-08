package com.dbolshak.testtask.dao;

/**
 * If you want to handle file system changes please implement this interface and register your instance.
 */
public interface TopicChangingListener {
    /**
     * Callback to handle adding a new file
     *
     * @param topic
     * @param timeStamp
     */
    void onTimeStampAdded(String topic, String timeStamp);

    /**
     * Callback to handle modifying a new file
     *
     * @param topic
     * @param timeStamp
     */
    void onTimeStampModified(String topic, String timeStamp);

    /**
     * Callback to handle deleting a file
     *
     * @param topic
     * @param timeStamp
     */
    void onTimeStampDeleted(String topic, String timeStamp);

    /**
     * Registers me to get following events
     */
    void register();
}
