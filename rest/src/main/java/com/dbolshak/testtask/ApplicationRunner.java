package com.dbolshak.testtask;

import com.dbolshak.testtask.fs.Indexer;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationRunner implements CommandLineRunner {
    @Autowired
    private BaseDirProvider baseDirProvider;
    @Autowired
    private Indexer indexer;

    @Override
    public void run(String... strings) throws Exception {
        if (strings.length != 1) {
            throw new ApplicationRuntimeException("You must specify at least one (and exactly one) parameter which points to base_dir");
        }
        Path path = Paths.get(strings[0]);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new ApplicationRuntimeException("base_dir does not exist or it's not a directory.");
        }

        baseDirProvider.setBaseDir(path.toAbsolutePath().toString());
        indexer.start();
    }
}
