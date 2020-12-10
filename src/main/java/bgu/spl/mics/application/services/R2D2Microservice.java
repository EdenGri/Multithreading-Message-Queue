package bgu.spl.mics.application.services;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
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
    long duration;


    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration=duration;
    }
    //Microservice R2D2 subscribe to DeactivationEvent and supplies specific callback
    //Microservice R2D2 subscribe to Terminate Broadcast and supplies specific callback
    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class, (c) -> {
            try {
            //execute the deactivation
            Thread.sleep(getDuration());
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            complete(c, true);
            //sets deactivation time
            Diary.getInstance().setR2D2Deactivate(System.currentTimeMillis());

        });

        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {
            terminate();
            //sets termination time
            Diary.getInstance().setR2D2Terminate(System.currentTimeMillis());
        });
        Main.countDownLatch.countDown();
    }

    //Return duration
    private long getDuration() {
        return duration;
    }
}
