package shouyeclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Content implements Serializable{
    public int id;
    @SerializedName("jump_info")
    public JumpInfo jumpInfo;
    @SerializedName("pic_info")
    public PicInfo picInfo;
}

