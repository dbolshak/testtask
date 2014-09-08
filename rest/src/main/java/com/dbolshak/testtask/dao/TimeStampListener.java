package com.dbolshak.testtask.dao;

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
    public void onTimeStampAdded(String topic, String timeStamp) {
        topicDao.addTimeStamp(timeStamp, topic);
    }

    @Override
    public void onTimeStampDeleted(String topic, String timeStamp) {
        topicDao.removeTimeStamp(timeStamp, topic);
        //just in case if someone deleted the latest timestamp we need to refresh it
        topicDao.addTimeStamp(fileSystemService.getLatestRunning(topic), topic);
    }
}
