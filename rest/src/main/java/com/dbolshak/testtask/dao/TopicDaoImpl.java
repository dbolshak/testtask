package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.dao.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;

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
    public void removeTimeStamp(String timeStamp, String topic) {
        ConcurrentSkipListSet<String> timeStamps = storage.get(topic);
        if (timeStamps != null && !timeStamps.isEmpty()) {
            timeStamps.remove(timeStamp);
        }
    }

    @Override
    public void addTimeStamp(String timeStamp, String topic) {
        ConcurrentSkipListSet<String> timeStamps = findOrCreate(topic);
        timeStamps.add(timeStamp);
    }

    @Override
    public String findLastRunningFor(String topic) {
        ConcurrentSkipListSet<String> timeStamps = storage.get(topic);
        if (timeStamps != null && !timeStamps.isEmpty()) {
            return timeStamps.last();
        }
        return "";
    }

    @Override
    public TimeStampContent findTimeStampInfo(String topic, String timeStamp) throws ExecutionException, InterruptedException {
        return cacheService.get(topic, timeStamp);
    }

    @Override
    public boolean topicExists(String topic) {
        ConcurrentSkipListSet<String> timeStamps = storage.get(topic);
        return timeStamps != null && !timeStamps.isEmpty();
    }

    private ConcurrentSkipListSet<String> findOrCreate(String topic) {
        ConcurrentSkipListSet<String> running = new ConcurrentSkipListSet<>();
        storage.putIfAbsent(topic, running);
        return storage.get(topic);
    }
}
