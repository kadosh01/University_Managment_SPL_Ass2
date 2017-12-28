package bgu.spl.a2;

import sun.security.pkcs.ContentInfo;

import java.util.Map;
import java.util.*;
import java.lang.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

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
	public ConcurrentHashMap<String,BlockingQueue<Action>> _actionsList;
	private ConcurrentHashMap<String,PrivateState> _privatestateList;
	protected ConcurrentHashMap<String,AtomicBoolean> _workonList;
	private Thread[] pool;
	private volatile boolean finish ;
	private VersionMonitor vm;

	private static ActorThreadPool ac;
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
		ac=this;

		finish=false;
		this.nthreads=nthreads;
		_actionsList=new ConcurrentHashMap<>() ;
		_privatestateList = new ConcurrentHashMap<>();
		_workonList = new ConcurrentHashMap<>();
		pool = new Thread[nthreads];
		vm= new VersionMonitor();
		for(int i=0; i<pool.length; i++){
			pool[i]= new Thread(() -> {
                while(!Thread.interrupted()){
			        int version= vm.getVersion();
                    Set<String> actors= _actionsList.keySet();
                    for(String id : actors){
                        if( _workonList.get(id)!=null && setWorkOn(id,true) ){ // change this : !_workonList.get(id).get()
                            if(_actionsList.get(id).size()>0){
								Queue<Action> actor_actions = _actionsList.get(id);
								if(actor_actions.peek()._actionName.equals("UnregStudents")){
									System.out.print("");
								}
								String n=actor_actions.peek()._actionName;
								actor_actions.poll().handle(this, id, _privatestateList.get(id));
								vm.inc();
                            }
							setWorkOn(id,false);
                        }
                    }
                    try {
                        vm.await(version);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                //System.out.println(Thread.currentThread().getName() +"is in state : "+ Thread.currentThread().getState());
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
				//getPrivateState(actorId).addRecord(action.getActionName());
				vm.inc();
			}
        }
        else{
            BlockingQueue<Action> newActor =new LinkedBlockingQueue<>();
            if(action!=null) {
				newActor.add(action);
				//actorState.addRecord(action.getActionName());
			}
            _actionsList.putIfAbsent(actorId,newActor); // change put to putifabsent
            _privatestateList.putIfAbsent(actorId,actorState);// change put to putifabsent
             _workonList.putIfAbsent(actorId,new AtomicBoolean(false));// change put to putifabsent
			vm.inc();
        }
        	System.out.println(action.getActionName()+" Action submitted by actor: "+actorId+" by thread: "+Thread.currentThread().getName());
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

		for(int i=0; i<pool.length; i++) {
			pool[i].interrupt();
			//pool[i].join();
			//System.out.println(pool[i].getName()+ "is in state : " + Thread.currentThread().getState());
		}
		//System.out.println("---shutdown---  num of actions: "+_actionsList.size());
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for(int i=0; i<pool.length; i++)
			pool[i].start();

		/*while(true){
			for(int i=0; i<pool.length; i++)
			System.out.println(pool[i].getState());
			try {
				wait(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
	}

	 public boolean setWorkOn(String actorId, boolean state)
	{
		return _workonList.get(actorId).compareAndSet(!state,state);

	}

	static public ActorThreadPool getinstance(){return ac;}
}
