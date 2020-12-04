package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.callbacks.DeactivationEventCallback;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private CountDownLatch latch;


    public R2D2Microservice(long duration, CountDownLatch latch) {

        super("R2D2");
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        DeactivationEventCallback DeactivationEventCB=new DeactivationEventCallback();
        subscribeEvent(DeactivationEvent.class, DeactivationEventCB);
        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {         //did this is microservice idk if should do it here? todo check
            Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
            terminate();
        });
        latch.countDown();
    }
}
