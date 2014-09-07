package com.dbolshak.testtask;

/**
 * Sets a base dir, so all classes who need information about baseDir can inject this bean
 */
public interface BaseDirProvider {
    /**
     * Sets a baseDir which is provided during application bootstrap
     *
     * @param baseDir
     */
    void setBaseDir(String baseDir);

    /**
     * @return current baseDir or null, if not set yet
     */
    String getBaseDir();
}
