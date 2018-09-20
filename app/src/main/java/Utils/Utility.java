package Utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import classcollection.CdList;
import classcollection.SingerList;
import classcollection.Song;
import shouyeclass.Album;
import shouyeclass.PlayList;
import shouyeclass.Shouye;
import shouyeclass.rank.Rank;
import shouyeclass.rank.SongData;
import shouyeclass.searchsong.SongSearch;
import shouyeclass.singersong.Musics;


public class Utility {
    public static final String TAG="Utility";

    public static CdList handleCdlistResponse( String response) {
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

    public static List<Album> handleSingerAlbumResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response).getJSONObject("singerAlbum").getJSONObject("data");
            String singerAlbumContent=jsonObject.getJSONArray("list").toString();
            return new Gson().fromJson(singerAlbumContent,new TypeToken<List<Album>>(){}.getType());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
    public static List<Rank> handleRankResponse(String response){
        int first = response.indexOf('(');
        int last = response.lastIndexOf(')');

        response = response.substring(first + 1, last);
        try {
            JSONArray array = new JSONArray(response);
            String rankContent = array.toString();
            return new Gson().fromJson(rankContent,new TypeToken<List<Rank>>(){}.getType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static List<SongData> handleRankSongResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("songlist");
            return new Gson().fromJson(jsonArray.toString(),new TypeToken<List<SongData>>(){}.getType());
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static SongSearch handleSongSearchResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response).getJSONObject("data");
            return new Gson().fromJson(jsonObject.toString(),SongSearch.class);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }
}



