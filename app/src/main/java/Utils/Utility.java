package Utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import classcollection.CdList;
import classcollection.SingerList;
import classcollection.Song;
import shouyeclass.PlayList;
import shouyeclass.Shouye;
import shouyeclass.singersong.Musics;


public class Utility {
    public static final String TAG="Utility";

    public static CdList handleCdlistResponse(Context context, String response) {
        int first = response.indexOf('(');
        int last = response.lastIndexOf(')');

        response = response.substring(first + 1, last);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray array = jsonObject.getJSONArray("cdlist");
            String cdlistContent = array.get(0).toString();
            return new Gson().fromJson(cdlistContent, CdList.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Shouye handleShouyeResponse(String response){
        return new Gson().fromJson(response,Shouye.class);
    }
    public static List<Song> handleAlbumResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response).getJSONObject("data");
            String albumContent=jsonObject.getJSONArray("list").toString();
            return new Gson().fromJson(albumContent, new TypeToken<List<Song>>(){}.getType());


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public static SingerList handleSingerListResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response).getJSONObject("singerList");
            String neidisingerContent=jsonObject.getJSONObject("data").toString();
            return new Gson().fromJson(neidisingerContent,SingerList.class);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public static List<PlayList> handlePlayListResponse(String response){
        int first = response.indexOf('(');
        int last = response.lastIndexOf(')');
        response = response.substring(first + 1, last);
        try{
            JSONObject jsonObject=new JSONObject(response).getJSONObject("data");
            String playlistContent=jsonObject.getJSONArray("list").toString();
            return new Gson().fromJson(playlistContent,new TypeToken<List<PlayList>>(){}.getType());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static List<Musics> handleSingerSongResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response).getJSONObject("data");
            String singersongContent=jsonObject.getJSONArray("list").toString();
            return new Gson().fromJson(singersongContent, new TypeToken<List<Musics>>(){}.getType());

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}



