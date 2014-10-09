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
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    @Autowired
    private TopicCacheDao topicCacheDao;
    @Autowired
    private TopicDao fileSystemDao;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    @PostSetDir
    public void index() {
        File[] topics = fileSystemService.findAllTopics();
        List<TimeStampTask> list = new ArrayList<>(topics.length);

        for (final File topic : topics) {
            list.add(new TimeStampTask(topic));
        }

        LOG.info("Going to index: base_dir");
        try {
            for (Future<TimeStamp> fut : executor.invokeAll(list)) {
                TimeStamp timeStamp = fut.get();
                topicCacheDao.addTimeStamp(timeStamp);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApplicationRuntimeException("exception during indexing", e);
        } catch (ExecutionException e) {
            throw new ApplicationRuntimeException("exception during indexing", e);
        } finally {
            LOG.info("Indexing has finished");
            executor.shutdown();
        }
    }

    private class TimeStampTask implements Callable<TimeStamp> {
        private final File topic;

        private TimeStampTask(File topic) {
            this.topic = topic;
        }

        public TimeStamp call() throws Exception {
            String topicStr = topic.getName();
            return new TimeStamp(topicStr, fileSystemDao.findLastRun(topicStr));
        }
    }
}
