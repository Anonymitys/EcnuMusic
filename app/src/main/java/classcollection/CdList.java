package classcollection;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CdList implements Serializable{
    public String dissname;
    public String logo;
    public String headurl;
    public String nickname;
    public int total_song_num;
    @SerializedName("songlist")
    public List<Song> songlist;
}
