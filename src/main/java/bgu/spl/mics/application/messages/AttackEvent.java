package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private Boolean isDone;
    private int duration;
    private List<Integer> serials;

    public AttackEvent(Boolean _isDone,int _duration,List<Integer> _serials) {
        isDone = _isDone;
        duration=_duration;
        serials=_serials;
    }


	
}
