package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.model.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class TimeStampListener extends AbstractTopicChangingListener {
    @Autowired
    private TopicCacheDao topicCacheDao;
    @Autowired
    private TopicDao fileSystemDao;

    @Override
    public void onTimeStampAdded(TimeStamp timeStamp) {
        topicCacheDao.addTimeStamp(timeStamp);
    }

    @Override
    public void onTimeStampDeleted(TimeStamp timeStamp) {
        topicCacheDao.removeTimeStamp(timeStamp);
        //just in case if someone deleted the latest timestamp we need to refresh it
        topicCacheDao.addTimeStamp(new TimeStamp(timeStamp.getTopic(), fileSystemDao.findLastRun(timeStamp.getTopic())));
    }
}
