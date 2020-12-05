package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
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
    long duration;


    public R2D2Microservice(long duration, CountDownLatch latch) {
        super("R2D2");
        this.latch = latch;
        duration=duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class, (c) -> {
            try {
            Thread.sleep(getDuration());
            }catch (InterruptedException e){
                e.printStackTrace();// todo check why we need this
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {
            Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
            terminate();
        });
        latch.countDown();
    }

    private long getDuration() {
        return duration;
    }
}
