package bgu.spl.a2;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */
public abstract class Action<R> {
    protected String _actionName = "empty";
    protected Promise<R> _promise = new Promise<>();
    protected AtomicInteger _callback_count = new AtomicInteger(0);
    protected callback _callback;
    protected ActorThreadPool _pool;
    protected PrivateState _privateState;
    protected String _actorID = "empty actor";
    private boolean _firstHandle = true;

    /**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();


    /**
     * start/continue handling the action
     * <p>
     * this method should be called in order to start this action
     * or continue its execution in the case where it has been already started.
     * <p>
     * IMPORTANT: this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     */
   /*package*/
    final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {

        if (_firstHandle) {
            _firstHandle=false;
            _pool = pool;
            _privateState = actorState;
            actorState.addRecord(_actionName);
            start();

        } else {
            _callback.call();
        }
    }


    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * <p>
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) {
        _callback_count = new AtomicInteger(actions.size());
        _callback = callback;
        if(actions.size()<=0){_pool.submit(this, _actorID, _privateState);}
        for (Action<?> act : actions) {
            act.getResult().subscribe(() -> {
                int local;
                do {
                    local = _callback_count.get();
                } while (!_callback_count.compareAndSet(local, local - 1));
                if (_callback_count.get() == 0) {
                    _pool.submit(this, _actorID, _privateState);
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
     * @param action     the action
     * @param actorId    actor's id
     * @param actorState actor's private state (actor's information)
     * @return promise that will hold the result of the sent action
     */
    public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState) {
        _pool.submit(action, actorId, actorState);
        return action.getResult();
    }

    /**
     * set action's name
     *
     * @param actionName
     */
    public void setActionName(String actionName) {
        _actionName = actionName;
    }

    /**
     * @return action's name
     */
    public String getActionName() {
        return _actionName;
    }
}
