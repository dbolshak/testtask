package com.dbolshak.testtask.utils;

import java.util.regex.Pattern;

/**
 * Created by dbolshak on 04.09.2014.
 */
final public class Constants {
    public static final String DATE_FORMAT = "YYYY-MM-DD-HH-mm-ss";
    public static final String HISTORY_SUBFOLDER = "history";

    /// depends on #{Constants.DATE_FORMAT}
    public static final String REG_EXP_DATE_PATTERN_STR = "([1-2][0-9]{3})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])-(0?[0-9]|[12][0-3])-([0-5][0-9])-([0-5][0-9])";
    public static final Pattern TIME_STAMP_PATTERN  = Pattern.compile(REG_EXP_DATE_PATTERN_STR);
    public static final String OFFSETS_FILE_NAME = "offsets.csv";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String PATH_SPLITTER = Pattern.quote(FILE_SEPARATOR);

    private Constants() {
    }
}
