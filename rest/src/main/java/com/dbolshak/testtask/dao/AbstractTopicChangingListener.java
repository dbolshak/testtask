package com.dbolshak.testtask.dao;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class AbstractTopicChangingListener implements TopicChangingListener {
    @Autowired
    private TopicChangingNotifier topicChangingNotifier;

    @Override
    public void onTimeStampAdded(String topic, String timeStamp) {
    }

    @Override
    public void onTimeStampModified(String topic, String timeStamp) {
    }

    @Override
    public void onTimeStampDeleted(String topic, String timeStamp) {
    }

    @Override
    @PostConstruct
    public void register() {
        topicChangingNotifier.addListener(this);
    }
}
