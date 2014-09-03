package com.dbolshak.testtask.rest.service;

import com.dbolshak.testtask.model.dto.ExistingTopicsDto;
import com.dbolshak.testtask.model.dto.LastRunningDetailsDto;
import com.dbolshak.testtask.model.dto.LastRunningDto;
import com.dbolshak.testtask.model.dto.StatisticsForLastRunningDto;
import com.dbolshak.testtask.rest.exceptions.InvalidConfigurationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dbolshak on 03.09.2014.
 */
@Service("topicService")
public class TopicServiceImpl implements TopicService, CommandLineRunner {
    private Path baseDir; //it will be inited during startup.

    public ExistingTopicsDto getAllExistingTopics() {
        ExistingTopicsDto result = new ExistingTopicsDto();
        ArrayList<String> topics = new ArrayList<>();
        result.setExistingTopics(topics);
        return result;
    }

    @Override
    public LastRunningDto findLastRunningFor(String topic) {
        LastRunningDto lastRunningDto = new LastRunningDto();
        lastRunningDto.setLastRunning("1-1-1-1");
        lastRunningDto.setTopic(topic);
        return lastRunningDto;
    }

    @Override
    public StatisticsForLastRunningDto getStaticsForLastRunningByTopic(String topic) {
        StatisticsForLastRunningDto statisticsForLastRunningDto = new StatisticsForLastRunningDto();
        statisticsForLastRunningDto.setTopic(topic);
        statisticsForLastRunningDto.setTotal(20);
        statisticsForLastRunningDto.setMin(1);
        statisticsForLastRunningDto.setMax(10);
        statisticsForLastRunningDto.setAverage(2);
        return statisticsForLastRunningDto;
    }

    @Override
    public LastRunningDetailsDto getLastRunningDetailsByTopic(String topic) {
        LastRunningDetailsDto detailsForLastRunning = new LastRunningDetailsDto();
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 2);
        map.put(3, 4);
        detailsForLastRunning.setMessagesForPartition(map);
        detailsForLastRunning.setTopic(topic);
        return detailsForLastRunning;
    }

    @Override
    public boolean topicExists(String topic) {
        //FIXME
        return true;
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
