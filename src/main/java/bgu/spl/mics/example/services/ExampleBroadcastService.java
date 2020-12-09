package bgu.spl.mics.example.services;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.example.messages.BroadcastExmpl;

public class ExampleBroadcastService extends MicroService {


    public ExampleBroadcastService() {
        super("ofryEden");
    }

    @Override
    public void initialize() {
        subscribeBroadcast(BroadcastExmpl.class, message -> {
            System.out.println("Broadcast Handler " + getName() + " got a new message from " + message.getSenderId());
            terminate();
            System.out.println("Mission Terminated");
        });
    }

}
