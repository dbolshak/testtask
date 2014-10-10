package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.model.TimeStamp;
import com.dbolshak.testtask.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service("fileSystemService")
class FileSystemServiceImpl implements FileSystemService {
    @Autowired
    private BaseDirProvider baseDirProvider;

    @Override
    public String getAbsoluteFileName(TimeStamp timeStamp) {
        return Helper.getFileName(baseDirProvider.getBaseDir(), timeStamp);
    }

    @Override
    public File[] findAllTopics() {
        return new File(baseDirProvider.getBaseDir()).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File baseDir, String topic) {
                Path pattern = Paths.get(Helper.getHistorySubFolder(baseDir.getAbsolutePath(), topic));
                return Files.exists(pattern) || Files.isDirectory(pattern);
            }
        });
    }
}
