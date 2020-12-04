package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.callbacks.BombDestroyerEventCallback;
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


    public LandoMicroservice(long duration, CountDownLatch latch) {

        super("Lando");
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        BombDestroyerEventCallback BombDestroyerEventCB=new BombDestroyerEventCallback();
        subscribeEvent(BombDestroyerEvent.class, BombDestroyerEventCB);
        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {         //did this is microservice idk if should do it here? todo check
            Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
            terminate();
        });
        latch.countDown();

    }
}
