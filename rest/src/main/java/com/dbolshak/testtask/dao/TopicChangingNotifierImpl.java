package com.dbolshak.testtask.dao;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dbolshak
 * Date: 9/5/14
 * Time: 7:51 AM
 * To change this template use File | Settings | File Templates.
 */
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
