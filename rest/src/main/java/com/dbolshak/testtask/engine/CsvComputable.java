package com.dbolshak.testtask.engine;

import au.com.bytecode.opencsv.CSVReader;
import com.dbolshak.testtask.exceptions.ApplicationRuntimeException;
import com.dbolshak.testtask.fs.FileSystemService;
import com.dbolshak.testtask.model.PartitionInfo;
import com.dbolshak.testtask.model.TimeStamp;
import com.dbolshak.testtask.model.TimeStampContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

@Component("fileReader")
class CsvComputable implements Computable {
    private static final Logger LOG = LoggerFactory.getLogger(CsvComputable.class);
    @Autowired
    private FileSystemService fileSystemService;

    @Override
    public TimeStampContent compute(final TimeStamp timeStamp) {
        try (CSVReader offsetCsvReader = new CSVReader(new FileReader(fileSystemService.getAbsoluteFileName(timeStamp)))) {
            TimeStampContent timeStampContent = new TimeStampContent();
            String[] csvLine;
            while ((csvLine = offsetCsvReader.readNext()) != null) {
                try {
                    timeStampContent.put(new PartitionInfo(csvLine));
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    LOG.warn("some problem with " + Arrays.toString(csvLine) + " in file " + timeStamp, e);
                }
            }
            return timeStampContent;
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Exception occurred in CsvComputable while handling " + timeStamp, e);
        }
    }
}
