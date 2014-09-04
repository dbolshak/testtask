package com.dbolshak.testtask.fs;

import java.text.ParseException;

/**
 * Created by dbolshak on 04.09.2014.
 */
public interface Indexer {
    /**
     * Finds a path to csv file for specified topic which has collected information by last running
     *
     * @param topic - topic for which we need to provide a path to a file with collected information by last running
     * @return absolute file path
     */
    String getLastRunning(String topic) throws ParseException;

    /**
     * Indexes a new file and associated it with specified topic.
     * @param path - absolute path to a file
     * @param topic - topic for which new file belongs to.
     * @param timeStamp - timestamp when file was created (it's part of file path).
     */
    //TODO: to think about throwing exception
    void addFileToIndex(String path, String topic, String timeStamp) throws ParseException;

    /**
     * Removes a file from index.
     * @param path absolute path to a file
     * @param topic topic.
     */
    void removeFileFromIndex(String path, String topic);

    /**
     * Sets a baseDir where we need to index content with topics
     * @param baseDir a file system path to directory with topics (absolute or relative).
     * @note In ideal case the method could be called only once (during bootstrapping application)
     */
    void setBaseDir(String baseDir);
}
