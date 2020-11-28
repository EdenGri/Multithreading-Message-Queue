package bgu.spl.mics.example.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.example.messages.EventExmpl;

public class ExampleEventService extends MicroService {

    public ExampleEventService() {

        super("ofryEden");
    }

    @Override
    public void initialize() {
        subscribeEvent(EventExmpl.class, ev -> {
            System.out.println("Event Handler " + getName() + " got a new event from " + ev.getSenderName());
            terminate();
            System.out.println("Mission Terminated");
        });
    }

}
