package classcollection.sort;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {
    @SerializedName("allsorts")
    public List<Sort> sorts;
    public int categoryId=0;
    public String categoryName="";
    public int usable=0;

}
