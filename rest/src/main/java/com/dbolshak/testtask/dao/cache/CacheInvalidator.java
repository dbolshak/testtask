package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.dao.AbstractTopicChangingListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("timeStampListener")
public class CacheInvalidator extends AbstractTopicChangingListener {
    @Autowired
    private CacheService cacheService;

    @Override
    public void onTimeStampModified(TimeStamp timeStamp) {
        invalidateCacheEntry(timeStamp);
    }

    @Override
    public void onTimeStampDeleted(TimeStamp timeStamp) {
        invalidateCacheEntry(timeStamp);
    }

    private void invalidateCacheEntry(TimeStamp timeStamp) {
        cacheService.remove(timeStamp);
    }
}
