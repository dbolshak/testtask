package com.dbolshak.testtask.model.dto;

/**
 * Created by dbolshak on 03.09.2014.
 */
public class LastRunningDto {
    private String topic;
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
