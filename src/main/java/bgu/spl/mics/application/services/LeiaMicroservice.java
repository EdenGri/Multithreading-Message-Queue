package bgu.spl.mics.application.services;
import java.util.List;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Attack;


/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;

    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    @Override
    protected void initialize() {
    	for (Attack attack:attacks){
            int _duration =attack.getDuration();
            List<Integer> _Serials =attack.getSerials();
    	    AttackEvent newAttackEvent=new AttackEvent(false,_duration,_Serials);
    	    sendEvent(newAttackEvent);
        }
    }
}






