package com.dbolshak.testtask.rest.controller;

import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDto;
import com.dbolshak.testtask.rest.dto.StatisticsForLastRunningDto;
import com.dbolshak.testtask.rest.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ExecutionException;

/**
 * Created by dbolshak on 03.09.2014.
 */
@Controller
public class TopicController {
    @Autowired
    TopicService topicService;

    @RequestMapping(value = "/topics", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ExistingTopicsDto> getExistingTopics() {
        return new ResponseEntity(topicService.getAllExistingTopics(), HttpStatus.OK);
    }

    @RequestMapping(value = "/lastRunning/{topic}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<LastRunningDto> getLastRunningByTopic(@PathVariable String topic) {
        if (!topicService.topicExists(topic)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(topicService.findLastRunningFor(topic), HttpStatus.OK);
    }

    @RequestMapping(value = "/statisticsForLastRunning/{topic}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<StatisticsForLastRunningDto> getStaticsForLastRunningByTopic(@PathVariable String topic) throws ExecutionException, InterruptedException {
        if (!topicService.topicExists(topic)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(topicService.getStaticsForLastRunningByTopic(topic), HttpStatus.OK);
    }

    @RequestMapping(value = "/detailsForLastRunning/{topic}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<LastRunningDetailsDto> getLastRunningDetailsByTopic(@PathVariable String topic) throws ExecutionException, InterruptedException {
        if (!topicService.topicExists(topic)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(topicService.getLastRunningDetailsByTopic(topic), HttpStatus.OK);
    }
}
