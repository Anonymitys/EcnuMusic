package shouyeclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import classcollection.Singer;

public class Album implements Serializable{
    public long album_id;
    public long albumid;
    public String album_mid;
    public String album_name;
    public String public_time;
    public String pub_time;
    @SerializedName("singers")
    public List<Singer> singerList;
    public int week;
    public int year;
}
