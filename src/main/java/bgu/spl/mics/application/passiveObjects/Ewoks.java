package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;


/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    private final ConcurrentHashMap<Integer, Ewok> ewokMap = new ConcurrentHashMap<>();

    private Ewoks() { //default constructor
    }

    public static Ewoks getInstance() {
        return Singleton.instance;
    }

    private static class Singleton {
        private static Ewoks instance = new Ewoks();

    }

    //initializes ewok collection
    //adds all ewoks to the collection
    public void load(int ewoksNum) { //todo check this
        synchronized (ewokMap) {
            for (int i = 1; i <= ewoksNum; i++) {
                ewokMap.put(i, new Ewok(i));
            }
        }
    }

    private boolean containsAllSerials(List<Integer> serials){
        synchronized (ewokMap){
            return ewokMap.keySet().containsAll(serials);
        }
    }

    public void releaseEwoks(List<Integer> serialNumbers) {
        if(!containsAllSerials(serialNumbers))
            return;
        for (int number : serialNumbers) {
            Ewok ewok;
            synchronized (ewokMap){
                    ewok = ewokMap.get(number);
            }
            synchronized (ewok) {
                ewok.release();
                ewok.notifyAll();
            }
        }
    }
    //stimulates the execution of a mission by calling sleep
    public void executeMission(List<Integer> serialNumbers, int time){ //todo check if this needs to be here or other place
        if(!containsAllSerials(serialNumbers))
            return;
        if(time > 0){
            try{
                Thread.sleep(time);//todo fix/check time
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            releaseEwoks(serialNumbers);
        }
    }

    public boolean acquireEwoks(List<Integer> serialNumbers) {
        try {
            if (!containsAllSerials(serialNumbers))
                return false;
            serialNumbers.sort(Comparator.naturalOrder());
            for(int num : serialNumbers){
                Ewok ewok;
                synchronized (ewokMap){
                    ewok = ewokMap.get(num);
                }
                synchronized (ewok){
                    while(!ewok.isAvailable()){
                        ewok.wait();
                    }
                    ewok.acquire();
                }
            }

        } catch (InterruptedException e){
            return false;
        }
        return true;
    }
}
