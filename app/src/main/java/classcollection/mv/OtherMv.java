package classcollection.mv;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import classcollection.Singer;

public class OtherMv implements Serializable {
    public String cover_pic;
    public String desc;
    public String name;
    public long playcnt;
    public long pubdate;
    @SerializedName("singers")
    public List<Singer> singers;
    public String vid;
    public String uploader_nick;


}
