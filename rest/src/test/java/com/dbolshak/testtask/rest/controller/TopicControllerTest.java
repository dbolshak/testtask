package com.dbolshak.testtask.rest.controller;

import com.dbolshak.testtask.rest.dto.ExistingTopicsDto;
import com.dbolshak.testtask.rest.dto.LastRunDetailsDto;
import com.dbolshak.testtask.rest.dto.LastRunDto;
import com.dbolshak.testtask.rest.dto.LastRunStatsDto;
import com.dbolshak.testtask.rest.service.TopicService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.dbolshak.testtask.Fixture.*;
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

    //HTTP requests
    private static final String ALL_TOPICS_URL = "/topic";
    private static final String TOPIC_DETAILS_URL = ALL_TOPICS_URL + "/{topic}";
    private static final String TOPIC_LAST_RUN_URL = TOPIC_DETAILS_URL + "/lastRun";
    private static final String TOPIC_LAST_RUN_STATS_URL = TOPIC_LAST_RUN_URL + "/stats";

    @Before
    public void setUp() throws Exception {
        this.mockMvc = standaloneSetup(controller).setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

        when(topicService.topicExists(TOPIC)).thenReturn(true);

        LastRunDto lastRunDto = new LastRunDto(TOPIC);
        lastRunDto.setLastRun(LAST_RUN);
        when(topicService.findLastRunFor(TOPIC)).thenReturn(lastRunDto);

        ExistingTopicsDto existingTopicsDto = new ExistingTopicsDto();
        existingTopicsDto.setExistingTopics(ALL_TOPICS);
        when(topicService.getAllExistingTopics()).thenReturn(existingTopicsDto);

        LastRunStatsDto lastRunStatsDto = new LastRunStatsDto(TOPIC);
        when(topicService.getLastRunStats(TOPIC)).thenReturn(lastRunStatsDto);

        LastRunDetailsDto lastRunDetailsDto = new LastRunDetailsDto(TOPIC);
        lastRunDetailsDto.setMessagesForPartition(TIME_STAMP_CONTENT.getContent());
        when(topicService.getLastRunDetails(TOPIC)).thenReturn(lastRunDetailsDto);
    }

    @Test
    public void testGetExistingTopics() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                get(ALL_TOPICS_URL).accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Assert.assertEquals("{\"existingTopics\":[\"topic-1\",\"topic-2\"]}", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGetLastRun() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                get(TOPIC_LAST_RUN_URL, TOPIC).accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Assert.assertEquals("{\"topic\":\"topic-1\",\"lastRun\":\"1984-12-19-00-00-01\"}", resultActions.andReturn().getResponse().getContentAsString());

    }

    @Test
    public void testGetLastRunStats() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                get(TOPIC_LAST_RUN_STATS_URL, TOPIC).accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Assert.assertEquals("{\"topic\":\"topic-1\",\"total\":null,\"min\":0,\"max\":0,\"average\":0.0}", resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    public void testGetLastRunDetails() throws Exception {
        ResultActions resultActions = this.mockMvc.perform(
                get(TOPIC_DETAILS_URL, TOPIC).accept(APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());

        Assert.assertEquals("{\"topic\":\"topic-1\",\"messagesForPartition\":{\"1\":1,\"2\":3}}", resultActions.andReturn().getResponse().getContentAsString());
    }
}