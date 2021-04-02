package rs.devlabs.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RestMessagesQueue {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestMessagesQueue.class);
    private final BlockingQueue<RestMessage> QUEUE = new LinkedBlockingDeque<>(10_000);

    public boolean put(RestMessage message) {
        try {
            QUEUE.put(message);
            return true;
        } catch (InterruptedException ex) {
            LOGGER.error("Error putting message into queue", ex);
        }
        return false;
    }

    public RestMessage take() throws InterruptedException {
        return QUEUE.take();
    }

    public long size() {
        return QUEUE.size();
    }
}
