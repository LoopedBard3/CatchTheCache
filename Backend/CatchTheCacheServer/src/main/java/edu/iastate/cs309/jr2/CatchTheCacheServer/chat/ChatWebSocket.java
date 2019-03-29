package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ServerEndpoint("/chats/m/{id}/websocket")
@Component
public class ChatWebSocket {
	
	private static Map<Session, String> sessionIdMap = new HashMap<>();
    private static Map<String, Session> idSessionMap = new HashMap<>();
    
    
    @Autowired
	ChatRepository chatRepo;
	@Autowired
	MessageRepository messageRepo;

    
    private final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);
    
    @OnOpen
    public void onOpen(
    	      Session session, 
    	      @PathParam("Id") String Id) throws IOException 
    {
        logger.info("Entered into Open");
        
        sessionIdMap.put(session, Id);
        idSessionMap.put(Id, session);
        
        String message="User:" + Id + " has Joined the Chat";
        	broadcast(message);
		
    }
 
    @OnMessage
    public void onMessage(Session session, String message) throws IOException 
    {
        // Handle new messages
    	logger.info("Entered into Message: Got Message:"+message);
    	String Id = sessionIdMap.get(session);
    	
    	if (message.startsWith("@")) // Direct message to a user using the format "@Id <message>"
    	{
    		String destId = message.split(" ")[0].substring(1); // don't do this in your code!
    		sendMessageToPArticularUser(destId, "[DM] " + Id + ": " + message);
    		sendMessageToPArticularUser(Id, "[DM] " + Id + ": " + message);
    	}
    	else // Message to whole chat
    	{
	    	broadcast(Id + ": " + message);
    	}
    }
 
    @OnClose
    public void onClose(Session session) throws IOException
    {
    	logger.info("Entered into Close");
    	
    	String Id = sessionIdMap.get(session);
    	sessionIdMap.remove(session);
    	idSessionMap.remove(Id);
        
    	String message= Id + " disconnected";
        broadcast(message);
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) 
    {
        // Do error handling here
    	logger.info("Entered into Error");
    }
    
	private void sendMessageToPArticularUser(String Id, String message) 
    {	
    	try {
    		idSessionMap.get(Id).getBasicRemote().sendText(message);
        } catch (IOException e) {
        	logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }
    
    private static void broadcast(String message) 
    	      throws IOException 
    {	  
    	sessionIdMap.forEach((session, Id) -> {
    		synchronized (session) {
	            try {
	                session.getBasicRemote().sendText(message);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}
}
