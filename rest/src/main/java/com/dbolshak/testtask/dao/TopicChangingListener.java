package com.dbolshak.testtask.dao;

/**
 * If you want to handle file system changes please implement this interface and register your instance.
 */
interface TopicChangingListener {
    /**
     * Callback to handle adding a new file
     *
     * @param topic a topic for which new timeStamp has been just added
     * @param timeStamp a new timestamp on the file system.
     */
    void onTimeStampAdded(String topic, String timeStamp);

    /**
     * Callback to handle modifying a new file
     *
     * @param topic a topic for which new timeStamp has been just modified
     * @param timeStamp a modified timestamp on the file system.
     */
    void onTimeStampModified(String topic, String timeStamp);

    /**
     * Callback to handle deleting a file
     *
     * @param topic a topic for which new timeStamp has been just deleted
     * @param timeStamp a deleted timestamp on the file system.
     */
    void onTimeStampDeleted(String topic, String timeStamp);

    /**
     * Registers me to get following events
     */
    void register();
}
