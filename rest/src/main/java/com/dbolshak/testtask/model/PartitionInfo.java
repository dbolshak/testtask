package com.dbolshak.testtask.model;

/**
 * Created by dbolshak on 09.10.2014.
 */
public class PartitionInfo {
    private static int PARTITION_INDEX = 0;
    private static int MESSAGE_COUNT_INDEX_INDEX = 1;

    private final Integer partition;
    private final Long messageCount;

    public PartitionInfo(String[] csvLine) throws IndexOutOfBoundsException, NumberFormatException{
        this.partition = Integer.valueOf(csvLine[PARTITION_INDEX]);
        this.messageCount = Long.valueOf(csvLine[MESSAGE_COUNT_INDEX_INDEX]);
    }

    public Integer getPartition() {
        return partition;
    }

    public Long getMessageCount() {
        return messageCount;
    }
}
