package classcollection.singer;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SingerList implements Serializable {
    @SerializedName("singerlist")
    public List<SingerDetail> singerList;
    @SerializedName("tags")
    public SingerTag singerTag;
}
