package com.dbolshak.testtask.fs;

/**
 * A customer interface to work with file system.
 */
public interface FileSystemService {
    /**
     * Returns the latest timestamp of topic running
     *
     * @param topic a topic for which we want to find the latest timestamp of running
     * @return A string like "1984-12-19-00-00-00" or empty string if there is no timestamps at all
     */
    String getLatestRunning(String topic);

    /**
     * Returns a absolute file name where information about timestamp for specified topic could be found.
     *
     * @param topic topic and timeStamp can identify the absolute path to a timestamp file
     * @param timeStamp topic and timeStamp can identify the absolute path to a timestamp file
     * @return absolute path to a timestamp file
     */
    String getAbsoluteFileName(String topic, String timeStamp);
}
