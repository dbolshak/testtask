package com.dbolshak.testtask.rest.controller;

import com.dbolshak.testtask.rest.exceptions.InvalidConfigurationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by dbolshak on 03.09.2014.
 */
@Controller
public class TopicController implements CommandLineRunner {
    DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
        @Override
        public boolean accept(Path file) throws IOException {
            return (Files.isDirectory(file));
        }
    };
    private Path baseDir;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        String result = "";
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir, filter)) {
            for (Path path : stream) {
                result += path.getFileName();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void run(String... strings) throws Exception {
        if (strings.length != 1) {
            throw new InvalidConfigurationException("You must specify at least one (and exactly one) parameter which points to base_dir");
        }
        Path path = Paths.get(strings[0]);
        if (!Files.exists(path) || !Files.isDirectory(path)) {
            throw new InvalidConfigurationException("base_dir does not exist or it's not a directory.");
        }
        baseDir = path;
    }
}
