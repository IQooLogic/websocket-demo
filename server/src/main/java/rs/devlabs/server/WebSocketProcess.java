package rs.devlabs.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class WebSocketProcess extends BaseProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketProcess.class);
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private RestMessagesQueue messageQueue;

    public WebSocketProcess(String name) {
        super(name);
    }

    @Override
    public void run() {
        LOGGER.info("Starting '{}'", getName());
        while (!isInterrupted()) {
            try {
                RestMessage clientMessage = messageQueue.take();
                LOGGER.info("Sending to user '{}'", clientMessage.getFrom());
                simpMessagingTemplate.convertAndSendToUser(clientMessage.getFrom(), "queue/messages",
                        new OutputMessage(clientMessage));
            } catch (InterruptedException ex) {
                LOGGER.warn("Interrupted while waiting for thread to terminate", ex);
            } catch (IllegalArgumentException ex) {
                LOGGER.warn("Unauthorized user, message not sent", ex);
            }
        }
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stopThread() {
        LOGGER.info("Interrupting thread: {}", getName());
        try {
            interrupt();
            join();
            LOGGER.info("DONE");
        } catch (InterruptedException ex) {
            LOGGER.warn("Interrupted while waiting for thread to terminate", ex);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LOGGER.error("Unhandled exception caught, '{}' thread is now terminated!", getName(), ex);
        System.exit(200);
    }
}
