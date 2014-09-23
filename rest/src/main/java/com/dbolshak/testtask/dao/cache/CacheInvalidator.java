package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.model.TimeStamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("timeStampListener")
/*timeStampListener bean must be registered before cacheInvalidator bean*/
class CacheInvalidator extends AbstractTopicChangingListener {
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
