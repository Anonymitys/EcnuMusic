package classcollection.sort;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Category {
    public String categoryGroupName="";
    @SerializedName("items")
    public List<Item> itemList;
    public int usable=0;
}
