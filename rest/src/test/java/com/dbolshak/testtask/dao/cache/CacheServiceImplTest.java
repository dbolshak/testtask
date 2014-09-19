package com.dbolshak.testtask.dao.cache;

import com.dbolshak.testtask.dao.Computable;
import com.dbolshak.testtask.dao.TimeStampContent;
import com.dbolshak.testtask.fs.FileSystemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceImplTest {
    @InjectMocks
    private CacheService service = new CacheServiceImpl();
    @Mock
    private Computable fileReader;
    @Mock
    private FileSystemService fileSystemService;

    @Test
    public void testRemove() throws Exception {
        String timeStamp = "some csv file";
        String topic = "topic";
        String fullPath = topic + timeStamp;

        when(fileSystemService.getAbsoluteFileName(topic, timeStamp)).thenReturn(fullPath);

        TimeStampContent timeStampContent = new TimeStampContent();
        timeStampContent.put(1, 1l);
        timeStampContent.put(2, 3l);
        when(fileReader.compute(fullPath)).thenReturn(timeStampContent);

        assertEquals(timeStampContent, service.get(topic, timeStamp));
        verify(fileReader).compute(fullPath);

        assertEquals(timeStampContent, service.get(topic, timeStamp));
        verifyZeroInteractions(fileReader);

        when(fileSystemService.getAbsoluteFileName(topic, timeStamp)).thenReturn(fullPath);
        service.remove(topic, timeStamp);
        service.get(topic, timeStamp);
        verify(fileReader, atLeast(2)).compute(fullPath);//init cache again
    }

    @Test
    public void testGet() throws Exception {
        String fileName = "some csv file";
        TimeStampContent timeStampContent = new TimeStampContent();
        timeStampContent.put(1, 1l);
        timeStampContent.put(2, 3l);

        when(fileReader.compute(fileName)).thenReturn(timeStampContent);
        assertEquals(timeStampContent, service.get(fileName));
        verify(fileReader).compute(fileName);

        assertEquals(timeStampContent, service.get(fileName));
        verifyZeroInteractions(fileReader);
    }

    @Test
    public void testGet1() throws Exception {
        String timeStamp = "some csv file";
        String topic = "topic";
        String fullPath = topic + timeStamp;

        when(fileSystemService.getAbsoluteFileName(topic, timeStamp)).thenReturn(fullPath);

        TimeStampContent timeStampContent = new TimeStampContent();
        timeStampContent.put(1, 1l);
        timeStampContent.put(2, 3l);
        when(fileReader.compute(fullPath)).thenReturn(timeStampContent);

        assertEquals(timeStampContent, service.get(topic, timeStamp));
        verify(fileReader).compute(fullPath);
    }
}