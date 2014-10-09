package com.dbolshak.testtask.model;

/**
 * Presents data of one line in csv file.
 */
public class PartitionInfo {
    private static final int PARTITION_INDEX = 0;
    private static final int MESSAGE_COUNT_INDEX = 1;

    private final Integer partition;
    private final Long messageCount;

    public PartitionInfo(String[] csvLine) throws IndexOutOfBoundsException, NumberFormatException{
        this.partition = Integer.valueOf(csvLine[PARTITION_INDEX]);
        this.messageCount = Long.valueOf(csvLine[MESSAGE_COUNT_INDEX]);
    }

    public Integer getPartition() {
        return partition;
    }

    public Long getMessageCount() {
        return messageCount;
    }
}
