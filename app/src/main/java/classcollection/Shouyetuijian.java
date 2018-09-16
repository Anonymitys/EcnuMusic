package classcollection;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Shouyetuijian implements Serializable{
    @SerializedName("focus")
    public List<Focus> focusList;
    @SerializedName("hotdiss")
    public Hotdiss hotdiss;
}
