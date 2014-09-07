package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.dao.TopicChangingNotifier;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.dao.cache.CacheService;
import com.dbolshak.testtask.utils.Helper;
import org.apache.commons.vfs2.FileSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by dbolshak on 04.09.2014.
 */
@Service("indexer")
public class IndexerImpl implements Indexer {
    private final static Logger LOG = LoggerFactory.getLogger(IndexerImpl.class);
    @Autowired
    private TopicChangingNotifier topicChangingNotifier;
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private CacheService cacheService;


    @Override
    public void setBaseDir(final String baseDir) throws FileSystemException {
        LOG.info (String.format("Going to index: %s directory", baseDir));

        cacheService.setBaseDir(baseDir);
        File root = new File(baseDir);
        File[] topics = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Path pattern = Paths.get(dir.getAbsolutePath() + Helper.FILE_SEPARATOR + name + Helper.FILE_SEPARATOR + Helper.HISTORY_SUBFOLDER);
                return Files.exists(pattern) || Files.isDirectory(pattern);
            }
        });

        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<Future<Void>> list = new ArrayList<>(1000);

        for (final File topic : topics) {
            Future<Void> future = executor.submit(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    final String topicStr = topic.getName();
                    File history = new File(topic.getAbsolutePath() + Helper.FILE_SEPARATOR + Helper.HISTORY_SUBFOLDER);
                    final String[] latestRunning = new String[]{""};
                    history.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String timeStamp) {
                            if (latestRunning[0].compareTo(timeStamp) < 0) {
                                latestRunning[0] = timeStamp;
                            }
                            return true;
                        }
                    });
                    topicDao.addTimeStamp(latestRunning[0], topicStr);
                    return null;
                }
            });
            list.add(future);
        }
        for(Future<Void> fut : list){
            try {
                fut.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        new Thread(new Runnable() {//VFS works slow if need to handle a lot of files
            @Override
            public void run() {
                try {
                    topicChangingNotifier.setBaseDir(baseDir);
                } catch (FileSystemException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }).start();

        LOG.info ("Indexing has finished");
    }
}
