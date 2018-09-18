package shouyeclass.rank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RankName {
    @SerializedName("ListName")
    public String listName;
    public long listennum;
    @SerializedName("pic_v12")
    public String rankIcon;
    public String showtime;
    @SerializedName("songlist")
    public List<RankSong> rankSongList;
    public int topID;
    public String update_key;
}
