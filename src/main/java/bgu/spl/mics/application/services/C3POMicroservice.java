package bgu.spl.mics.application.services;
import java.util.HashMap;
import java.util.List;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Message;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.callbacks.AttackEventCallback;
import bgu.spl.mics.application.callbacks.TerminateBroadcastCallback;
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
    private HashMap<Class<? extends Message>,Callback<? extends Message>> CallbacksHM;
	
    public C3POMicroservice() {
        super("C3PO");
        CallbacksHM=new HashMap<>();
        TerminateBroadcastCallback _TBCallback=new TerminateBroadcastCallback();
        CallbacksHM.put(TerminateBroadcast.class,_TBCallback);
        AttackEventCallback _AECallback=new AttackEventCallback();
        CallbacksHM.put(AttackEvent.class,_AECallback);
    }

    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class, callback);
        subscribeBroadcast(TerminateBroadcast.class,);

    }
}
