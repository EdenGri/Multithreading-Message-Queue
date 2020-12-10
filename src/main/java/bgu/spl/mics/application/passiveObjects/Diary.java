package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private AtomicInteger totalAttacks= new AtomicInteger(0);
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;


    public static Diary getInstance(){
        return Singleton.diary;
    }

    private static class Singleton {
        private static Diary diary=new Diary();
    }

    //Increment by one the totalAttack
     public void incrementTotalAttacks(){
        totalAttacks.getAndIncrement();
     }

    public void resetTotalAttacks(){//todo delete
        totalAttacks=new AtomicInteger(0);
    } //todo delete

    private Diary(){}

    //Return totalAttack
    public AtomicInteger getTotalAttacks() {
        return totalAttacks;
    }
    //Set totalAttack
    public void setTotalAttacks(AtomicInteger _totalAttacks) {
        totalAttacks = _totalAttacks;
    }

    //Return finish time of Han
    public long getHanSoloFinish() {
        return HanSoloFinish;
    }
    //Set finish time of Han
    public void setHanSoloFinish(long _HanSoloFinish) {
        HanSoloFinish = _HanSoloFinish;
    }

    //Return finish time of C3PO
    public long getC3POFinish() {
        return C3POFinish;
    }
    //Set finish time of C3PO
    public void setC3POFinish(long _C3POFinish) {
        C3POFinish = _C3POFinish;
    }

    //Return Deactivation time of R2D2
    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }
    //Set Deactivation time of R2D2
    public void setR2D2Deactivate(long _R2D2Deactivate) {
        R2D2Deactivate = _R2D2Deactivate;
    }

    //Return Leia terminate time
    public long getLeiaTerminate() {
        return LeiaTerminate;
    }
    //Set Leia terminate time
    public void setLeiaTerminate(long _LeiaTerminate) {
        LeiaTerminate = _LeiaTerminate;
    }

    //Return Han terminate time
    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }
    //Set Han terminate time
    public void setHanSoloTerminate(long _HanSoloTerminate) {
        HanSoloTerminate = _HanSoloTerminate;
    }

    //Return C3PO terminate time
    public long getC3POTerminate() {
        return C3POTerminate;
    }
    //Set C3PO terminate time
    public void setC3POTerminate(long _C3POTerminate) {
        C3POTerminate = _C3POTerminate;
    }

    //Return R2D2 terminate time
    public long getR2D2Terminate() {
        return R2D2Terminate;
    }
    //Set R2D2 terminate time
    public void setR2D2Terminate(long _R2D2Terminate) {
        R2D2Terminate = _R2D2Terminate;
    }

    //Return Lando terminate time
    public long getLandoTerminate() {
        return LandoTerminate;
    }
    //Set Lando terminate time
    public void setLandoTerminate(long _LandoTerminate) {
        LandoTerminate = _LandoTerminate;
    }
}
