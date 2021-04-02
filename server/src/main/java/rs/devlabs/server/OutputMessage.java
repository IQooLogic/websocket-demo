package rs.devlabs.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OutputMessage {

    private String from;
    private String text;
    private String time;

    public OutputMessage(final String from, final String text, final String time) {
        this.from = from;
        this.text = text;
        this.time = time;
    }

    public OutputMessage(RestMessage clientMessage) {
        this.from = clientMessage.getFrom();
        this.text = clientMessage.getText();
        this.time = new SimpleDateFormat("HH:mm").format(new Date());
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }
}
