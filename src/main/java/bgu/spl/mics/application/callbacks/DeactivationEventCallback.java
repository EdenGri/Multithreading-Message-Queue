package bgu.spl.mics.application.callbacks;

import bgu.spl.mics.Callback;
import bgu.spl.mics.application.messages.DeactivationEvent;

public class DeactivationEventCallback implements Callback<DeactivationEvent> {
    @Override
    public void call(DeactivationEvent c) {
        try {
            Thread.sleep(c.getDuration());
        }catch (InterruptedException e){}
    }
}
