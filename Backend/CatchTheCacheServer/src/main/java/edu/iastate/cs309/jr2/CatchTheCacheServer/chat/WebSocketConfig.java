package edu.iastate.cs309.jr2.CatchTheCacheServer.chat;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import edu.iastate.cs309.jr2.CatchTheCacheServer.cache.CustomConfigurator;

@ConditionalOnWebApplication
@Configuration
public class WebSocketConfig {
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}

	@Bean
	public CustomConfigurator customConfigurator() {
		return new CustomConfigurator();
	}
}
