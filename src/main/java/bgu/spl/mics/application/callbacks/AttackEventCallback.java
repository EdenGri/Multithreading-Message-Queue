package bgu.spl.mics.application.callbacks;

import bgu.spl.mics.Callback;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Diary;

public class AttackEventCallback implements Callback<AttackEvent>{

    @Override
    public void call(AttackEvent c) {
        try {
            Thread.sleep(c.getAttack().getDuration());
            Diary diary=Diary.getInstance();
            //todo add synchronize
            diary.setTotalAttacks(diary.getTotalAttacks()+1);
        }catch (InterruptedException e){}
    }
}
