package com.dbolshak.testtask.rest.dto;

/**
 * Simple DTO object which is a middle layer object between frontend (JSON) and backend (POJO)
 */
public class LastRunDto {
    private final String topic;
    private String lastRun;

    public LastRunDto(String topic) {
        this.topic = topic;
    }

    public String getLastRun() {
        return lastRun;
    }

    public void setLastRun(String lastRun) {
        this.lastRun = lastRun;
    }

    public String getTopic() {
        return topic;
    }
}
