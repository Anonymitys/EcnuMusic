package shouyeclass.singersong;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import classcollection.Singer;

public class MusicData {
    public long albumid;
    public String albummid;
    public String albumname;
    @SerializedName("singer")
    public List<Singer> singerList;
    public long songid;
    public String songmid;
    public String songname;
    public String vid;

}
