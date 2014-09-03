package com.dbolshak.testtask.model.dto;

import java.util.Map;

/**
 * Created by dbolshak on 03.09.2014.
 */
public class LastRunningDetailsDto {
    private String topic;
    private Map<Integer, Integer> messagesForPartition;

    public LastRunningDetailsDto(String topic) {
        this.topic = topic;
    }

    public Map<Integer, Integer> getMessagesForPartition() {
        return messagesForPartition;
    }

    public void setMessagesForPartition(Map<Integer, Integer> messagesForPartition) {
        this.messagesForPartition = messagesForPartition;
    }

    public String getTopic() {
        return topic;
    }
}
