package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private Attack attack;

    public AttackEvent(Attack _attack) {
        attack=_attack;
    }

    public Attack getAttack(){
        return attack;
    }
}
