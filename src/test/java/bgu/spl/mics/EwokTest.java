package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Ewok;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class EwokTest {

    private Ewok ewok;

    @BeforeEach
    public void setUp() {
        ewok = new Ewok();
    }

    @Test
    public void testAcquire() {
        assertDoesNotThrow(() -> {
            Executable[] lambdas = {() -> ewok.acquire(), () -> ewok.release()};
            //this for loop makes sure no exceptions are thrown with series of acquires/releases
            for (int i = 0; i < (1 << 10); i++) {
                int n = i;
                int[] indexes = new int[10]; //we want indexes to be all options of series of 0/1 (0 represents acquire, 1 release)
                for (int j = 0; j < 10; j++) {
                    indexes[j] = n & 1;
                    n = n >> 1;
                }

                for (int index : indexes)
                    lambdas[index].execute();
            }
        });
        assertTrue(ewok.isAvailable());
        ewok.acquire();
        assertFalse(ewok.isAvailable());
    }

    @Test
    public void testRelease() {
        //makes sure no exception is thrown in series of releases
        //we can notice that first test also checks releases
        for (int j = 0; j < 10; j++)
            assertDoesNotThrow(() -> ewok.release());
        ewok.acquire();
        assertFalse(ewok.isAvailable());
        ewok.release();
        assertTrue(ewok.isAvailable());
    }

}