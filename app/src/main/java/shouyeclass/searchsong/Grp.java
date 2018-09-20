package shouyeclass.searchsong;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import classcollection.Singer;
import shouyeclass.Album;

public class Grp {
    @SerializedName("album")
    public Album album;
    @SerializedName("mid")
    public String songmid;
    public String title;
    @SerializedName("singer")
    public List<Singer> singerList;

}
