package com.dbolshak.testtask.dao;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TopicChangingNotifierImpl implements TopicChangingNotifier {
    Collection<TopicChangingListener> listeners = new CopyOnWriteArrayList<>();
    @Override
    public void setBaseDir(String baseDir) {

    }

    @Override
    public void addListener(TopicChangingListener topicChangingListener) {
        listeners.add(topicChangingListener);
    }

    @Override
    public void deleteListener(TopicChangingListener topicChangingListener) {
        listeners.remove(topicChangingListener);
    }
}
