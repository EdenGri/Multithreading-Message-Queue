package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
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
    long duration;


    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class, (c)->{//todo delete the c
            try {
                Thread.sleep(getDuration());
                complete(c, true);//todo check if the future result needs to be true?
                sendBroadcast(new TerminateBroadcast());//todo check if it needs to be lando who send the terminateBroadcast
                terminate();
                Diary.getInstance().setLandoTerminate(System.currentTimeMillis());
            }catch (InterruptedException e){
                e.printStackTrace();// todo check why we need this
            }
        });


        Main.countDownLatch.countDown();

    }

    private long getDuration() {
        return duration;
    }
}
