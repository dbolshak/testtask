package com.dbolshak.testtask.fs;

import org.apache.commons.vfs2.FileSystemException;

/**
 * This interface provides functionality to index a base_dir.
 */
public interface Indexer {
    /**
     * Start indexing and other necessary service when application is ready
     */
    void index();
}
