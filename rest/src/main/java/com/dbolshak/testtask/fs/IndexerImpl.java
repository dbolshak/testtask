package com.dbolshak.testtask.fs;

import com.dbolshak.testtask.dao.TopicChangingNotifier;
import com.dbolshak.testtask.dao.cache.CacheService;
import com.dbolshak.testtask.utils.Helper;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by dbolshak on 04.09.2014.
 */
@Service("indexer")
public class IndexerImpl implements Indexer{
    private Map<String, TreeMap<String, String>> runningsForTopic = new ConcurrentHashMap<>();
    private boolean registered = false;
    @Autowired
    private TopicChangingNotifier topicChangingNotifier;
    @Autowired
    private CacheService cacheService;

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
    public void setBaseDir(final String baseDir) {
        runningsForTopic.clear();
        File root = new File(baseDir);
        File[] topics = root.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Path pattern = Paths.get(dir.getAbsolutePath() + Helper.FILE_SEPARATOR + name + Helper.FILE_SEPARATOR + Helper.HISTORY_SUBFOLDER);
                return Files.exists(pattern) || Files.isDirectory(pattern);
            }
        });
        for (File topic: topics) {
            final String topicStr = topic.getName();
            File history = new File(topic.getAbsolutePath() + Helper.FILE_SEPARATOR + Helper.HISTORY_SUBFOLDER);
            history.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String timeStamp) {
                    Path pattern = Paths.get(dir.getAbsolutePath() +
                            Helper.FILE_SEPARATOR + timeStamp + Helper.FILE_SEPARATOR + Helper.HISTORY_SUBFOLDER + Helper.FILE_SEPARATOR + Helper.OFFSETS_FILE_NAME);
                    try {
                        if (Files.exists(pattern) || Files.isRegularFile(pattern) || Helper.TIME_STAMP_PATTERN.matcher(timeStamp).groupCount() == 6) {
                            addFileToIndex(pattern.toAbsolutePath().toString(), topicStr, timeStamp);
                            cacheService.get(Helper.getFileName(baseDir, topicStr, timeStamp)).getContent();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
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
        if (!paths[paths.length - 1].equals(Helper.OFFSETS_FILE_NAME)) {
            return false;
        }
        if (!paths[paths.length - 3].equals(Helper.HISTORY_SUBFOLDER)) {
            return false;
        }
        return Helper.TIME_STAMP_PATTERN.matcher(paths[paths.length - 2]).groupCount() == 6;
    }
}
