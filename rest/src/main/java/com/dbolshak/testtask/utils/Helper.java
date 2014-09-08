package com.dbolshak.testtask.utils;

import java.util.regex.Pattern;

/**
 * A helper class, which has constants and static methods for our application.
 */
final public class Helper {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String HISTORY_SUB_FOLDER = FILE_SEPARATOR + "history";

    /// depends on #{Constants.DATE_FORMAT}
    private static final String OFFSETS_FILE_NAME = "offsets.csv";
    private static final String REG_EXP_DATE_PATTERN_STR = "([1-2][0-9]{3})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])-(0?[0-9]|[12][0-3])-([0-5][0-9])-([0-5][0-9])";
    private static final Pattern TIME_STAMP_PATTERN = Pattern.compile(REG_EXP_DATE_PATTERN_STR);

    private Helper() {
    }

    public static String getFileName(String baseDir, String topic, String timeStamp) {
        return baseDir + FILE_SEPARATOR + topic + FILE_SEPARATOR + HISTORY_SUB_FOLDER + FILE_SEPARATOR + timeStamp + FILE_SEPARATOR + OFFSETS_FILE_NAME;
    }

    public static boolean validateFileName(String fileName) {
        String[] paths = fileName.replace("\\", "/").split("/");
        if (paths.length < 4) {
            return false;
        }
        if (!paths[paths.length - 1].equals(Helper.OFFSETS_FILE_NAME)) {
            return false;
        }
        if (!paths[paths.length - 3].equals(Helper.HISTORY_SUB_FOLDER)) {
            return false;
        }
        return Helper.TIME_STAMP_PATTERN.matcher(paths[paths.length - 2]).groupCount() == 6;
    }
}
