package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.annotation.PostSetDir;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.exceptions.ApplicationRuntimeException;
import com.dbolshak.testtask.fs.FileSystemService;
import com.dbolshak.testtask.model.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
/*class has `public` access in order to call @PostSetDir by reflection*/
public class IndexerImpl implements Indexer {
    private static final Logger LOG = LoggerFactory.getLogger(IndexerImpl.class);

    @Autowired
    private TopicCacheDao topicCacheDao;
    @Autowired
    private TopicDao fileSystemDao;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    @PostSetDir
    public void index() {
        LOG.info("Going to index: base_dir");

        ExecutorService executor = Executors.newFixedThreadPool(16);
        File[] topics = fileSystemService.findAllTopics();
        List<Future<TimeStamp>> list = new ArrayList<>(topics.length);

        for (final File topic : topics) {
            Future<TimeStamp> future = executor.submit(new Callable<TimeStamp>() {
                @Override
                public TimeStamp call() throws Exception {
                    String topicStr = topic.getName();
                    return new TimeStamp(topicStr, fileSystemDao.findLastRun(topicStr));
                }
            });
            list.add(future);
        }
        for (Future<TimeStamp> fut : list) {
            try {
                TimeStamp timeStamp = fut.get();
                topicCacheDao.addTimeStamp(timeStamp);
            } catch (InterruptedException | ExecutionException e) {
                throw new ApplicationRuntimeException("An exception", e);
            }
        }
        executor.shutdown();

        LOG.info("Indexing has finished");
    }
}
