package com.dbolshak.testtask.rest.controller;

import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunDto;
import com.dbolshak.testtask.rest.dto.LastRunStatsDto;
import com.dbolshak.testtask.rest.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for our RESTFull service
 */
@Controller
@RequestMapping("/topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @RequestMapping
    @ResponseBody
    public ResponseEntity<ExistingTopicsDto> getExistingTopics() {
        return new ResponseEntity(topicService.getAllExistingTopics(), HttpStatus.OK);
    }

    @RequestMapping(value = "/{topic}/lastRun", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<LastRunDto> getLastRun(@PathVariable String topic) {
        if (topicService.topicExists(topic)) {
            return new ResponseEntity(topicService.findLastRunFor(topic), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{topic}/lastRun/stats", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<LastRunStatsDto> getLastRunStats(@PathVariable String topic) {
        if (topicService.topicExists(topic)) {
            return new ResponseEntity(topicService.getLastRunStats(topic), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{topic}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<LastRunDetailsDto> getLastRunDetails(@PathVariable String topic) {
        if (topicService.topicExists(topic)) {
            return new ResponseEntity(topicService.getLastRunDetails(topic), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
