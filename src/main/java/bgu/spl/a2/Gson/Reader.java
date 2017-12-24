package bgu.spl.a2.Gson;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Reader implements Serializable {

    private String threads;
    private List<Computer> Computers;
    @SerializedName("Phase 1")
    private List<ActionParsing> Phase1;
    @SerializedName("Phase 2")
    private List<ActionParsing> Phase2;
    @SerializedName("Phase 3")
    private List<ActionParsing> Phase3;

    public String getThreads(){return threads;}

    public List<Computer> getComputers(){return Computers;}

    public List<ActionParsing> getPhase1(){return Phase1;}

    public List<ActionParsing> getPhase2(){return Phase2;}

    public List<ActionParsing> getPhase3(){return Phase3;}
}
