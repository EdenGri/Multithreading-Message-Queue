package bgu.spl.mics;

import bgu.spl.mics.example.messages.BroadcastExmpl;
import bgu.spl.mics.example.messages.EventExmpl;
import bgu.spl.mics.example.services.ExampleBroadcastService;
import bgu.spl.mics.example.services.ExampleEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBusTest {


    private MessageBus messageBus;
    private EventExmpl exampleEvent;
    private BroadcastExmpl exampleBroadcast;

    @BeforeEach
    public void setUp() {
        messageBus = MessageBusImpl.getInstance();
        exampleEvent = new EventExmpl("ofry");
        exampleBroadcast = new BroadcastExmpl("eden");

    }

    @Test
    public void testType(){
        assertNotNull(messageBus);
        assertSame(MessageBusImpl.getInstance(), MessageBusImpl.getInstance());
    }

    @Test
    //updated tests to work in according to message bus implementation and structure
    //tests event in general (subscribe & send)
    public void testSubscribeEvent() throws InterruptedException {
        ExampleEventService m1 = new ExampleEventService();
        Message message;
        assertThrows(NullPointerException.class, () -> messageBus.subscribeEvent(null, m1));
        assertThrows(NullPointerException.class, () -> messageBus.subscribeEvent(EventExmpl.class, null));
        messageBus.register(m1);
        messageBus.subscribeEvent(EventExmpl.class, m1);
        Future future = messageBus.sendEvent(exampleEvent);
        assertNotNull(future);
        message = messageBus.awaitMessage(m1);
        assertSame(exampleEvent, message);
        messageBus.unregister(m1);
    }

    @Test
    //updated tests to work in according to message bus implementation and structure
    //tests broadcast in general (subscribe & send)
    public void testSubscribeBroadcast() throws InterruptedException{
        ExampleBroadcastService m1 = new ExampleBroadcastService();
        assertThrows(NullPointerException.class, () -> messageBus.subscribeBroadcast(null, m1));
        assertThrows(NullPointerException.class, () -> messageBus.subscribeBroadcast(BroadcastExmpl.class, null));
        messageBus.register(m1);
        messageBus.subscribeBroadcast(BroadcastExmpl.class, m1);
        ExampleBroadcastService m2 = new ExampleBroadcastService();
        messageBus.register(m2);
        messageBus.subscribeBroadcast(BroadcastExmpl.class, m2);
        messageBus.sendBroadcast(exampleBroadcast);
        Message message1 = messageBus.awaitMessage(m1);
        Message message2 = messageBus.awaitMessage(m2);
        assertSame(message1,message2);
        assertSame(message1,exampleBroadcast);
        messageBus.unregister(m1);
        messageBus.unregister(m2);
    }

    @Test
    public void testComplete() {
        //updated tests to work in according to message bus implementation and structure
        assertThrows(NullPointerException.class, () -> messageBus.complete(exampleEvent, null));
        assertThrows(NullPointerException.class, () -> messageBus.complete(null, "true"));
        ExampleEventService m1 = new ExampleEventService();
        messageBus.register(m1);
        m1.initialize();
        Future<String> future = messageBus.sendEvent(exampleEvent);
        assertFalse(future.isDone());
        messageBus.complete(exampleEvent, "true");
        assertEquals(future.get(), "true");
        assertTrue(future.isDone());
        messageBus.unregister(m1);
    }

    @Test
    //updated tests to work in according to message bus implementation and structure
    public void testAwaitMessage() throws InterruptedException{
        ExampleBroadcastService m1 = new ExampleBroadcastService();
        messageBus.register(m1);
        messageBus.subscribeBroadcast(BroadcastExmpl.class, m1);
        messageBus.sendBroadcast(exampleBroadcast);
        assertEquals(messageBus.awaitMessage(m1), exampleBroadcast);
        messageBus.unregister(m1);

    }

}
