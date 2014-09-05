package com.dbolshak.testtask.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TimeStampListener implements TopicChangingListener {
    @Autowired
    private TopicChangingNotifier topicChangingNotifier;
    @Autowired
    private TopicDao topicDao;

    @Override
    public void onTimeStampAdded(String topic, String timeStamp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTimeStampModified(String topic, String timeStamp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTimeStampDeleted(String topic, String timeStamp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @PostConstruct
    public void register() {
        topicChangingNotifier.addListener(this);
    }

}
