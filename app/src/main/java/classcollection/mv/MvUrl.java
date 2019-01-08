package classcollection.mv;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MvUrl implements Serializable {
    @SerializedName("freeflow_url")
    public List<String> mvurlList;
}
