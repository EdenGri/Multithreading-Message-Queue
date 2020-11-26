package bgu.spl.mics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


import static org.junit.jupiter.api.Assertions.*;


public class FutureTest {

    private Future<String> future;

    @BeforeEach
    public void setUp(){
        future = new Future<>();
    }

    @Test
    public void testGet()
    {
        assertFalse(future.isDone());
        future.resolve("");
        assertTrue(future.isDone());
        future.get();
        assertTrue(future.isDone());
    }

    @Test
    public void testResolve(){
        String str = "ofryAndEden"+Math.random();
        future.resolve(str);
        assertTrue(future.isDone());
        assertTrue(str.equals(future.get()));
    }

    @Test
    public void testIsDone(){
        String str = "ofryAndEden"+Math.random();
        assertFalse(future.isDone());
        future.resolve(str);
        assertTrue(future.isDone());
    }

    @Test
    public void testGetWithTimeOut() throws InterruptedException
    {
        String str = "ofryAndEden"+Math.random();
        assertFalse(future.isDone());
        long t0 = System.currentTimeMillis();
        assertNull(future.get(100,TimeUnit.MILLISECONDS));
        long t1 = System.currentTimeMillis();
        assertTrue(t1-t0 >= 100);
        assertFalse(future.isDone());
        future.resolve(str);
        assertEquals(future.get(100,TimeUnit.MILLISECONDS),str);
    }
}
