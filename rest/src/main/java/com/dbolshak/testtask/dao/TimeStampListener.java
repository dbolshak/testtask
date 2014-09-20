package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.fs.FileSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeStampListener extends AbstractTopicChangingListener {
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public void onTimeStampAdded(TimeStamp timeStamp) {
        topicDao.addTimeStamp(timeStamp);
    }

    @Override
    public void onTimeStampDeleted(TimeStamp timeStamp) {
        topicDao.removeTimeStamp(timeStamp);
        //just in case if someone deleted the latest timestamp we need to refresh it
        topicDao.addTimeStamp(new TimeStamp(timeStamp.getTopic(), fileSystemService.getLastRun(timeStamp.getTopic())));
    }
}
