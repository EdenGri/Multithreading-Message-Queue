package bgu.spl.mics.application.services;
import java.util.List;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
    private Callback<AttackEvent> callback;
	
    public C3POMicroservice() {
        super("C3PO");
        callback= new Callback<AttackEvent>() {
            @Override
            public void call(AttackEvent c) {
                sleep()
            }
        }
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, callback);
        subscribeBroadcast(TerminateBroadcast.class,);

    }
}
