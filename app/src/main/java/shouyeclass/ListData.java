package shouyeclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import shouyeclass.Album;

public class ListData implements Serializable{
    @SerializedName("list")
    public List<Album> albumList;
}
