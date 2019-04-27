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

	private static final Logger logger = LoggerFactory.getLogger(CacheListWebSocket.class);

	@OnOpen
	public void onOpen(Session session) throws IOException {
		logger.info("Connection Opened: " + session.getId());
		sessionList.add(session);
	}

	@OnMessage
	public void onMessage(Session session, String message) throws IOException {
		logger.info("Entered into Message: Got Message: " + message);
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		logger.info("Connection Closed: " + session.getId());
		sessionList.remove(session);

	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		// logger.info("Error: " + throwable.getMessage());
		throwable.printStackTrace();
	}

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
