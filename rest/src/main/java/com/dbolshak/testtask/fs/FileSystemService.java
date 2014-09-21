package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.TimeStamp;

/**
 * A customer interface to work with file system.
 */
public interface FileSystemService {
    /**
     * Returns the last timestamp of topic run
     *
     * @param topic a topic for which we want to find the last timestamp of run
     * @return A string like "1984-12-19-00-00-00" or empty string if there is no timestamps at all
     */
    String getLastRun(String topic);

    /**
     * Returns a absolute file name where information about timestamp for specified topic could be found.
     *
     * @param timeStamp topic and timeStamp can identify the absolute path to a timestamp file
     * @return absolute path to a timestamp file
     */
    String getAbsoluteFileName(TimeStamp timeStamp);
}
