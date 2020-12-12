package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBusImpl implements MessageBus {


    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<MicroService>> messagesMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Event, Future> determineFutureMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> MicroServiceMap = new ConcurrentHashMap<>();

    private static class Singleton {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    public static MessageBusImpl getInstance() {
        return Singleton.instance;
    }

    private MessageBusImpl() {
    }


    private void subscribeGeneral(Class<? extends Message> type, MicroService m) {
        //adds specific message type to message map if not yet added
        messagesMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        //adds ("subscribes") m to specific message type
        messagesMap.get(type).add(m);
    }

    @Override
    // A Microservice calls this method to subscribe itself for some type event
    public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
        if (type == null || m == null)
            throw new NullPointerException();
        subscribeGeneral(type, m);
    }

    @Override
    // A Microservice calls this method to subscribe itself for some type of broadcast
    public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
        if (type == null || m == null)
            throw new NullPointerException();
        subscribeGeneral(type, m);
    }

    @Override
    // a MicroService calls this method when finishing to treat the event in order to determine result of event
    public <T> void complete(Event<T> e, T result) {
        //returns future in which specific event is mapped to
        Future future = determineFutureMap.get(e);
        //future we received resolves to the result
        future.resolve(result);
    }

    @Override
    // A Microservice calls this method to add the broadcast message to queues of all Microservices subscribed to it
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue<MicroService> subs;
        synchronized (b.getClass()) {
            subs = messagesMap.get(b.getClass());
            if (subs == null) //if no one is subscribed for this broadcast do nothing
                return;
        }
        //traverses through MicroServices subscribed for broadcast and adds broadcast to their message queue
        for (MicroService m : subs) {
            try {
                LinkedBlockingQueue<Message> ms = MicroServiceMap.get(m);
                //In case of unregistered microservice
                if(ms == null) {
                    continue;
                }
                ms.put(b);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    // A Microservice calls this method to add the event to message queue of one of the Microservices that are subscribed (in a round-robin manner)
    //This method returns a Future object - from which the sending Microservice retrieve result of event once completed
    public <T> Future<T> sendEvent(Event<T> e) {
        //fetches all MicroServices subscribed to this type of event
        ConcurrentLinkedQueue<MicroService> subscribers = messagesMap.get(e.getClass());
        if (subscribers == null)
            return null;
        MicroService upNext;
        Future<T> output = new Future<>();
        determineFutureMap.put(e, output);
        //validates the integrity of the round-robin in case of multiple events of the same type.
        synchronized (e.getClass()) {
            //takes next subscriber from head of message queue and adds him again to the end (in a round-robin manner)
            upNext = subscribers.poll();
            if (upNext == null)
                return null;
            subscribers.add(upNext);
        }
        //adds event message to message queue of the subscriber that was next in line
        LinkedBlockingQueue<Message> messageQueue;
        synchronized (upNext) {
            messageQueue = MicroServiceMap.get(upNext); //gets next in line
            if (messageQueue == null)
                return null;
            messageQueue.add(e);//adds message to his message queue
        }
        return output;
    }

    @Override
    //registers microservice to message bus and creates queue for it
    public void register(MicroService m) {
        MicroServiceMap.putIfAbsent(m, new LinkedBlockingQueue<>());
    }

    @Override
    public void unregister(MicroService m) {
        messagesMap.forEach((t, s) -> {
            synchronized (t) {
                //removes MicroService m from the list of subscribers to that type of message
                s.remove(m);
            }
        });
        LinkedBlockingQueue<Message> messageQueue;
        synchronized (m) {
            messageQueue = MicroServiceMap.get(m);
        }
        //In case of unregister of unexciting microservice
        if(messageQueue == null)
        {
            return;
        }
        //returns message queue of specific MicroService in the map
        for (Message message : messageQueue) {
            if (message instanceof Broadcast) {
                continue;
            }
            //changes the output (future) of each message in the queue to null
            Future<?> future = determineFutureMap.get(message);
            if (future != null)
                future.resolve(null);
        }
        MicroServiceMap.remove(m);

    }

    @Override
    public Message awaitMessage(MicroService m) throws InterruptedException {
        //goes to the message queue of the MicroService m and retrieves the next message from head of queue (while also removing it)
        LinkedBlockingQueue<Message> queue = MicroServiceMap.get(m);

        //In case of unregister microservice
        if(queue == null) {
            return null;
        }

        return queue.take();
    }


}
