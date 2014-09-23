package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.fs.FileSystemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeStampListener extends AbstractTopicChangingListener {
    @Autowired
    private TopicCacheDao topicCacheDao;
    @Autowired
    private FileSystemDao fileSystemDao;

    @Override
    public void onTimeStampAdded(TimeStamp timeStamp) {
        topicCacheDao.addTimeStamp(timeStamp);
    }

    @Override
    public void onTimeStampDeleted(TimeStamp timeStamp) {
        topicCacheDao.removeTimeStamp(timeStamp);
        //just in case if someone deleted the latest timestamp we need to refresh it
        topicCacheDao.addTimeStamp(new TimeStamp(timeStamp.getTopic(), fileSystemDao.findLastRun(timeStamp.getTopic())));
    }
}
