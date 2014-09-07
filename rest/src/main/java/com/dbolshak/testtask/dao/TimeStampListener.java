package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.fs.FileSystemService;
import com.dbolshak.testtask.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;

@Component
public class TimeStampListener extends AbstractTopicChangingListener {
    @Autowired private TopicDao topicDao;
    @Autowired private FileSystemService fileSystemService;

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
