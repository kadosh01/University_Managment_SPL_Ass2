package bgu.spl.a2;

import java.util.Map;
import java.util.*;
import java.lang.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {
	private int nthreads; // number of thread
	private ConcurrentHashMap<String,Queue<Action>> _actionsList;
	private ConcurrentHashMap<String,PrivateState> _privatestateList;
	protected ConcurrentHashMap<String,Boolean> _workonList;
	private Thread[] pool;
	private boolean finish = false;
	private VersionMonitor vm;
	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	public ActorThreadPool(int nthreads) {
		this.nthreads=nthreads;
		_actionsList=new ConcurrentHashMap<>() ;
		_privatestateList = new ConcurrentHashMap<>();
		_workonList = new ConcurrentHashMap<>();
		pool = new Thread[nthreads];
		vm= new VersionMonitor();
		for(int i=0; i<pool.length; i++){
			pool[i]= new Thread(() -> {
                while(!finish){
			        int version= vm.getVersion();
                    Set<String> actors= _actionsList.keySet();
                    for(String id : actors){
                        if( _workonList.get(id)!=null && !_workonList.get(id)){
                            if(_actionsList.get(id).size()>0){
                            	try {
									Queue<Action> actor_actions = _actionsList.get(id);
									actor_actions.remove().handle(this, id, _privatestateList.get(id));
									_workonList.put(id, true); //check if the value changes
								}
								catch (NoSuchElementException e){

								}
                            }
                        }
                    }
                    try {
                        vm.await(version);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
		}
	}

	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return _privatestateList;
	}
	
	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		return _privatestateList.get(actorId);
	}


	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
        if (_actionsList.containsKey(actorId)){
        	if(action!=null) {
				_actionsList.get(actorId).add(action);
				vm.inc();
			}
        }
        else{
            ConcurrentLinkedQueue<Action> newActor =new ConcurrentLinkedQueue<>();
            if(action!=null)
            	newActor.add(action);
            _actionsList.put(actorId,newActor);
            _privatestateList.put(actorId,actorState);
            _workonList.put(actorId,false);
			vm.inc();
        }
		System.out.println(action.getActionName()+"Action submitted");
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		finish= true;
		vm.inc();
		for(int i=0; i<pool.length; i++)
			pool[i].join();
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for(int i=0; i<pool.length; i++)
			pool[i].start();
	}

	public void setWorkOn(String actorId, boolean state)
	{
		_workonList.put(actorId,state);
	}
}
