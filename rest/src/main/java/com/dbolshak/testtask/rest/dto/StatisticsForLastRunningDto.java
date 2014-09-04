package com.dbolshak.testtask.rest.dto;

/**
 * Created by dbolshak on 03.09.2014.
 */
//int is not enough. TODO:FIXME
public class StatisticsForLastRunningDto {
    private String topic;
    private int total;
    private int min;
    private int max;
    private int average;

    public StatisticsForLastRunningDto(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }
}
