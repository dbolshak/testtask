package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.dao.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Repository
public class TopicDaoImpl implements TopicDao {
    private Map<String, TreeSet<String>> storage = new ConcurrentHashMap<>(100000);
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
        TreeSet<String> timeStamps = storage.get(topic);
        if (timeStamps != null && !timeStamps.isEmpty()) {
            timeStamps.remove(timeStamp);
        }
    }

    @Override
    public void addTimeStamp(String timeStamp, String topic) {
        TreeSet<String> timeStamps = findOrCreate(topic);
        timeStamps.add(timeStamp);
    }

    @Override
    public String findLastRunningFor(String topic) {
        TreeSet<String> timeStamps = storage.get(topic);
        if (timeStamps != null && !timeStamps.isEmpty()) {
            return timeStamps.last();
        }
        return "";
    }

    @Override
    public TimeStampInfo findTimeStampInfo(String topic, String timeStamp) throws ExecutionException, InterruptedException {
        return cacheService.get(topic, timeStamp);
    }

    @Override
    public boolean exists(String topic) {
        TreeSet<String> timeStamps = storage.get(topic);
        return timeStamps != null && !timeStamps.isEmpty();
    }

    private TreeSet<String> findOrCreate(String topic) {
        TreeSet<String> running = storage.get(topic);
        if (running == null) {
            running = new TreeSet<>();
            storage.put(topic, running);
        }
        return running;
    }
}
