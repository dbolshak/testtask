package com.dbolshak.testtask.rest.controller;

import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunningDto;
import com.dbolshak.testtask.rest.dto.StatisticsForLastRunningDto;
import com.dbolshak.testtask.rest.service.TopicService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
public class TopicControllerTest {
    @InjectMocks
    private TopicController controller;
    @Mock
    private TopicService topicService;
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = standaloneSetup(controller).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
    }

    @Test
    public void testGetExistingTopics() throws Exception {
        ExistingTopicsDto existingTopicsDto = new ExistingTopicsDto();
        existingTopicsDto.setExistingTopics(Arrays.asList("topic-1", "topic-2"));

        when(topicService.getAllExistingTopics()).thenReturn(existingTopicsDto);

        ResultActions resultActions = this.mockMvc.perform(
                get("/topic/").accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Assert.assertEquals("{\"existingTopics\":[\"topic-1\",\"topic-2\"]}", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGetLastRunningByTopic() throws Exception {
        String topic1 = "topic-1";
        LastRunningDto lastRunningDto = new LastRunningDto(topic1);
        lastRunningDto.setLastRunning("2000-12-12");

        when(topicService.findLastRunningFor(topic1)).thenReturn(lastRunningDto);
        when(topicService.topicExists(topic1)).thenReturn(true);

        ResultActions resultActions = this.mockMvc.perform(
                get("/topic/{topic}/lastRun", topic1).accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Assert.assertEquals("{\"topic\":\"topic-1\",\"lastRunning\":\"2000-12-12\"}", resultActions.andReturn().getResponse().getContentAsString());

    }

    @Test
    public void testGetStaticsForLastRunningByTopic() throws Exception {
        String topic1 = "topic-1";
        StatisticsForLastRunningDto statisticsForLastRunningDto = new StatisticsForLastRunningDto(topic1);

        when(topicService.getStaticsForLastRunningByTopic(topic1)).thenReturn(statisticsForLastRunningDto);
        when(topicService.topicExists(topic1)).thenReturn(true);

        ResultActions resultActions = this.mockMvc.perform(
                get("/topic/{topic}/lastRun/stats", topic1).accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Assert.assertEquals("{\"topic\":\"topic-1\",\"total\":null,\"min\":0,\"max\":0,\"average\":0.0}", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGetLastRunningDetailsByTopic() throws Exception {
        String topic1 = "topic-1";
        LastRunningDetailsDto lastRunningDetailsDto = new LastRunningDetailsDto(topic1);
        Map<Integer, Long> map = new HashMap<>();
        map.put(1, 2l);
        map.put(2, 4l);
        lastRunningDetailsDto.setMessagesForPartition(map);
        when(topicService.getLastRunningDetailsByTopic(topic1)).thenReturn(lastRunningDetailsDto);
        when(topicService.topicExists(topic1)).thenReturn(true);

        ResultActions resultActions = this.mockMvc.perform(
                get("/topic/{topic}", topic1).accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Assert.assertEquals("{\"topic\":\"topic-1\",\"messagesForPartition\":{\"1\":2,\"2\":4}}", resultActions.andReturn().getResponse().getContentAsString());
    }
}