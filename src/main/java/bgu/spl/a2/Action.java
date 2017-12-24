package bgu.spl.a2;

import java.util.Collection;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {
    protected String _actionName="empty";
    protected Promise<R> _promise=new Promise<>();
    protected int _callback_count=0;
    protected callback _callback;
    protected ActorThreadPool _pool;
    protected PrivateState _privateState;
    protected String _actorID="empty actor";
    private boolean _firstHandle=true;

	/**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();
    

    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    */
   /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {

       if(_firstHandle){
           _pool= pool;
           _privateState= actorState;
           _firstHandle= false;
           System.out.println(_actorID+" : "+_actionName+" status : start by - "+ Thread.currentThread().getName());
           start();

       }
       else{
           System.out.println(_actorID+" : "+_actionName+" status : continue by - "+ Thread.currentThread().getName());
           _callback.call();
       }
    }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) {
        _callback_count+=actions.size();
        _callback= callback;
        for(Action<?> act : actions)
        {
            act.getResult().subscribe(()->{
                _callback_count--;
                if(_callback_count==0)
                {
                    _pool.submit(this,_actionName,_privateState);
                }
            });
        }

    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
       _promise.resolve(result);
       //_pool.setWorkOn(_actorID,false); //change the state of the current actor

        System.out.println(_actorID+" : "+_actionName+" status : complete by - "+ Thread.currentThread().getName());
    }
    
    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
    	return _promise;
    }
    
    /**
     * send an action to an other actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
	 *    
     * @return promise that will hold the result of the sent action
     */
	public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState){
        _pool.submit(action,actorId,actorState);
        System.out.println(_actorID+" : "+_actionName+" status : send message to "+actorId+" by - "+ Thread.currentThread().getName());
        return action.getResult();
    }
	
	/**
	 * set action's name
	 * @param actionName
	 */
	public void setActionName(String actionName){
        _actionName=actionName;
	}
	
	/**
	 * @return action's name
	 */
	public String getActionName(){
       return _actionName;
	}
}
