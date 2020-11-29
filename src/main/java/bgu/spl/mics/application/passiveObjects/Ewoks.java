package bgu.spl.mics.application.passiveObjects;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {

    private final Map<Integer, Ewok> ewokMap = new HashMap<>();

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
    public void load(Ewok[] ewoks) {
        synchronized (ewokMap) {
            for (Ewok e : ewoks) {
                ewokMap.put(e.getSerialNumber(), e);
            }
        }
    }

    public void releaseEwoks(List<Integer> serialNumbers)


}
