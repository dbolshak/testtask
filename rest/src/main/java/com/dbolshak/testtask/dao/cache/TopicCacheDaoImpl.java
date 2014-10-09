package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.model.TimeStamp;
import com.dbolshak.testtask.model.TimeStampContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Repository("topicCacheDao")
class TopicCacheDaoImpl implements TopicCacheDao {
    private final ConcurrentHashMap<String, ConcurrentSkipListSet<String>> storage = new ConcurrentHashMap<>(500);
    @Autowired
    private CacheService cacheService;

    @Override
    public Collection<String> findAllTopics() {
        return storage.keySet();
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void removeTimeStamp(TimeStamp timeStamp) {
        if (topicExists(timeStamp.getTopic())) {
            storage.get(timeStamp.getTopic()).remove(timeStamp.getRun());
        }
    }

    @Override
    public void addTimeStamp(TimeStamp timeStamp) {
        findOrCreateTimeStampsForTopic(timeStamp.getTopic()).add(timeStamp.getRun());
    }

    @Override
    public String findLastRun(String topic) {
        return topicExists(topic) ? storage.get(topic).last() : "";
    }

    @Override
    public TimeStampContent findTimeStampContent(TimeStamp timeStamp) {
        return cacheService.get(timeStamp);
    }

    @Override
    public boolean topicExists(String topic) {
        ConcurrentSkipListSet<String> timeStamps = storage.get(topic);
        return timeStamps != null && !timeStamps.isEmpty();
    }

    private ConcurrentSkipListSet<String> findOrCreateTimeStampsForTopic(String topic) {
        storage.putIfAbsent(topic, new ConcurrentSkipListSet<String>());
        return storage.get(topic);
    }
}
