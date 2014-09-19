package com.dbolshak.testtask;

import com.dbolshak.testtask.annotation.PostSetDir;
import com.dbolshak.testtask.rest.exceptions.ApplicationRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class ApplicationRunner implements CommandLineRunner {
    @Autowired
    private ApplicationContext context;
    @Autowired
    private BaseDirProvider baseDirProvider;

    @Override
    public void run(String... strings) throws Exception {
        if (strings.length != 1) {
            throw new ApplicationRuntimeException("You must specify at least one (and exactly one) parameter which points to base_dir");
        }
        Path path = Paths.get(strings[0]);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new ApplicationRuntimeException("base_dir does not exist or it's not a directory.");
        }

        baseDirProvider.setBaseDir(path.toAbsolutePath().toString());
        runPostSetDirMethods();
    }

    private void runPostSetDirMethods() throws Exception {
        for (String beanName : context.getBeanDefinitionNames()) {
            Object bean = context.getBean(beanName);
            for (Method method : bean.getClass().getMethods()) {
                if (method.getAnnotation(PostSetDir.class) != null) {
                    method.invoke(bean);
                }
            }
        }
    }
}
