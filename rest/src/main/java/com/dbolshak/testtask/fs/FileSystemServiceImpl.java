package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.dbolshak.testtask.utils.Helper.FILE_SEPARATOR;

import java.io.File;
import java.io.FilenameFilter;

@Service
public class FileSystemServiceImpl implements FileSystemService {
    @Autowired
    private BaseDirProvider baseDirProvider;

    @Override
    public String getLastRun(String topic) {
        File history = new File(baseDirProvider.getBaseDir() + FILE_SEPARATOR + topic + FILE_SEPARATOR + Helper.HISTORY_SUB_FOLDER);
        final String[] lastRun = new String[]{""};
        history.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String timeStamp) {
                if (lastRun[0].compareTo(timeStamp) < 0) {
                    lastRun[0] = timeStamp;
                }
                return true;
            }
        });
        return lastRun[0];
    }

    @Override
    public String getAbsoluteFileName(String topic, String timeStamp) {
        return Helper.getFileName(baseDirProvider.getBaseDir(), topic, timeStamp);
    }
}
