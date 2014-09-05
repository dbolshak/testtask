package com.dbolshak.testtask.dao;

import org.springframework.stereotype.Component;

@Component
public class TimeStampListener extends AbstractTopicChangingListener {
    //@Autowired
    //private TopicDao topicDao;

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
}
