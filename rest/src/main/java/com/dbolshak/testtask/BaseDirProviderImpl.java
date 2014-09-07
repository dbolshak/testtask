package com.dbolshak.testtask;

import org.springframework.stereotype.Component;

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
