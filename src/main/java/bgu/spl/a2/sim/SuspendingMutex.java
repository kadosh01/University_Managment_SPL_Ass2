package bgu.spl.a2.sim;
import bgu.spl.a2.Promise;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 * 
 * Note: this class can be implemented without any synchronization. 
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	
	private Computer _computer;
	private AtomicBoolean isFree;
	private Queue<Promise<Computer>> _promises;

	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer){
		_computer= computer;
		isFree= new AtomicBoolean(true);
		_promises= new LinkedBlockingQueue<>();
	}
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 * 
	 * @return a promise for the requested computer
	 */

	public Promise<Computer> down(){ //added synchronized
		Promise<Computer> comp= new Promise<>();
		_promises.add(comp);
		if(isFree.compareAndSet(true,false)){
		_promises.poll().resolve(_computer);
			// comp.resolve(_computer);
		}

	return comp;
}
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up(){
		//while(!isFree.compareAndSet(false, true))////
		Promise<Computer> prom=_promises.poll();//.resolve(_computer);
		if(prom!=null){
			prom.resolve(_computer);
		}
		else{isFree.compareAndSet(false,true);}

	}
}
