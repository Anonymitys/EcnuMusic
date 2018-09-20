package shouyeclass.searchsong;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import classcollection.Singer;
import shouyeclass.Album;

public class SearchSong {
    @SerializedName("album")
    public Album album;
    @SerializedName("mid")
    public String songmid;
    @SerializedName("singer")
    public List<Singer> singerList;
    public String title;
    @SerializedName("grp")
    public List<Grp> grpList;
}
