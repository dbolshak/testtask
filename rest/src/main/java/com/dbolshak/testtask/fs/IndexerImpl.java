package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.annotation.PostSetDir;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class IndexerImpl implements Indexer {
    private static final Logger LOG = LoggerFactory.getLogger(IndexerImpl.class);

    @Autowired
    private TopicDao topicDao;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    @PostSetDir
    public void index() {
        LOG.info("Going to index: base_dir");

        ExecutorService executor = Executors.newFixedThreadPool(16);
        List<Future<TimeStamp>> list = new ArrayList<>(1000);

        for (final File topic : fileSystemService.getAllTopics()) {
            Future<TimeStamp> future = executor.submit(new Callable<TimeStamp>() {
                @Override
                public TimeStamp call() throws Exception {
                    String topicStr = topic.getName();
                    return new TimeStamp(topicStr, fileSystemService.getLastRun(topicStr));
                }
            });
            list.add(future);
        }
        for (Future<TimeStamp> fut : list) {
            try {
                TimeStamp timeStamp = fut.get();
                topicDao.addTimeStamp(timeStamp);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationRuntimeException("An exception", e);
            }
        }
        executor.shutdown();

        LOG.info("Indexing has finished");
    }
}
