package com.dbolshak.testtask.dao;


import com.dbolshak.testtask.TimeStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class AbstractTopicChangingListener implements TopicChangingListener {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTopicChangingListener.class);
    @Autowired
    private TopicChangingNotifier topicChangingNotifier;

    @Override
    public void onTimeStampAdded(TimeStamp timeStamp) {
        LOG.trace("onTimeStampAdded");
    }

    @Override
    public void onTimeStampModified(TimeStamp timeStamp) {
        LOG.trace("onTimeStampModified");
    }

    @Override
    public void onTimeStampDeleted(TimeStamp timeStamp) {
        LOG.trace("onTimeStampDeleted");
    }

    @Override
    @PostConstruct
    public void register() {
        topicChangingNotifier.addListener(this);
    }
}
