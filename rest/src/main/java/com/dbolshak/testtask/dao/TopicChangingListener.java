package com.dbolshak.testtask.dao;

/**
 * Created with IntelliJ IDEA.
 * User: dbolshak
 * Date: 9/5/14
 * Time: 7:40 AM
 * To change this template use File | Settings | File Templates.
 */
public interface TopicChangingListener {
    /***
     * Callback to handle adding a new file
     * @param topic
     * @param timeStamp
     */
    void onTimeStampAdded(String topic, String timeStamp);

    /***
     * Callback to handle modifying a new file
     * @param topic
     * @param timeStamp
     */
    void onTimeStampModified(String topic, String timeStamp);

    /***
     * Callback to handle deleting a file
     * @param topic
     * @param timeStamp
     */
    void onTimeStampDeleted(String topic, String timeStamp);

    /**
     * Registers me to get following events
     */
    void register();

}
