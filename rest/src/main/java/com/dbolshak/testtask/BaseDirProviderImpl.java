package com.dbolshak.testtask;

import org.springframework.stereotype.Component;

@Component
public class BaseDirProviderImpl implements BaseDirProvider {
    private volatile String baseDir; //Write operation must be happen only once

    @Override
    public String getBaseDir() {
        return baseDir;
    }

    @Override
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
