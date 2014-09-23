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

import static com.dbolshak.testtask.utils.Helper.FILE_SEPARATOR;

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
        File root = new File(baseDirProvider.getBaseDir());
        return root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Path pattern = Paths.get(dir.getAbsolutePath() + FILE_SEPARATOR + name + FILE_SEPARATOR + Helper.HISTORY_SUB_FOLDER);
                return Files.exists(pattern) || Files.isDirectory(pattern);
            }
        });
    }
}
