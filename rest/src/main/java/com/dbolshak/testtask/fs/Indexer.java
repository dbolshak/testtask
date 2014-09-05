package com.dbolshak.testtask.fs;

import org.apache.commons.vfs2.FileSystemException;

/**
 * Created by dbolshak on 04.09.2014.
 */
public interface Indexer {
    /**
     * Sets a baseDir where we need to index content with topics
     * @param baseDir a file system path to directory with topics (absolute or relative).
     * @note In ideal case the method could be called only once (during bootstrapping application)
     */
    void setBaseDir(String baseDir) throws FileSystemException;
}
