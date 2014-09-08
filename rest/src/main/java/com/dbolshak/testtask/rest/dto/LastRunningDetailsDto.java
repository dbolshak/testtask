package com.dbolshak.testtask.rest.dto;

import java.util.Map;

/**
 * Simple DTO object which is a middle layer object between frontend (JSON) and backend (POJO)
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
