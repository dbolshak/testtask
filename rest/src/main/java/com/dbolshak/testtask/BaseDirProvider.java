package com.dbolshak.testtask;

/**
 * Sets a base dir, so all classes who need information about baseDir can inject this bean
 */
public interface BaseDirProvider {
    /**
     * @return current baseDir or null, if not set yet
     */
    String getBaseDir();

    /**
     * Sets a baseDir which is provided during application bootstrap
     *
     * @param baseDir a folder where topics are located, could be provided by absolute or relative path.
     */
    void setBaseDir(String baseDir);
}
