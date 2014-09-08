package com.dbolshak.testtask.dao;

import au.com.bytecode.opencsv.CSVReader;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;

@Component("fileReader")
public class CsvComputable implements Computable {
    private static final Logger LOG = LoggerFactory.getLogger(CsvComputable.class);

    @Override
    public TimeStampInfo compute(String file) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            TimeStampInfo timeStampInfo = new TimeStampInfo();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    timeStampInfo.put(Integer.valueOf(nextLine[0]), Long.valueOf(nextLine[1]));
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    LOG.warn("some problem with " + nextLine.toString() + " in file " + file, e);
                }
            }
            return timeStampInfo;
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Exception occurred in CsvComputable while handling " + file, e);
        }
    }
}
