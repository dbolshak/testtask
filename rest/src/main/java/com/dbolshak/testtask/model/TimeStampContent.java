package com.dbolshak.testtask.model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * A Pojo which wraps CSV file content to convenient structure.
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
