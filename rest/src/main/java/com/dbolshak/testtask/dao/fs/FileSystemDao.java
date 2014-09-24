package com.dbolshak.testtask.dao.fs;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.dao.TopicDao;
import com.dbolshak.testtask.engine.Computable;
import com.dbolshak.testtask.fs.FileSystemService;
import com.dbolshak.testtask.model.TimeStamp;
import com.dbolshak.testtask.model.TimeStampContent;
import com.dbolshak.testtask.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;

import static com.dbolshak.testtask.utils.Helper.FILE_SEPARATOR;

@Service
class FileSystemDao implements TopicDao {
    @Autowired
    private Computable fileReader;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private BaseDirProvider baseDirProvider;

    @Override
    public Collection<String> findAllTopics() {
        File[] topicFiles = fileSystemService.findAllTopics();
        Collection<String> topics = new ArrayList<>(topicFiles.length);
        for (File topic : topicFiles) {
            topics.add(topic.getName());
        }
        return topics;
    }

    @Override
    public String findLastRun(String topic) {
        File history = new File(getFullTopicPath(topic) + FILE_SEPARATOR + Helper.HISTORY_SUB_FOLDER);
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
    public TimeStampContent findTimeStampContent(TimeStamp timeStamp) {
        return fileReader.compute(timeStamp);
    }

    @Override
    public boolean topicExists(String topic) {
        return new File(getFullTopicPath(topic)).exists();
    }

    private String getFullTopicPath(String topic) {
        return baseDirProvider.getBaseDir() + FILE_SEPARATOR + topic;
    }
}
