package shouyeclass.rank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Rank {
    @SerializedName("GroupName")
    public String groupname;
    @SerializedName("List")
    public List<RankName> rankNameList;
}
