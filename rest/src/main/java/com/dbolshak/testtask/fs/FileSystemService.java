package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.TimeStamp;

import java.io.File;

/**
 * A customer interface to work with file system.
 */
public interface FileSystemService {
    /**
     * Returns a absolute file name where information about timestamp for specified topic could be found.
     *
     * @param timeStamp topic and timeStamp can identify the absolute path to a timestamp file
     * @return absolute path to a timestamp file
     */
    String getAbsoluteFileName(TimeStamp timeStamp);

    /**
     * Returns from file system all topics in base_dir
     *
     * @return array of topics
     */
    File[] findAllTopics();
}
