package bgu.spl.a2.Gson;

import com.google.gson.annotations.SerializedName;

public class Computer {


    private String Type;
    @SerializedName("Sig Success")
    private long sigSuccess;

    @SerializedName("Sig Fail")
    private long sigFail;


    public String getType(){return Type;}

    public long getSigSuccess(){return sigSuccess;}

    public long getSigFail(){return sigFail;}
}
