package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.utils.Constants;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by dbolshak on 04.09.2014.
 */
@Service("indexer")
public class IndexerImpl implements Indexer{
    private Map<String, TreeMap<String, String>> runningsForTopic = new ConcurrentHashMap<>();
    private boolean registered = false;

    @Override
    public String getLastRunning(String topic) throws ParseException {
        TreeMap<String, String> running  = runningsForTopic.get(topic);
        if (running == null || running.isEmpty()) {
            return "";
        }
        return running.lastEntry().getValue();
    }

    @Override
    public void addFileToIndex(String path, String topic, String timeStamp) throws ParseException {
        TreeMap<String, String> lastRunning  = findOrCreate(topic);
        lastRunning.put(timeStamp, path);
    }

    @Override
    public void removeFileFromIndex(String path, String topic) {
        TreeMap<String, String> running  = runningsForTopic.get(topic);
        if (running == null || running.isEmpty()) {
            return;
        }
        //TODO fix me implement
    }

    @Override
    public void setBaseDir(String baseDir) {
        runningsForTopic.clear();
        File root = new File(baseDir);
        File[] topics = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Path pattern = Paths.get(dir.getAbsolutePath() + Constants.FILE_SEPARATOR + name + Constants.FILE_SEPARATOR + Constants.HISTORY_SUBFOLDER);
                return Files.exists(pattern) || Files.isDirectory(pattern);
            }
        });
        for (File topic: topics) {
            final String topicStr = topic.getName();
            File history = new File(topic.getAbsolutePath() + Constants.FILE_SEPARATOR + Constants.HISTORY_SUBFOLDER);
            history.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String timeStamp) {
                    Path pattern = Paths.get(dir.getAbsolutePath() +
                            Constants.FILE_SEPARATOR + timeStamp + Constants.FILE_SEPARATOR + Constants.HISTORY_SUBFOLDER + Constants.FILE_SEPARATOR + Constants.OFFSETS_FILE_NAME);
                    try {
                        if (Files.exists(pattern) || Files.isRegularFile(pattern) || Constants.TIME_STAMP_PATTERN.matcher(timeStamp).groupCount() == 6) {
                            addFileToIndex(pattern.toAbsolutePath().toString(), topicStr, timeStamp);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                }
            });
        }
        if (!registered) {
            registered = true;
            try {
                FileSystemManager fsManager = VFS.getManager();
                FileObject listenDir = fsManager.resolveFile(baseDir);
                DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener() {
                    public void fileCreated(FileChangeEvent event) throws Exception {
                        if (validateFileName(event.getFile().toString())) {
                            String[] paths = event.getFile().toString().replace("\\", "/").split("/");
                            addFileToIndex(event.getFile().toString(), paths[paths.length - 4], paths[paths.length - 2]);
                        }
                    }
                    public void fileDeleted(FileChangeEvent event) throws Exception {
                        if (validateFileName(event.getFile().toString())) {
                            String[] paths = event.getFile().toString().replace("\\", "/").split("/");
                            removeFileFromIndex(event.getFile().toString(), paths[paths.length - 4]);
                        }
                    }
                    public void fileChanged(FileChangeEvent event) throws Exception {
                        //don't care
                    }
                });
                fm.setRecursive(true);
                fm.addFile(listenDir);
                fm.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private TreeMap<String, String> findOrCreate(String topic) {
        TreeMap<String, String> running  = runningsForTopic.get(topic);
        if (running == null) {
            running = new TreeMap<>();
            runningsForTopic.put(topic, running);
        }
        return running;
    }

    private boolean validateFileName(String fileName) {
        String[] paths = fileName.replace("\\", "/").split("/");
        if (paths.length < 4) {
            return false;
        }
        if (!paths[paths.length - 1].equals(Constants.OFFSETS_FILE_NAME)) {
            return false;
        }
        if (!paths[paths.length - 3].equals(Constants.HISTORY_SUBFOLDER)) {
            return false;
        }
        return Constants.TIME_STAMP_PATTERN.matcher(paths[paths.length - 2]).groupCount() == 6;
    }
}
