package rs.devlabs.server;

import java.security.Principal;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setUserDestinationPrefix("/client");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat")
                .setHandshakeHandler(new DefaultHandshakeHandlerImpl())
                .setAllowedOrigins("localhost");
        registry.addEndpoint("/chat")
                .setHandshakeHandler(new DefaultHandshakeHandlerImpl())
                .setAllowedOrigins("localhost")
                .withSockJS();

        registry.addEndpoint("/live")
                .setHandshakeHandler(new DefaultHandshakeHandlerImpl())
                .setAllowedOrigins("localhost");
        registry.addEndpoint("/live")
                .setHandshakeHandler(new DefaultHandshakeHandlerImpl())
                .setAllowedOrigins("localhost")
                .withSockJS();
    }

    private static class DefaultHandshakeHandlerImpl extends DefaultHandshakeHandler {

        @Override
        protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
            String user = request.getURI().getQuery();
            user = user == null ? "unknown" : user;
            LOGGER.info("Registering user '{}', {}", user, request.getURI().getPath());
            return new WebSocketPrincipal(user);
        }
    }
}
