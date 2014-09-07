package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.BaseDirProvider;
import com.dbolshak.testtask.utils.Helper;
import org.apache.commons.vfs2.FileChangeEvent;
import org.apache.commons.vfs2.FileListener;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TopicChangingNotifierImpl implements TopicChangingNotifier {
    private static final Logger LOG = LoggerFactory.getLogger(TopicChangingNotifierImpl.class);

    private Collection<TopicChangingListener> listeners = new CopyOnWriteArrayList<>();

    @Autowired
    private BaseDirProvider baseDirProvider;

    @Override
    public void init() throws FileSystemException {
        LOG.info("Setting VFS listener started");
        FileSystemManager fsManager = VFS.getManager();
        FileObject listenDir = fsManager.resolveFile(baseDirProvider.getBaseDir());
        DefaultFileMonitor fm = new DefaultFileMonitor(new FileListener() {
            public void fileCreated(FileChangeEvent event) throws Exception {
                if (Helper.validateFileName(event.getFile().toString())) {
                    String[] paths = event.getFile().toString().replace("\\", "/").split("/");
                    for (TopicChangingListener listener : listeners) {
                        listener.onTimeStampAdded(paths[paths.length - 4], paths[paths.length - 2]);
                    }
                }
            }

            public void fileDeleted(FileChangeEvent event) throws Exception {
                if (Helper.validateFileName(event.getFile().toString())) {
                    String[] paths = event.getFile().toString().replace("\\", "/").split("/");
                    for (TopicChangingListener listener : listeners) {
                        listener.onTimeStampDeleted(paths[paths.length - 4], paths[paths.length - 2]);
                    }
                }
            }

            public void fileChanged(FileChangeEvent event) throws Exception {
                if (Helper.validateFileName(event.getFile().toString())) {
                    String[] paths = event.getFile().toString().replace("\\", "/").split("/");
                    for (TopicChangingListener listener : listeners) {
                        listener.onTimeStampModified(paths[paths.length - 4], paths[paths.length - 2]);
                    }
                }
            }
        });
        fm.setRecursive(true);
        fm.addFile(listenDir);
        fm.start();
        LOG.info("Setting VFS listener finished");
    }

    @Override
    public void addListener(TopicChangingListener topicChangingListener) {
        listeners.add(topicChangingListener);
    }

    @Override
    public void deleteListener(TopicChangingListener topicChangingListener) {
        listeners.remove(topicChangingListener);
    }
}
