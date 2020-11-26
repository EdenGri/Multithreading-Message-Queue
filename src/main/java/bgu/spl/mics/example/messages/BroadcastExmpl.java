package bgu.spl.mics.example.messages;

import bgu.spl.mics.Broadcast;

public class BroadcastExmpl implements Broadcast {

    private String senderId;

    public BroadcastExmpl(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderId() {
        return senderId;
    }

}
