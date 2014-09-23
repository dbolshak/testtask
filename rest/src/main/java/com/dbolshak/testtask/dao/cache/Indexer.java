package com.dbolshak.testtask.dao.cache;

/**
 * This interface provides functionality to index a base_dir.
 */
interface Indexer {
    /**
     * Start indexing and other necessary service when application is ready
     */
    void index();
}
