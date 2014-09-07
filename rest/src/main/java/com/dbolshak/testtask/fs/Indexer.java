package com.dbolshak.testtask.fs;

import org.apache.commons.vfs2.FileSystemException;

/**
 * Created by dbolshak on 04.09.2014.
 */
public interface Indexer {
    /**
     * Start indexing and other necessary service when application is ready
     */
    void start() throws FileSystemException;
}
