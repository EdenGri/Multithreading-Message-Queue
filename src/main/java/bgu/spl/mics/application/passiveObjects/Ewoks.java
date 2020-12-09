package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    public void load(int ewoksNum) {
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
        //validates input
        if(!containsAllSerials(serialNumbers))
            return;
        serialNumbers.sort(Comparator.naturalOrder());
        for (int number : serialNumbers) {
            Ewok ewok;
            ewok = ewokMap.get(number);
            //releases ewok and notifies its release
            synchronized (ewok) {
                ewok.release();
                ewok.notifyAll();
            }
        }
    }

    public void acquireEwoks(List<Integer> serialNumbers) {
        try {
            //validates input
            if (!containsAllSerials(serialNumbers))
                return;
            serialNumbers.sort(Comparator.naturalOrder());
            for(int num : serialNumbers){
                Ewok ewok;
                ewok = ewokMap.get(num);
                //waits until specific ewok we want is available
                synchronized (ewok){
                    while(!ewok.isAvailable()){
                        ewok.wait();
                    }
                    ewok.acquire();
                }
            }

        } catch (InterruptedException e){}
    }
}
