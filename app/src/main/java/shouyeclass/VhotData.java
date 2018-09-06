package shouyeclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VhotData implements Serializable{
    @SerializedName("v_hot")
    public List<Vhot> vhotList;
}
