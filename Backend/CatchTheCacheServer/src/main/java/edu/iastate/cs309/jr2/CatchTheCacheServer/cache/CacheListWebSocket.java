package edu.iastate.cs309.jr2.CatchTheCacheServer.cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/caches/websocket", configurator = CustomConfigurator.class)
@Component
public class CacheListWebSocket {

	// Store all socket session and their corresponding id
	private static List<Session> sessionList = new ArrayList<>();

	private static final Logger logger = LoggerFactory.getLogger(CacheWebSocket.class);

	/**
	 * Runs on socket open
	 * 
	 * @param session WebSocket Session to perform these actions on
	 * @throws IOException
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException {
		logger.info("Connection Opened: " + session.getId());
		sessionList.add(session);
	}

	/**
	 * Runs when the socket receives a message
	 * 
	 * @param session WebSocket Session to perform these actions on
	 * @param message String message that was received
	 * @throws IOException
	 */
	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		logger.info("Entered into Message: Got Message: " + message);
	}

	/**
	 * Runs on socket close
	 * 
	 * @param session WebSocket Session to perform these actions on
	 * @throws IOException
	 */
	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Connection Closed: " + session.getId());
		sessionList.remove(session);

	}

	/**
	 * Runs on error
	 * 
	 * @param session   WebSocket Session to perform these actions on
	 * @param throwable Throwable exception that occurred
	 */
	@OnError
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
	}

	/**
	 * Broadcast message to all sessions
	 * 
	 * @param message String message to broadcast
	 * @throws IOException
	 */
	public static void broadcast(String message) throws IOException {
		for (Session session : sessionList) {
			logger.info("Broadcasting for cache list to session: " + session.getId());
			synchronized (session) {
				try {
					session.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
