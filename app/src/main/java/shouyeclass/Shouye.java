package shouyeclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shouye implements Serializable{
    @SerializedName("focus")
    public FocusData focusData;
    @SerializedName("new_album")
    public AlbumData albumData;
    @SerializedName("recomPlaylist")
    public RecomData recomData;
}
