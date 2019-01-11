package classcollection.mv;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import classcollection.Singer;

public class MVInfo implements Serializable {
    public String cover_pic;
    public String desc;
    public String name;
    public String playcnt;
    public String pubdate;
    @SerializedName("singers")
    public List<Singer> singerList;
    public String vid;
}
