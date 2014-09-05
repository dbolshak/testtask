package com.dbolshak.testtask.rest.dto;

import java.math.BigDecimal;

public class StatisticsForLastRunningDto {
    private String topic;
    private BigDecimal total;
    private long min;
    private long max;
    private double average;

    public StatisticsForLastRunningDto(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public long getMin() {
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
