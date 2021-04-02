package rs.devlabs.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Server extends SpringBootServletInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        SpringApplication.run(Server.class, args);
    }

    @Bean
    public WebSocketProcess webSocketProcess() {
        return new WebSocketProcess("WebSocketProcess");
    }

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Webscket Server Application context initialized");
        webSocketProcess().start();
    }
}
