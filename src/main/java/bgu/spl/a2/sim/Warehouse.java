package bgu.spl.a2.sim;

import java.util.HashMap;
import java.util.List;

/**
 * represents a warehouse that holds a finite amount of computers
 * and their suspended mutexes.
 * releasing and acquiring should be blocking free.
 */
public class Warehouse {
	private HashMap<String,SuspendingMutex> _pcMutexList;
	private HashMap<String,Computer> _pcList;
    private static Warehouse instance;

	private Warehouse(List<Computer> computers){
	    _pcMutexList=new HashMap<>();
	    _pcList= new HashMap<>();
        for (Computer comp:computers) {
            _pcList.put(comp.computerType,comp);
            SuspendingMutex compmutex=new SuspendingMutex(comp);
            _pcMutexList.put(comp.computerType,compmutex);
        }
    }

	public static Warehouse getInstance(List<Computer> computers){
	    if(instance==null){
	        instance=new Warehouse(computers);
        }
        return instance;
    }

    public static Warehouse getInstance(){
	    return instance;
    }

    public SuspendingMutex getMutex(String computerName){return _pcMutexList.get(computerName);}

}
