package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.dao.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Repository
public class TopicDaoImpl implements TopicDao {
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
        ConcurrentSkipListSet<String> timeStamps = storage.get(timeStamp.getTopic());
        if (timeStamps != null && !timeStamps.isEmpty()) {
            timeStamps.remove(timeStamp.getRun());
        }
    }

    @Override
    public void addTimeStamp(TimeStamp timeStamp) {
        ConcurrentSkipListSet<String> timeStamps = findOrCreate(timeStamp.getTopic());
        timeStamps.add(timeStamp.getRun());
    }

    @Override
    public String findLastRun(String topic) {
        ConcurrentSkipListSet<String> timeStamps = storage.get(topic);
        if (timeStamps != null && !timeStamps.isEmpty()) {
            return timeStamps.last();
        }
        return "";
    }

    @Override
    public TimeStampContent findTimeStampContent(TimeStamp timeStamp) {
        return cacheService.get(timeStamp.getTopic(), timeStamp.getRun());
    }

    @Override
    public boolean topicExists(String topic) {
        ConcurrentSkipListSet<String> timeStamps = storage.get(topic);
        return timeStamps != null && !timeStamps.isEmpty();
    }

    private ConcurrentSkipListSet<String> findOrCreate(String topic) {
        storage.putIfAbsent(topic, new ConcurrentSkipListSet<String>());
        return storage.get(topic);
    }
}
