package edu.iastate.cs309.jr2.CatchTheCacheServer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.iastate.cs309.jr2.CatchTheCacheServer.chat.*;

public class TestChatService {
	@InjectMocks
	ChatService chatService;
	@Mock
	ChatRepository chatRepo;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getChatByIdTest() {
		Chat chat =new Chat(1, "first chat", 0);
		Optional<Chat> result= Optional.of(chat);
		when(chatRepo.findById(1)).thenReturn(result);

		Chat testChat= chatService.getChatById(1);
		
		assertEquals(Integer.valueOf(1), testChat.getId());
		assertEquals("first chat", testChat.getUser());
		assertEquals(Integer.valueOf(0),testChat.getCacheId());
		
	}
	
	@Test
	public void testGetAllChats() { 
		Chat chat1 =new Chat(1, "first chat", 0);
		Chat chat2 =new Chat(2, "not a second chat", 333);
		Chat chat3 =new Chat(3, "number 1+1+1 chat", 40);
		List<Chat> chatList=new ArrayList<Chat>();
		chatList.add(chat1);
		chatList.add(chat2);
		chatList.add(chat3);
		
		when(chatRepo.findAll()).thenReturn(chatList);
		
		List<Chat> result= chatService.getAllChats();
		
		assertEquals(Integer.valueOf(1), result.get(0).getId());
		assertEquals("first chat", result.get(0).getUser());
		assertEquals(Integer.valueOf(0),result.get(0).getCacheId());
		assertEquals(Integer.valueOf(2), result.get(1).getId());
		assertEquals("not a second chat", result.get(1).getUser());
		assertEquals(Integer.valueOf(333),result.get(1).getCacheId());
		assertEquals(Integer.valueOf(3), result.get(2).getId());
		assertEquals("number 1+1+1 chat", result.get(2).getUser());
		assertEquals(Integer.valueOf(40),result.get(2).getCacheId());
		
	}

	@Test
	public void testGetAllChats2() {
		List<Chat> chatList=new ArrayList<Chat>();
		chatList.add(new Chat());
		chatList.add(new Chat());
		chatList.add(new Chat());
		chatList.add(new Chat());
		chatList.add(new Chat());
		
		when(chatRepo.findAll()).thenReturn(chatList);
		
		List<Chat> result= chatService.getAllChats();
		
		assertEquals(5, result.size());
	}
}
