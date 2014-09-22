package com.dbolshak.testtask;

import org.springframework.stereotype.Component;

@Component
public class BaseDirProviderImpl implements BaseDirProvider {
    //Write operation must be happen only once
    private volatile String baseDir;

    @Override
    public String getBaseDir() {
        return baseDir;
    }

    @Override
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
