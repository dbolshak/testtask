package com.dbolshak.testtask.app;

import com.dbolshak.testtask.dao.TopicChangingNotifier;
import com.dbolshak.testtask.fs.Indexer;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationRunner.class);
    @Autowired
    private BaseDirProvider baseDirProvider;
    @Autowired
    private Indexer indexer;
    @Autowired
    private TopicChangingNotifier topicChangingNotifier;

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

        new Thread(new Runnable() {//VFS works slow if need to handle a lot of files
            @Override
            public void run() {
                try {
                    topicChangingNotifier.init();
                } catch (FileSystemException e) {
                    LOG.warn ("Listening to file system events based on VFS is failed becuase exception", e);
                }
            }
        }).start();
    }
}
