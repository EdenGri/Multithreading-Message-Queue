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
    //Microservice Lando subscribe to BombDestroyerEvent and supplies specific callback
    @Override
    protected void initialize() {
        subscribeEvent(BombDestroyerEvent.class, (c)->{
            try {
                //execute the bomb destroyer
                Thread.sleep(getDuration());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
                complete(c, true);
                //Lando send Terminate Broadcast
                sendBroadcast(new TerminateBroadcast());
                terminate();
                //sets termination time
                Diary.getInstance().setLandoTerminate(System.currentTimeMillis());

        });

        Main.countDownLatch.countDown();

    }
    //Return duration
    private long getDuration() {
        return duration;
    }
}
