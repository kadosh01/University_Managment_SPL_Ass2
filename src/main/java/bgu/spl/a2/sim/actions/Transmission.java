package bgu.spl.a2.sim.actions;
import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.BankStates;

import java.util.*;

public class Transmission extends Action<String> {

    int amount;
    String sender;
    String receiver;
    String receiverBank;
    String senderBank;

    public Transmission(int amount, String receiver, String sender, String receiverBank, String SenderBank){
        this.amount=amount;
        this.sender=sender;
        this.receiver=receiver;
        this.receiverBank=receiverBank;
        this.senderBank=SenderBank;
        _actionName= "Transmission";
    }

    protected void start () {

        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> confAction = new Confirmation(sender, receiver, receiverBank, new BankStates());
        System.out.println("Creates confirm Action");
        actions.add(confAction);
        sendMessage(confAction, receiverBank, new BankStates());
        System.out.println("done with confirmation send message");
        then(actions, () ->  {
            Boolean result = actions.get(0).getResult().get();
            if(result == true){
                complete("Transmission succeed");
                System.out.println("Transmission succeed");
            }
            else{
                complete("Transmission failed");
                System.out.println("Transmission filed");
            }
        });
    }

}

