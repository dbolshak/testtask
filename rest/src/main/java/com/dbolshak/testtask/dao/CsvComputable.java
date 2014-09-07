package com.dbolshak.testtask.dao;

import au.com.bytecode.opencsv.CSVReader;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dbolshak on 05.09.2014.
 */
@Component("fileReader")
public class CsvComputable implements Computable {
    private final static Logger LOG = LoggerFactory.getLogger(CsvComputable.class);
    @Override
    public TimeStampContent compute(String file) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            TimeStampContent timeStampContent = new TimeStampContent();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    timeStampContent.put(Integer.valueOf(nextLine[0]), Long.valueOf(nextLine[1]));
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    LOG.warn("Cannot handle " + nextLine + "from file " + file + " because of exception", e);
                }
            }
            return timeStampContent;
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Cannot handle specified file " + file, e);
        }
    }
}
