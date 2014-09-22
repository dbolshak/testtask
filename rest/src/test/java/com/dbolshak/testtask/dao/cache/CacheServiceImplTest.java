package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.Computable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.dbolshak.testtask.Fixture.TIME_STAMP;
import static com.dbolshak.testtask.Fixture.TIME_STAMP_CONTENT;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceImplTest {
    @InjectMocks
    private CacheService service = new CacheServiceImpl();
    @Mock
    private Computable fileReader;

    @Before
    public void createFixture() {
        when(fileReader.compute(TIME_STAMP)).thenReturn(TIME_STAMP_CONTENT);
    }

    @Test
    public void getCachedValueFromLRU() throws Exception {
        verifyFirstGetAndCacheMissing();

        /*
         * Now cache has required value, so fileReader must not be used to get the value.
         */
        assertEquals(TIME_STAMP_CONTENT, service.get(TIME_STAMP));
        verifyZeroInteractions(fileReader);
    }

    @Test
    public void testRemove() throws Exception {
        verifyFirstGetAndCacheMissing();

        /*
         * We know, that cache has a cached value, so lets clear our cache and try to get value again.
         */
        service.remove(TIME_STAMP);
        service.get(TIME_STAMP);
        verify(fileReader, atLeast(2)).compute(TIME_STAMP);//init cache again
    }

    private void verifyFirstGetAndCacheMissing() {
        assertEquals(TIME_STAMP_CONTENT, service.get(TIME_STAMP));
        verify(fileReader).compute(TIME_STAMP);
    }
}