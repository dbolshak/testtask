package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.annotation.PostSetDir;
import com.dbolshak.testtask.utils.Helper;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TopicChangingNotifierImpl implements TopicChangingNotifier, FileListener {
    private static final Logger LOG = LoggerFactory.getLogger(TopicChangingNotifierImpl.class);
    private final Collection<TopicChangingListener> listeners = new CopyOnWriteArrayList<>();
    @Autowired
    private BaseDirProvider baseDirProvider;

    @Override
    @PostSetDir
    public void init() {
        new Thread(new Runnable() {//VFS works slow if need to handle a lot of files
            @Override
            public void run() {
                try {
                    LOG.info("Setting VFS listener started");
                    FileSystemManager fsManager = VFS.getManager();
                    FileObject listenDir = fsManager.resolveFile(baseDirProvider.getBaseDir());
                    DefaultFileMonitor fm = new DefaultFileMonitor(TopicChangingNotifierImpl.this);
                    fm.setRecursive(true);
                    fm.addFile(listenDir);
                    fm.start();
                    LOG.info("Setting VFS listener finished");
                } catch (FileSystemException e) {
                    LOG.warn("file system listeners won't get any events, because VFS service did not started properly", e);
                }
            }
        }).start();
    }

    @Override
    public void addListener(TopicChangingListener topicChangingListener) {
        listeners.add(topicChangingListener);
    }

    @Override
    public void removeListener(TopicChangingListener topicChangingListener) {
        listeners.remove(topicChangingListener);
    }

    @Override
    public void fileCreated(FileChangeEvent fileChangeEvent) {
        handleFileSystemEvent(fileChangeEvent, new FileActionHandlerCallback() {
            @Override
            public void fileActionHandle(TopicChangingListener listener, TimeStamp timeStamp) {
                listener.onTimeStampAdded(timeStamp);
            }
        });
    }

    @Override
    public void fileDeleted(FileChangeEvent fileChangeEvent) {
        handleFileSystemEvent(fileChangeEvent, new FileActionHandlerCallback() {
            @Override
            public void fileActionHandle(TopicChangingListener listener, TimeStamp timeStamp) {
                listener.onTimeStampDeleted(timeStamp);
            }
        });
    }

    @Override
    public void fileChanged(FileChangeEvent fileChangeEvent) {
        handleFileSystemEvent(fileChangeEvent, new FileActionHandlerCallback() {
            @Override
            public void fileActionHandle(TopicChangingListener listener, TimeStamp timeStamp) {
                listener.onTimeStampModified(timeStamp);
            }
        });
    }

    private TimeStamp createTimeStampFromFileChangeEvent(FileChangeEvent fileChangeEvent) {
        if (Helper.validateFileName(fileChangeEvent.getFile().toString())) {
            String[] paths = fileChangeEvent.getFile().toString().replace("\\", "/").split("/");
            return new TimeStamp(paths[paths.length - 4], paths[paths.length - 2]);
        }
        return null;
    }

    private void handleFileSystemEvent(FileChangeEvent fileChangeEvent, FileActionHandlerCallback fileActionHandlerCallback) {
        TimeStamp timeStamp  = createTimeStampFromFileChangeEvent(fileChangeEvent);
        if (timeStamp != null) {
            for (TopicChangingListener listener : listeners) {
                fileActionHandlerCallback.fileActionHandle(listener, timeStamp);
            }
        }
    }

    private interface FileActionHandlerCallback {
        void fileActionHandle(TopicChangingListener listener, TimeStamp timeStamp);
    }
}
