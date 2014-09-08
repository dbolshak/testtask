package com.dbolshak.testtask.dao;

/**
 * A basic interface which can provide content from specified file.
 */
public interface Computable {
    /**
     * Parses specified file and returns content.
     *
     * @param file file for which we need to get content
     * @return content of specified file.
     */
    TimeStampContent compute(String file);
}
