package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.model.TimeStamp;

/**
 * If you want to handle file system changes please implement this interface and register your instance.
 */
interface TopicChangingListener {
    /**
     * Callback to handle adding a new file
     *
     * @param timeStamp a new timestamp on the file system.
     */
    void onTimeStampAdded(TimeStamp timeStamp);

    /**
     * Callback to handle modifying a new file
     *
     * @param timeStamp a modified timestamp on the file system.
     */
    void onTimeStampModified(TimeStamp timeStamp);

    /**
     * Callback to handle deleting a file
     *
     * @param timeStamp a deleted timestamp on the file system.
     */
    void onTimeStampDeleted(TimeStamp timeStamp);

    /**
     * Registers me to get following events
     */
    void register();
}
