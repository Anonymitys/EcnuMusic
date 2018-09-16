package classcollection;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Song implements Serializable{
    public int albumid;
    public String albummid;
    public String albumname;
    @SerializedName("singer")
    public List<Singer> singerlist;
    public long songid;
    public String songmid;
    public String songname;
    public String vid;

}
