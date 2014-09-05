package com.dbolshak.testtask.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeStampListener extends AbstractTopicChangingListener {
    @Autowired
    private TopicDao topicDao;

    @Override
    public void onTimeStampAdded(String topic, String timeStamp) {
        topicDao.addTimeStamp(timeStamp, topic);
    }

    @Override
    public void onTimeStampDeleted(String topic, String timeStamp) {
        topicDao.removeTimeStamp(timeStamp, topic);
    }
}
