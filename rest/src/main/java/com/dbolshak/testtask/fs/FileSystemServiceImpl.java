package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static com.dbolshak.testtask.utils.Helper.FILE_SEPARATOR;

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
    public String getAbsoluteFileName(TimeStamp timeStamp) {
        return Helper.getFileName(baseDirProvider.getBaseDir(), timeStamp);
    }

    @Override
    public File[] getAllTopics() {
        File root = new File(baseDirProvider.getBaseDir());
        File[] topics = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Path pattern = Paths.get(dir.getAbsolutePath() + FILE_SEPARATOR + name + FILE_SEPARATOR + Helper.HISTORY_SUB_FOLDER);
                return Files.exists(pattern) || Files.isDirectory(pattern);
            }
        });
        return  topics;
    }
}
