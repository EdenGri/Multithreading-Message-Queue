package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.callbacks.BombDestroyerEventCallback;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    public LandoMicroservice(long duration) {
        super("Lando");
    }

    @Override
    protected void initialize() {
        BombDestroyerEventCallback BombDestroyerEventCB=new BombDestroyerEventCallback();
        subscribeEvent(BombDestroyerEvent.class, BombDestroyerEventCB);

    }
}
