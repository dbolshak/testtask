package com.dbolshak.testtask.dao;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by dbolshak on 05.09.2014.
 */
public class TimeStampContent {
    private final Map<Integer, Long> partitionToMessageCount = new ConcurrentSkipListMap<>();
    private final Map<Integer, Long> unmodifiedCopy = Collections.unmodifiableMap(partitionToMessageCount);

    public Map<Integer, Long> getContent() {
        return unmodifiedCopy;
    }

    public void put(Integer partition, Long messageCount) {
        partitionToMessageCount.put(partition, messageCount);
    }
}
