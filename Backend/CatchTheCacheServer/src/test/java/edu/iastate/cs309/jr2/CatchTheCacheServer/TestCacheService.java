package edu.iastate.cs309.jr2.CatchTheCacheServer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.iastate.cs309.jr2.CatchTheCacheServer.cache.*;
import edu.iastate.cs309.jr2.CatchTheCacheServer.models.*;

/**
 * @author Aidan Sherburne
 *
 */
public class TestCacheService {

	@Mock
	CacheService cacheService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreate() {// tests the CacheAddRequest/Response
		CacheAddRequest request = new CacheAddRequest("MockitoCache", 42.033831, -93.6387, "aidans",
				"Aidan's apartment. Don't be a stalker!");
		when(cacheService.create(request))
				.thenReturn(new ResponseEntity<CacheAddResponse>(new CacheAddResponse(true, false), HttpStatus.OK));

		assertEquals(true, cacheService.create(request).getBody().getAuthorized());
		assertEquals(false, cacheService.create(request).getBody().getSuccess());
	}

	@Test
	public void testList() {// tests the CacheListRequest/Response
		ArrayList<Cache> mockList = new ArrayList<Cache>();
		Cache cacheOne = new Cache();
		Cache cacheTwo = new Cache();
		Cache cacheThree = new Cache();

		mockList.add(cacheOne);
		mockList.add(cacheTwo);
		mockList.add(cacheThree);

		CacheListResponse response = new CacheListResponse(mockList);

		when(cacheService.list()).thenReturn(new ResponseEntity<CacheListResponse>(response, HttpStatus.OK));

		ArrayList<Cache> cacheList = cacheService.list().getBody().getCacheList();

		assertEquals(3, cacheList.size());
		verify(cacheService, times(1)).list();
	}

	@Test
	public void testGetMesages() {// tests MessageListRequest/Response
		List<Message> mockList = new ArrayList<Message>();
		Message messageOne = new Message();
		Message messageTwo = new Message();
		Message messageThree = new Message();

		mockList.add(messageOne);
		mockList.add(messageTwo);
		mockList.add(messageThree);

		MessageListResponse response = new MessageListResponse(mockList);

		when(cacheService.getMessages(1)).thenReturn(new ResponseEntity<MessageListResponse>(response, HttpStatus.OK));

		List<Message> messageList = cacheService.getMessages(1).getBody().getMessageList();

		assertEquals(3, messageList.size());
		verify(cacheService, times(1)).getMessages(1);
	}

}
