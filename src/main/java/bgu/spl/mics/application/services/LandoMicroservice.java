package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

import java.util.concurrent.CountDownLatch;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private CountDownLatch latch;
    long duration;


    public LandoMicroservice(long duration, CountDownLatch latch) {
        super("Lando");
        duration=duration;
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class, (c)->{//todo delete the c
            try {
                Thread.sleep(getDuration());
            }catch (InterruptedException e){
                e.printStackTrace();// todo check why we need this
            }
        });

        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {
            Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
            terminate();
        });

        latch.countDown();

    }

    private long getDuration() {
        return duration;
    }
}
