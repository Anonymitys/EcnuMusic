package shouyeclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlayList implements Serializable{
    public String dissid;
    public String createtime;
    @SerializedName("creator")
    public Creator creator;
    public String dissname;
    public String imgurl;
    public long listennum;
}
