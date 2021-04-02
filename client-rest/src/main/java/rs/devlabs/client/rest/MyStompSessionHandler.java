package rs.devlabs.client.rest;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyStompSessionHandler.class);
    private final Map<String, StompSession> sessions = new HashMap<>();// sessionId, session

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        LOGGER.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/message_queue", this);
        LOGGER.info("Subscribed to /topic/message_queue");
//        session.send("/app/live", getSampleMessage());
//        LOGGER.info("Message sent to websocket server");
        sessions.put(session.getSessionId(), session);

        RestMessageSenderSimulatorProcess process = new RestMessageSenderSimulatorProcess("RestMessageSenderSimulatorProcess", session);
        process.start();
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
    public void handleFrame(StompHeaders headers, Object payload) {
        RestMessage msg = (RestMessage) payload;
        LOGGER.info("Received : " + msg.getText() + " from : " + msg.getFrom());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        LOGGER.error("Got an exception", exception);
    }

    /**
     * A sample message instance.
     *
     * @return instance of <code>Message</code>
     */
    private RestMessage getSampleMessage() {
        RestMessage msg = new RestMessage();
        msg.setFrom("Nicky");
        msg.setText("Howdy!!");
        return msg;
    }
}
