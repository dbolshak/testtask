package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;

import static com.dbolshak.testtask.utils.Helper.FILE_SEPARATOR;

@Component
public class FileSystemServiceImpl implements FileSystemService {
    @Autowired
    private BaseDirProvider baseDirProvider;

    @Override
    public String getLatestRunning(String topic) {
        File history = new File(baseDirProvider.getBaseDir() + FILE_SEPARATOR + topic + Helper.HISTORY_SUB_FOLDER);
        final String[] latestRunning = new String[]{""};
        history.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String timeStamp) {
                if (latestRunning[0].compareTo(timeStamp) < 0) {
                    latestRunning[0] = timeStamp;
                }
                return true;
            }
        });
        return latestRunning[0];
    }

    @Override
    public String getAbsoluteFileName(String topic, String timeStamp) {
        return baseDirProvider.getBaseDir() + FILE_SEPARATOR + topic + Helper.HISTORY_SUB_FOLDER + FILE_SEPARATOR + timeStamp + Helper.OFFSETS_FILE_NAME;
    }
}
