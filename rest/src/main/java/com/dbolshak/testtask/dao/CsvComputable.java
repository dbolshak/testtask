package com.dbolshak.testtask.dao;

import au.com.bytecode.opencsv.CSVReader;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Component("fileReader")
public class CsvComputable implements Computable {
    private static final Logger LOG = LoggerFactory.getLogger(CsvComputable.class);

    @Override
    public TimeStampContent compute(String file) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            TimeStampContent timeStampContent = new TimeStampContent();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    timeStampContent.put(Integer.valueOf(nextLine[0]), Long.valueOf(nextLine[1]));
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    LOG.warn("some problem with " + Arrays.toString(nextLine) + " in file " + file, e);
                }
            }
            return timeStampContent;
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Exception occurred in CsvComputable while handling " + file, e);
        }
    }
}
