package classcollection.singer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SingerTag implements Serializable {
    @SerializedName("area")
    public List<Maps> areaList;
    @SerializedName("genre")
    public List<Maps> genreList;
    @SerializedName("sex")
    public List<Maps> sexList;
}
