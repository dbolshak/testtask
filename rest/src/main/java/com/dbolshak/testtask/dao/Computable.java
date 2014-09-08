package com.dbolshak.testtask.dao;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by dbolshak on 05.09.2014.
 */
public interface Computable {
    TimeStampInfo compute(String file);
}
