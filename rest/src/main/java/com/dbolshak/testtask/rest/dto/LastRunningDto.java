package com.dbolshak.testtask.rest.dto;

/**
 * Simple DTO object which is a middle layer object between frontend (JSON) and backend (POJO)
 */
public class LastRunningDto {
    private final String topic;
    private String lastRunning;

    public LastRunningDto(String topic) {
        this.topic = topic;
    }

    public String getLastRunning() {
        return lastRunning;
    }

    public void setLastRunning(String lastRunning) {
        this.lastRunning = lastRunning;
    }

    public String getTopic() {
        return topic;
    }
}
