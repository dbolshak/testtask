package com.dbolshak.testtask;

import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: dbolshak
 * Date: 9/7/14
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class BaseDirProviderImpl implements BaseDirProvider {
    private String baseDir;

    @Override
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public String getBaseDir() {
        return baseDir;
    }
}
