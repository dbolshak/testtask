package com.dbolshak.testtask;

import com.dbolshak.testtask.model.PartitionInfo;
import com.dbolshak.testtask.model.TimeStamp;
import com.dbolshak.testtask.model.TimeStampContent;

import java.util.Arrays;
import java.util.List;

/**
 * Few common objects for tests
 */
final public class Fixture {
    public static final String TOPIC = "topic-1";
    public static final String LAST_RUN = "1984-12-19-00-00-01";
    public static final String NOT_LAST_RUN = "1984-12-19-00-00-00";
    public static final TimeStamp TIME_STAMP = new TimeStamp(TOPIC, LAST_RUN);
    public static final List<String> ALL_TOPICS = Arrays.asList(TOPIC, "topic-2");
    public static final TimeStampContent TIME_STAMP_CONTENT = new TimeStampContent();

    static {
        TIME_STAMP_CONTENT.put(new PartitionInfo(new String[]{"1", "1"}));
        TIME_STAMP_CONTENT.put(new PartitionInfo(new String[]{"2", "3"}));
    }

    private Fixture() {
    }
}
