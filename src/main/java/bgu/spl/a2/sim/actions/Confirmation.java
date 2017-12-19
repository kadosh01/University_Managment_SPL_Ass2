package bgu.spl.a2.sim.actions;


import bgu.spl.a2.Action;
import bgu.spl.a2.PrivateState;

import java.util.*;

public class Confirmation extends Action<Boolean> {

    String sender;
    String reciver;
    String reciverBank;
    PrivateState bankStates;
    String actionName="Confirmation";

    public Confirmation(String sender, String reciver, String reciverBank, PrivateState bankStates){
        this.sender=sender;
        this.reciver=reciver;
        this.reciverBank=reciverBank;
        this.bankStates=bankStates;
    }

    protected void start(){
        List<Action<Boolean>> actions = new ArrayList <>();
        then(actions, ()-> {
            System.out.println("Confirmation complete has been called");
            complete(true);
        });
    }
}