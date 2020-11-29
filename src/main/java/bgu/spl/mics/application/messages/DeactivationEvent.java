package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class DeactivationEvent implements Event<Boolean> {
    private int duration;

    public DeactivationEvent(int _duration) {
        duration=_duration;
    }

    public int getDuration() {
        return duration;
    }
}
