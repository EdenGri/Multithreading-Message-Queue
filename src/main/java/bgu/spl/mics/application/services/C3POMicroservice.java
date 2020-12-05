package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    private Ewoks ewoks;
    private CountDownLatch latch;

	
    public C3POMicroservice(CountDownLatch latch) {
        super("C3PO");
        ewoks = Ewoks.getInstance();
        this.latch = latch;
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, (c) -> {
            Attack attack = c.getAttack();
            List<Integer> resources = attack.getSerials();
            Ewoks ewoks = Ewoks.getInstance();
            ewoks.acquireEwoks(resources);
            try {
                Thread.sleep(attack.getDuration());
            } catch (InterruptedException e){
                e.printStackTrace();// todo check why we need this
            }
            ewoks.releaseEwoks(resources);
            complete(c, true); //when we transfer this to the microservices then this line will not be red
            Diary diary=Diary.getInstance();
            diary.setC3POFinish(System.currentTimeMillis());
            diary.incrementTotalAttacks();
        });
        subscribeBroadcast(TerminateBroadcast.class, (broadcast)-> {         //did this is microservice idk if should do it here? todo check
            Diary.getInstance().setC3POTerminate(System.currentTimeMillis());
            terminate();
        });
        latch.countDown();
    }
}
