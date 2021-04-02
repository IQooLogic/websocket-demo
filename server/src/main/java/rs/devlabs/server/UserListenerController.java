package rs.devlabs.server;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Deprecated
@Controller
public class UserListenerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserListenerController.class);

    @Autowired
    private RestMessagesQueue queue;

    @MessageMapping("/chat")
    @SendToUser("/queue/messages")
    public OutputMessage send(final RestMessage message, Principal principal) throws Exception {
        queue.put(message);
        LOGGER.info("Queue size is {}, {}", queue.size(), principal.getName());
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}
