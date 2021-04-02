package rs.devlabs.server;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class RestListenerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestListenerController.class);

    @Autowired
    private RestMessagesQueue queue;

    @MessageMapping("/live")
    @SendTo("/topic/message_queue")
    public OutputMessage send(final RestMessage message) throws Exception {
        queue.put(message);
        LOGGER.info("Queue size is {}", queue.size());
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}
