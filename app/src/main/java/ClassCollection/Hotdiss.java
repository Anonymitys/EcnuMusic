package ClassCollection;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Hotdiss implements Serializable{
    @SerializedName("list")
    public List<RecommendList> hotdissList;
    public int sum;
}
