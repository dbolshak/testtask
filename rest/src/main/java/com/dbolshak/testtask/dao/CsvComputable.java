package com.dbolshak.testtask.dao;

import au.com.bytecode.opencsv.CSVReader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dbolshak on 05.09.2014.
 */
@Component("fileReader")
public class CsvComputable implements Computable {
    @Override
    public TimeStampInfo compute(String file) throws InterruptedException, IOException {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            TimeStampInfo timeStampInfo = new TimeStampInfo();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    timeStampInfo.put(Integer.valueOf(nextLine[0]), Long.valueOf(nextLine[1]));
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            return timeStampInfo;
        }
    }
}
