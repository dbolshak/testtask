package com.dbolshak.testtask.dao;

import com.dbolshak.testtask.TimeStamp;
import com.dbolshak.testtask.dao.cache.CacheService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.dbolshak.testtask.Fixture.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TopicDaoImplTest {
    @InjectMocks
    private TopicDao service = new TopicDaoImpl();
    @Mock
    private CacheService cacheService;

    @Before
    public void createFixture() {
        /*
         * Our DAO has at one topic at startup of any test
         */
        service.addTimeStamp(TIME_STAMP);
    }

    @After
    public void clear() {
        /*
         * Clear environment after all our modifications
         */
        service.clear();
    }

    @Test
    public void testFindAllTopics() throws Exception {
        /*
         * Check our startup condition, we must have there one item, lets go and check it!
         */
        assertEquals(TOPIC, service.findAllTopics().iterator().next());
        checkCountOfTopics(1);
    }

    @Test
    public void testClear() throws Exception {
        /*
         * Lets check that we have something in DAO
         */
        assertEquals(TOPIC, service.findAllTopics().iterator().next());
        assertEquals(1, service.findAllTopics().size());

        /*
         * Lets clear our DAO and check that it's empty now
         */
        service.clear();
        checkCountOfTopics(0);
    }

    @Test
    public void testRemoveTimeStamp() throws Exception {
        /*
         * Make sure that we have LAST_RUN timestamp for topic TOPIC
         */
        assertEquals(LAST_RUN, service.findLastRun(TOPIC));

        /*
         * Lets remove our known timestamp LAST_RUN for topic TOPIC and check that there is no any more this timestamp
         */
        service.removeTimeStamp(TIME_STAMP);
        assertEquals("", service.findLastRun(TOPIC));
    }

    @Test
    public void testAddTimeStamp() throws Exception {
        /*
         * Clear our environment and check that we have no items in DAO
         */
        clear();
        checkCountOfTopics(0);

        /*
         * Lets add some item in DAO and check that we see it there
         */
        service.addTimeStamp(TIME_STAMP);
        assertEquals(TOPIC, service.findAllTopics().iterator().next());
        checkCountOfTopics(1);
    }

    @Test
    public void testFindLastRun() throws Exception {
        /*
         * Lets add some non last time stamp and make sure that even in this case we will return the latest one
         */
        service.addTimeStamp(new TimeStamp(TOPIC, NOT_LAST_RUN));
        assertEquals(LAST_RUN, service.findLastRun(TOPIC));
    }

    @Test
    public void testFindTimeStampContent() throws Exception {
        service.addTimeStamp(new TimeStamp(TOPIC, NOT_LAST_RUN));

        when(cacheService.get(TIME_STAMP)).thenReturn(TIME_STAMP_CONTENT);

        assertEquals(TIME_STAMP_CONTENT, service.findTimeStampContent(new TimeStamp(TOPIC, service.findLastRun(TOPIC))));
    }

    @Test
    public void testTopicExists() throws Exception {
        /*
         * Lets check that we can check exists or not of our topic which was added during startup
         */
        assertEquals(true, service.topicExists(TOPIC));
    }

    // call this method if you want to check count of existing topics in our DAO
    private void checkCountOfTopics(int expected) {
        assertEquals(expected, service.findAllTopics().size());
    }
}
