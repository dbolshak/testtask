package com.dbolshak.testtask.utils;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class HelperTest {

    @Test
    public void testValidateFileName() throws Exception {
        assertTrue(Helper.validateFileName("file:///C:/base_dir/11/topic-10/history/2000-01-01-05-46-33/offsets.csv"));
    }
}