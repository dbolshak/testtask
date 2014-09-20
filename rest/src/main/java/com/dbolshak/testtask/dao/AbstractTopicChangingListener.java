package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class AbstractTopicChangingListener implements TopicChangingListener {
    @Autowired
    private TopicChangingNotifier topicChangingNotifier;

    @Override
    public void onTimeStampAdded(TimeStamp timeStamp) {
    }

    @Override
    public void onTimeStampModified(TimeStamp timeStamp) {
    }

    @Override
    public void onTimeStampDeleted(TimeStamp timeStamp) {
    }

    @Override
    @PostConstruct
    public void register() {
        topicChangingNotifier.addListener(this);
    }
}
