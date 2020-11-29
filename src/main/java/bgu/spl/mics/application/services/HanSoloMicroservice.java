package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.callbacks.AttackEventCallback;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {
    int numOfAttack;
    public HanSoloMicroservice() {
        super("Han");
        numOfAttack=0;
    }


    @Override
    protected void initialize() {
        AttackEventCallback AttackEventCB=new AttackEventCallback();
        subscribeEvent(AttackEvent.class, AttackEventCB);

    }
}
