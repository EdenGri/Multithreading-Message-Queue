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
		private static MessageBus instance = new MessageBusImpl();//todo check if to add final
	}

	public static MessageBus getInstance(){
		return Singleton.instance;
	}

	private MessageBusImpl(){}


	private void subscribeGeneral(Class<? extends Message> type, MicroService m){
	messagesMap.putIfAbsent(type, new ConcurrentLinkedQueue<>());
	messagesMap.get(type).add(m); //todo check if this line needs to be synchronized (yuval did it synch) (get type)
	}

	@Override
	// A Microservice calls this method to subscribe itself for some type event
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if (type==null || m==null)
			throw new NullPointerException();
		subscribeGeneral(type, m);
	}

	@Override
	// A Microservice calls this method to subscribe itself for some type of broadcast
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if (type==null || m==null)
			throw new NullPointerException();
		subscribeGeneral(type, m);
    }

	@Override
	// a MicroService calls this method when finishing to treat the event in order to determine result of event
	public <T> void complete(Event<T> e, T result) {
		Future future = determineFutureMap.get(e);
		future.resolve(result); //future object associated with event e resolves to the result
	}

	@Override
	// A Microservice calls this method to add the broadcast message to queues of all Microservices subscribed to it
	public void sendBroadcast(Broadcast b) {
		ConcurrentLinkedQueue<MicroService> microServices;
		synchronized (b.getClass()){ //todo not sure this needs to be synchronized
			microServices = messagesMap.get(b.getClass());
			if (microServices == null) //if no one is subscribed for this broadcast do nothing
				return;
		}
		//traverses through MicroServices subscribed for broadcast and adds broadcast to their message queue
		for (MicroService i : messagesMap.get(b.getClass())){
			try{
				MicroServiceMap.get(i).put(b);
			} catch (InterruptedException e){
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
		MicroService upNext;
		Future<T> output = new Future<>();
		determineFutureMap.put(e, output);
		if (subscribers == null)
			return null;
		synchronized (e.getClass()){
			if(subscribers.isEmpty())
				return null;
			//takes next subscriber from head of message queue and adds him again to the end (in a round-robin manner)
			upNext = subscribers.poll();
			subscribers.add(upNext);
		}
		//adds event message to message queue of the subscriber that was next in line
		LinkedBlockingQueue<Message> messageQueue;
		synchronized (upNext){
			messageQueue = MicroServiceMap.get(upNext);
			if(messageQueue == null)
				return null;
			messageQueue.add(e);
		}
		return output;
	}

	@Override
	public void register(MicroService m) {
		MicroServiceMap.put(m, new LinkedBlockingQueue<>());
	}

	@Override
	public void unregister(MicroService m) {
		LinkedBlockingQueue<Message> messageQueue;
		messagesMap.forEach((t, s) -> {
			synchronized (t){
				s.remove(m);//removes MicroService m from the list of subscribers to that type of message
			}
		});
		synchronized (m){
			messageQueue = MicroServiceMap.get(m); //returns message queue of specific MicroService in the map
		}
		for (Message message : messageQueue){ //changes the output (future) of each message in the queue to null
			Future<?> future = determineFutureMap.get(message);
			if(future == null)
				return;
			else future.resolve(null);
		}
		MicroServiceMap.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		//goes to the message queue of the MicroService m and retrieves the next message from head of queue (while also removing it)
		return MicroServiceMap.get(m).take();
	}


}
