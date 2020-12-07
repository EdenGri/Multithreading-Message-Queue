package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.List;

public class AttackEvent implements Event<Boolean> {
    private Attack attack;

    public AttackEvent(Attack _attack) {
        attack=_attack;
    }

    public Attack getAttack(){
        return attack;
    }

    public void executeAttack(){
        Attack attack = this.getAttack();
        List<Integer> resources = attack.getSerials();
        Ewoks ewoks = Ewoks.getInstance();
        ewoks.acquireEwoks(resources);
        try {
            Thread.sleep(attack.getDuration());
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        ewoks.releaseEwoks(resources);
    }
}
