package com.dbolshak.testtask;

/**
 * Presents immutable structure to identify timestamp
 */
public class TimeStamp {
    private final String topic;
    private final String run;

    public TimeStamp(String topic, String run) {
        this.topic = topic;
        this.run = run;
    }

    public String getTopic() {
        return topic;
    }

    public String getRun() {
        return run;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeStamp timeStamp = (TimeStamp) o;

        if (!run.equals(timeStamp.run)) return false;
        if (!topic.equals(timeStamp.topic)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = topic.hashCode();
        result = 31 * result + run.hashCode();
        return result;
    }
}
