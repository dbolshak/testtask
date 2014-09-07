package com.dbolshak.testtask.rest.dto;

import java.util.Map;

/**
 * Created by dbolshak on 03.09.2014.
 */
public class LastRunningDetailsDto {
    private final String topic;
    private Map<Integer, Long> messagesForPartition;

    public LastRunningDetailsDto(String topic) {
        this.topic = topic;
    }

    public Map<Integer, Long> getMessagesForPartition() {
        return messagesForPartition;
    }

    public void setMessagesForPartition(Map<Integer, Long> messagesForPartition) {
        this.messagesForPartition = messagesForPartition;
    }

    public String getTopic() {
        return topic;
    }
}
