package com.dbolshak.testtask.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AbstractTopicChangingListener implements TopicChangingListener {
    protected String baseDir;
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

    @Override
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
