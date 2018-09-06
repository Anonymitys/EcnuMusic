package Utils;

import android.content.Context;

public class MusicRequestUtil {
    public static void getDayRecommendid(Context context,ResultCallback callback){
        String url="https://u.y.qq.com/cgi-bin/musicu.fcg?g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&" +
                "data={\"recomPlaylist\":{\"method\":\"get_hot_recommend\",\"param\":{\"async\":1,\"cmd\":2},\"module\":\"playlist.HotRecommendServer\"}}";
        OkHttpEngine.getInstance(context).getAsynHttp(url,callback);
    }
    public static void getDetailMusic(Context context,String dissid,ResultCallback callback){
        String url="https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&" +
                "onlysong=0&disstid=" +dissid+ "&g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0" +
                "&platform=yqq&needNewCode=0";
        String header="https://y.qq.com/n/yqq/playsquare/"+dissid+".html";
        OkHttpEngine.getInstance(context).getAsynHttp(url,header,callback);
    }

    public static void getShouyeMusic(Context context,ResultCallback callback){
        String url="https://u.y.qq.com/cgi-bin/musicu.fcg?g_tk=698981220&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&data={\"recomPlaylist\":{\"method\":\"get_hot_recommend\",\"param\":{\"async\":1,\"cmd\":2},\"module\":\"playlist.HotRecommendServer\"},\"new_album\":{\"module\":\"music.web_album_library\",\"method\":\"get_album_by_tags\",\"param\":{\"area\":1,\"company\":-1,\"genre\":-1,\"type\":-1,\"year\":-1,\"sort\":2,\"get_tags\":1,\"sin\":0,\"num\":20,\"click_albumid\":0}},\"focus\":{\"module\":\"QQMusic.MusichallServer\",\"method\":\"GetFocus\",\"param\":{}}}";
         OkHttpEngine.getInstance(context).getAsynHttp(url,callback);

    }

    public static void getAlbumMusic(Context context,String albummid,ResultCallback callback){
        String url="https://c.y.qq.com/v8/fcg-bin/fcg_v8_album_info_cp.fcg?albummid="+albummid+"&g_tk=698981220&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
        OkHttpEngine.getInstance(context).getAsynHttp(url,callback);
    }

    public static void getPlayList(Context context,ResultCallback callback){
        String url="https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_diss_by_tag.fcg?picmid=1&rnd=0.3762366600200149&g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&categoryId=10000000&sortId=5&sin=0&ein=100";
        String header="https://y.qq.com/portal/playlist.html";
        OkHttpEngine.getInstance(context).getAsynHttp(url,header,callback);
    }
}