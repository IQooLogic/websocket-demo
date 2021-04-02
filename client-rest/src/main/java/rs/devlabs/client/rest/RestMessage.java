package rs.devlabs.client.rest;

public class RestMessage {

    private String from;
    private String text;

    public RestMessage() {
    }

    public RestMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
