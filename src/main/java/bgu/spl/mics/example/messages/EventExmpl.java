package bgu.spl.mics.example.messages;

import bgu.spl.mics.Event;

public class EventExmpl implements Event<String>{

    private String senderName;

    public EventExmpl(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}