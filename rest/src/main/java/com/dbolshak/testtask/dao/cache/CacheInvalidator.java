package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.AbstractTopicChangingListener;
import com.dbolshak.testtask.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheInvalidator extends AbstractTopicChangingListener {
    @Autowired
    CacheService cacheService;

    @Override
    public void onTimeStampModified(String topic, String timeStamp) {
        cacheService.remove(topic, timeStamp);
    }

    @Override
    public void onTimeStampDeleted(String topic, String timeStamp) {
        cacheService.remove(Helper.getFileName(baseDir, topic, timeStamp));
    }
}
