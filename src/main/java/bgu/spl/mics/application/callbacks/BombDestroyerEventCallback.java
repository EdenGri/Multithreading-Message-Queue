package bgu.spl.mics.application.callbacks;

import bgu.spl.mics.Callback;
import bgu.spl.mics.application.messages.BombDestroyerEvent;

public class BombDestroyerEventCallback implements Callback<BombDestroyerEvent> {
    @Override
    public void call(BombDestroyerEvent c) {
        try {
            Thread.sleep(c.getDuration());
        }catch (InterruptedException e){}
    }
}
