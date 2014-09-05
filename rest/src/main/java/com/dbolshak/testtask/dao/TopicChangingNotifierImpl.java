package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.utils.Helper;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.impl.DefaultFileMonitor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TopicChangingNotifierImpl implements TopicChangingNotifier {
    Collection<TopicChangingListener> listeners = new CopyOnWriteArrayList<>();

    @Override
    public void setBaseDir(String baseDir) throws FileSystemException {
        FileSystemManager fsManager = VFS.getManager();
        FileObject listenDir = fsManager.resolveFile(baseDir);
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
