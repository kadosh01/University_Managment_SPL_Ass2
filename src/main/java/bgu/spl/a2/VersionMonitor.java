package bgu.spl.a2;

/**
 * Describes a monitor that supports the concept of versioning - its idea is
 * simple, the monitor has a version number which you can receive via the method
 * {@link #getVersion()} once you have a version number, you can call
 * {@link #await(int)} with this version number in order to wait until this
 * version number changes.
 *
 * you can also increment the version number by one using the {@link #inc()}
 * method.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class VersionMonitor {
    private int _ver=0;

    //adding synchronization to prevent from threads to access _ver variable while another thread updates it
    public synchronized int getVersion() {
        return _ver;
    }

    //adding synchronization because this method calls notifyAll()
    public synchronized void inc() {
        _ver++;
        notifyAll();
    }

    //adding synchronization because this method calls wait()
    public synchronized void await(int version) throws InterruptedException {
        while(_ver==version) {
            try{
                wait();
            }
            catch(InterruptedException e){
            }
        }
    }
}
