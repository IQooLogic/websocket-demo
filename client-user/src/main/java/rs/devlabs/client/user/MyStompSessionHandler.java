package rs.devlabs.client.user;

import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("New session established: {}", session.getSessionId());
        session.subscribe("/client/queue/messages", this);
        LOGGER.info("Subscribed to /client/queue/messages");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        LOGGER.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return RestMessage.class;
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        LOGGER.error("Got an exception", exception);
        if (exception instanceof ConnectionLostException) {
            System.exit(100);
        }
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        RestMessage msg = (RestMessage) payload;
        LOGGER.info("Received : " + msg.getText() + " from : " + msg.getFrom());
    }
}
