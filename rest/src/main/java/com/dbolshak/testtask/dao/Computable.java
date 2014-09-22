package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.TimeStamp;

/**
 * A basic interface which can provide content from specified file.
 */
public interface Computable {
    /**
     * Perform some action on provided timeStamp
     *
     * @param timeStamp which we need to handle
     * @return content of timeStamp.
     * FIXME: correct javadoc
     */
    TimeStampContent compute(final TimeStamp timeStamp);
}
