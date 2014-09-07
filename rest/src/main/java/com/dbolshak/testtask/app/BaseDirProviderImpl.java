package com.dbolshak.testtask.app;

import com.dbolshak.testtask.app.BaseDirProvider;
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
