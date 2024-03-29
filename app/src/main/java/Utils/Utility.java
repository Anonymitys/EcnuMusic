package Utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import classcollection.CdList;
import classcollection.mv.MVDetail;
import classcollection.mv.MVInfo;
import classcollection.mv.MvUrl;
import classcollection.mv.OtherMv;
import classcollection.singer.SingerList;
import classcollection.Song;
import classcollection.sort.Category;
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
            String singerContent=jsonObject.getJSONObject("data").toString();
            return new Gson().fromJson(singerContent,SingerList.class);

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

    public static List<Category> handleCategoryResponse(String response){
        int first = response.indexOf('(');
        int last = response.lastIndexOf(')');
        response = response.substring(first + 1, last);

        try {
            JSONObject jsonObject=new JSONObject(response).getJSONObject("data");
            JSONArray jsonArray=jsonObject.getJSONArray("categories");
            return new Gson().fromJson(jsonArray.toString(),new TypeToken<List<Category>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<MVDetail> handleMVResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response).getJSONObject("data");
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            return new Gson().fromJson(jsonArray.toString(),new TypeToken<List<MVDetail>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<MvUrl> handleMvUrlResponse(String response,String vid){
        try {
            JSONObject jsonObject=new JSONObject(response).getJSONObject("getMvUrl").getJSONObject("data").getJSONObject(vid);
            JSONArray jsonArray=jsonObject.getJSONArray("mp4");
            return new Gson().fromJson(jsonArray.toString(),new TypeToken<List<MvUrl>>(){}.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<OtherMv> handleOtherMvResponse(String response){
        try {
            JSONObject jsonObject=new JSONObject(response).getJSONObject("other").getJSONObject("data");
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            return new Gson().fromJson(jsonArray.toString(),new TypeToken<List<OtherMv>>(){}.getType());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static MVInfo handleMvInfoResponse(String response,String vid){
        try {
            JSONObject jsonObject=new JSONObject(response).getJSONObject("mvinfo").getJSONObject("data").getJSONObject(vid);
            return new Gson().fromJson(jsonObject.toString(),MVInfo.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}



