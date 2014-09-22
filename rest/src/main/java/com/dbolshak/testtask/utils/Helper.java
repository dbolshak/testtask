package com.dbolshak.testtask.utils;

import com.dbolshak.testtask.TimeStamp;

import java.util.regex.Pattern;

/**
 * a helper class
 */
public final class Helper {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String HISTORY_SUB_FOLDER = "history";

    private static final String REG_EXP_DATE_PATTERN_STR = "([1-2][0-9]{3})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])-(0?[0-9]|[12][0-3])-([0-5][0-9])-([0-5][0-9])";
    private static final Pattern TIME_STAMP_PATTERN = Pattern.compile(REG_EXP_DATE_PATTERN_STR);
    private static final String OFFSETS_FILE_NAME = "offsets.csv";

    private static final int MIN_SEGMENT_COUNT = 4;
    private static final int GROUP_COUNT_IN_REG_EXP = 6;
    private static final int OFFSET_FILE_SEGMENT_INDEX = 1;
    private static final int TIMESTAMP_SEGMENT_INDEX = 2;
    private static final int HISTORY_SUB_FOLDER_SEGMENT_INDEX = 3;
    private static final int TOPIC_SEGMENT_INDEX = 4;


    private Helper() {
    }

    public static String getFileName(String baseDir, TimeStamp timeStamp) {
        return baseDir + FILE_SEPARATOR + timeStamp.getTopic() + FILE_SEPARATOR + HISTORY_SUB_FOLDER + FILE_SEPARATOR + timeStamp.getRun() + FILE_SEPARATOR + OFFSETS_FILE_NAME;
    }

    public static boolean validateFileName(String fileName) {
        String[] paths = splitFilePath(fileName);
        if (paths.length < MIN_SEGMENT_COUNT) {
            return false;
        }
        if (!paths[paths.length - OFFSET_FILE_SEGMENT_INDEX].equals(Helper.OFFSETS_FILE_NAME)) {
            return false;
        }
        return paths[paths.length - HISTORY_SUB_FOLDER_SEGMENT_INDEX].equals(Helper.HISTORY_SUB_FOLDER) && Helper.TIME_STAMP_PATTERN.matcher(paths[paths.length - TIMESTAMP_SEGMENT_INDEX]).groupCount() == GROUP_COUNT_IN_REG_EXP;
    }

    public static TimeStamp createTimeStampFromFile(String filePath) {
        if (Helper.validateFileName(filePath)) {
            String[] paths = splitFilePath(filePath);
            return new TimeStamp(paths[paths.length - TOPIC_SEGMENT_INDEX], paths[paths.length - TIMESTAMP_SEGMENT_INDEX]);
        }
        return null;
    }

    private static String[] splitFilePath(String fileName) {
        return fileName.replace("\\", "/").split("/");
    }
}
