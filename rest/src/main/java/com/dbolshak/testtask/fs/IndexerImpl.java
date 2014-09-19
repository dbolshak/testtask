package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.annotation.PostSetDir;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static com.dbolshak.testtask.utils.Helper.FILE_SEPARATOR;

@Service
public class IndexerImpl implements Indexer {
    private final static Logger LOG = LoggerFactory.getLogger(IndexerImpl.class);

    @Autowired
    private TopicDao topicDao;
    @Autowired
    private BaseDirProvider baseDirProvider;
    @Autowired
    private FileSystemService fileSystemService;


    @Override
    @PostSetDir
    public void index() {
        LOG.info(String.format("Going to index: %s directory", baseDirProvider.getBaseDir()));

        File root = new File(baseDirProvider.getBaseDir());
        File[] topics = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Path pattern = Paths.get(dir.getAbsolutePath() + FILE_SEPARATOR + name + FILE_SEPARATOR + Helper.HISTORY_SUB_FOLDER);
                return Files.exists(pattern) || Files.isDirectory(pattern);
            }
        });

        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<Future<Map.Entry<String, String>>> list = new ArrayList<>(1000);

        for (final File topic : topics) {
            Future<Map.Entry<String, String>> future = executor.submit(new Callable<Map.Entry<String, String>>() {
                @Override
                public Map.Entry<String, String> call() throws Exception {
                    String topicStr = topic.getName();
                    return new AbstractMap.SimpleEntry<>(fileSystemService.getLastRun(topicStr), topicStr);
                }
            });
            list.add(future);
        }
        for (Future<Map.Entry<String, String>> fut : list) {
            try {
                Map.Entry<String, String> timeStampAndTopic = fut.get();
                topicDao.addTimeStamp(timeStampAndTopic.getKey(), timeStampAndTopic.getValue());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();

        LOG.info("Indexing has finished");
    }
}
