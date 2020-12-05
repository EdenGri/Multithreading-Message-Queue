package bgu.spl.mics.application.callbacks;

import bgu.spl.mics.Callback;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;


import java.util.List;

public class AttackEventCallback implements Callback<AttackEvent>{

    @Override
    public void call(AttackEvent c) {
        try {
            Attack attack = c.getAttack();
            List<Integer> resources = attack.getSerials();
            Ewoks ewoks = Ewoks.getInstance();
            ewoks.acquireEwoks(resources);
            Thread.sleep(attack.getDuration());
            ewoks.releaseEwoks(resources);
           // complete(c, true); //when we transfer this to the microservices then this line will not be red
            Diary diary=Diary.getInstance();
            diary.setHanSoloFinish(System.currentTimeMillis());// add this line to hansolo only
            diary.setC3POFinish(System.currentTimeMillis()); //add this line to C3PO only
            // add synchronize
            //diary.setTotalAttacks(diary.getTotalAttacks()+1); //atomic integer + getandincrement
        } catch (InterruptedException e){}
    }
}
//todo DELETE THIS CLASS
