package rs.devlabs.client.rest;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompSession;

public class RestMessageSenderSimulatorProcess extends BaseProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMessageSenderSimulatorProcess.class);
    private final List<String> names = Stream.of("Pera", "Mika", "Laza", "Zika", "Maja").collect(Collectors.toList());
    private final StompSession session;

    public RestMessageSenderSimulatorProcess(String name, StompSession session) {
        super(name);
        this.session = session;
    }

    @Override
    public void run() {
        LOGGER.info("Starting '{}'", getName());
        while (!isInterrupted()) {
            try {
                int index = ThreadLocalRandom.current().nextInt(0, names.size());
//                Thread.sleep(1000);
                String name = names.get(index);
                session.send("/app/live", new RestMessage(name, "Howdy!!"));
                LOGGER.info("Message sent to websocket server: {}", name);
            }/* catch (InterruptedException ex) {
                LOGGER.warn("Interrupted while waiting for thread to terminate", ex);
            }*/ catch (IllegalArgumentException ex) {
                LOGGER.warn("Unauthorized user, message not sent", ex);
            } catch (ConnectionLostException ex) {
                LOGGER.warn("Connection lost", ex);
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
