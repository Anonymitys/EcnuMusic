package shouyeclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ContentData implements Serializable{
    @SerializedName("content")
    public List<Content> contentList;

}
