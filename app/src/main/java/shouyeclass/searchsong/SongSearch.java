package shouyeclass.searchsong;

import com.google.gson.annotations.SerializedName;

public class SongSearch {
    @SerializedName("song")
    public Search search;
    @SerializedName("zhida")
    public Zhidao zhidao;
}
