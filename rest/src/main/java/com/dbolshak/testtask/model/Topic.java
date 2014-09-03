package com.dbolshak.testtask.model;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by dbolshak on 03.09.2014.
 */
public class Topic {
    private static final String DATE_FORMAT = "YYYY-MM-DD-HH-mm-ss";

    private String name;
    private SortedMap<AtomicLong, Path> timeStampToLogMap = new ConcurrentSkipListMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String  getLastRunningTime() {
        if (timeStampToLogMap.isEmpty()) {
            return "";
        }
        SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
        return dt.format(new Date(timeStampToLogMap.lastKey().get()));
    }
}
