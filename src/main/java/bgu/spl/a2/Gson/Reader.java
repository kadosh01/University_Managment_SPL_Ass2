package bgu.spl.a2.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reader {

    private String threads;
    private List<Computer> Computers;
    @SerializedName("Phase 1")
    private List<Action> Phase1;
    @SerializedName("Phase 2")
    private List<Action> Phase2;
    @SerializedName("Phase 3")
    private List<Action> Phase3;

    public String getThreads(){return threads;}

    public List<Computer> getComputers(){return Computers;}

    public List<Action> getPhase1(){return Phase1;}

    public List<Action> getPhase2(){return Phase2;}

    public List<Action> getPhase3(){return Phase3;}
}
