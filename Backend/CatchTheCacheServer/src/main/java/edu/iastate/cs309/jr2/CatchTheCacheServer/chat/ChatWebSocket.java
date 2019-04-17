package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import java.util.Map;

import javax.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.HashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.iastate.cs309.jr2.CatchTheCacheServer.cache.CustomConfigurator;
/**
 * This class does the operations of initiating and closing chats through websockets.
 * @author Annie (Yung-Hsueh) Lee
 *
 */
@ServerEndpoint(value = "/chats/m/{id}/websocket", configurator = CustomConfigurator.class)
@Component
public class ChatWebSocket {
	private static Map<Session, Integer> sessionIdMap = new HashMap<>();
	private static Map<Integer, Session> idSessionMap = new HashMap<>();
    
    private final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);
    
    @OnOpen
    public void onOpen(
    	      Session session, 
    	      @PathParam("id") int id) throws IOException 
    {
        logger.info("Entered into Open");
        sessionIdMap.put(session, id);
        idSessionMap.put(id, session);
        
        String message="Chat id:" + id + " has been initiated";
        	broadcast(message);
		
    }
 
    @OnMessage
    public void onMessage(Session session, String message) throws IOException 
    {
        // Handle new messages
    	logger.info("Entered into Message: Got Message:"+message);
    	int id = sessionIdMap.get(session);
    	
	    	broadcast(id + ": " + message);
    }
 
    @OnClose
    public void onClose(Session session) throws IOException
    {
    	int id = sessionIdMap.get(session);
    	logger.info("Connection closed: "+ id);
    	
    	
    	sessionIdMap.remove(session);
    	idSessionMap.remove(id);
        
    	String message= id + " disconnected";
        broadcast(message);
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) 
    {
        // Do error handling here
    	logger.info("Entered into Error");
    }
    
    private static void broadcast(String message) 
    	      throws IOException 
    {	  
    	sessionIdMap.forEach((session, id) -> {
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
