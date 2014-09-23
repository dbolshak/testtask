package com.dbolshak.testtask.engine;

import com.dbolshak.testtask.model.TimeStamp;
import com.dbolshak.testtask.model.TimeStampContent;

/**
 * A basic interface which can provide content from specified file.
 */
public interface Computable {
    /**
     * Perform some action on provided timeStamp
     *
     * @param timeStamp which we need to handle
     * @return content of timeStamp.
     */
    TimeStampContent compute(final TimeStamp timeStamp);
}
