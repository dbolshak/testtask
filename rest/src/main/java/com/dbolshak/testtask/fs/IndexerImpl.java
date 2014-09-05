package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.dao.TopicChangingNotifier;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.dao.cache.CacheService;
import com.dbolshak.testtask.utils.Helper;
import org.apache.commons.vfs2.FileSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by dbolshak on 04.09.2014.
 */
@Service("indexer")
public class IndexerImpl implements Indexer {
    @Autowired
    private TopicChangingNotifier topicChangingNotifier;
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private CacheService cacheService;


    @Override
    public void setBaseDir(final String baseDir) throws FileSystemException {
        cacheService.setBaseDir(baseDir);
        File root = new File(baseDir);
        File[] topics = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Path pattern = Paths.get(dir.getAbsolutePath() + Helper.FILE_SEPARATOR + name + Helper.FILE_SEPARATOR + Helper.HISTORY_SUBFOLDER);
                return Files.exists(pattern) || Files.isDirectory(pattern);
            }
        });
        for (final File topic : topics) {
            final String topicStr = topic.getName();
            File history = new File(topic.getAbsolutePath() + Helper.FILE_SEPARATOR + Helper.HISTORY_SUBFOLDER);
            history.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String timeStamp) {
                    if (Helper.validateFileName(Helper.getFileName(baseDir, topicStr, timeStamp))) {
                        topicDao.addTimeStamp(timeStamp, topicStr);
                    }
                    return true;
                }
            });
        }
        topicChangingNotifier.setBaseDir(baseDir);
    }
}
