package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {
    private int duration;

    public BombDestroyerEvent(int _duration) {
        duration=_duration;
    }

    public int getDuration() {
        return duration;
    }
}
