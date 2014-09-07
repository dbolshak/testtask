package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.utils.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FilenameFilter;

@Component
public class TimeStampListener extends AbstractTopicChangingListener {
    @Autowired private TopicDao topicDao;
    @Autowired private BaseDirProvider baseDirProvider;

    @Override
    public void onTimeStampAdded(String topic, String timeStamp) {
        topicDao.addTimeStamp(timeStamp, topic);
    }

    @Override
    public void onTimeStampDeleted(String topic, String timeStamp) {
        topicDao.removeTimeStamp(timeStamp, topic);
        //just in case if someone deleted the latest timestamp we need to refresh it
        File history = new File(baseDirProvider.getBaseDir() + Helper.FILE_SEPARATOR + topic + Helper.FILE_SEPARATOR + Helper.HISTORY_SUBFOLDER);
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
        topicDao.addTimeStamp(latestRunning[0], topic);
    }
}
