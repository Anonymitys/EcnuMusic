package Utils;

import android.content.Context;

public class MusicRequestUtil {
    public static void getDayRecommendid(Context context, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&" +
                "data={\"recomPlaylist\":{\"method\":\"get_hot_recommend\",\"param\":{\"async\":1,\"cmd\":2},\"module\":\"playlist.HotRecommendServer\"}}";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getDetailMusic(Context context, String dissid, ResultCallback callback) {
        /*String url = "https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&" +
                "onlysong=0&disstid=" + dissid + "&g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0" +
                "&platform=yqq&needNewCode=0";*/
        String url = "https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&" +
                "onlysong=0&disstid=" + dissid + "";
        String header = "https://y.qq.com/n/yqq/playsquare/" + dissid + ".html";
        OkHttpEngine.getInstance(context).getAsynHttp(url, header, callback);
    }

    public static void getShouyeMusic(Context context, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?g_tk=698981220&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&data={\"recomPlaylist\":{\"method\":\"get_hot_recommend\",\"param\":{\"async\":1,\"cmd\":2},\"module\":\"playlist.HotRecommendServer\"},\"new_album\":{\"module\":\"music.web_album_library\",\"method\":\"get_album_by_tags\",\"param\":{\"area\":1,\"company\":-1,\"genre\":-1,\"type\":-1,\"year\":-1,\"sort\":2,\"get_tags\":1,\"sin\":0,\"num\":20,\"click_albumid\":0}},\"focus\":{\"module\":\"QQMusic.MusichallServer\",\"method\":\"GetFocus\",\"param\":{}}}";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);

    }

    public static void getAlbumMusic(Context context, String albummid, ResultCallback callback) {
        String url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_album_info_cp.fcg?albummid=" + albummid + "&g_tk=698981220&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getPlayList(int count, Context context, int categoryId, int sortId, ResultCallback callback) {
        int start = 80 * count;
        int end = 80 * (count + 1) - 1;
        String url = "https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_diss_by_tag.fcg?picmid=1&rnd=0.3762366600200149&g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&categoryId=" + categoryId + "&sortId=" + sortId + "&sin=" + start + "&ein=" + end;
        String header = "https://y.qq.com/portal/playlist.html";
        OkHttpEngine.getInstance(context).getAsynHttp(url, header, callback);
    }

    public static void getSinger(Context context, int area, int genre, int sex, int count, ResultCallback callback) {
        int start = count * 80;
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?" +
                "data={\"singerList\":{\"module\":\"Music.SingerListServer\",\"method\":\"get_singer_list\",\"param\":{\"area\":" + area + ",\"sex\":" + sex + ",\"genre\":" + genre + ",\"index\":-100,\"sin\":" + start + "}}}";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);

    }

    public static void getGantaiSinger(Context context, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?&g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&data=%7B%22comm%22%3A%7B%22ct%" +
                "22%3A24%2C%22cv%22%3A10000%7D%2C%22singerList%22%3A%7B%22module%22%3A%22Music.SingerListServer%22%2C%22method%22%3A%22get_singer_list%22%2C%22" +
                "param%22%3A%7B%22area%22%3A2%2C%22sex%22%3A-100%2C%22genre%22%3A-100%2C%22index%22%3A-100%2C%22sin%22%3A0%2C%22cur_page%22%3A1%7D%7D%7D";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getKoreaSinger(Context context, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?&g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8" +
                "&notice=0&platform=yqq&needNewCode=0&data=%7B%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A10000%7D%2C%22" +
                "singerList%22%3A%7B%22module%22%3A%22Music.SingerListServer%22%2C%22method%22%3A%22get_singer_list%22%2C%22" +
                "param%22%3A%7B%22area%22%3A3%2C%22sex%22%3A-100%2C%22genre%22%3A-100%2C%22index%22%3A-100%2C%22sin%22%3A0%2C%22" +
                "cur_page%22%3A1%7D%7D%7D";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getAmericaSinger(Context context, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?&g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8" +
                "&notice=0&platform=yqq&needNewCode=0&data=%7B%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A10000%7D%2C%22" +
                "singerList%22%3A%7B%22module%22%3A%22Music.SingerListServer%22%2C%22method%22%3A%22" +
                "get_singer_list%22%2C%22param%22%3A%7B%22area%22%3A5%2C%22sex%22%3A-100%2C%22" +
                "genre%22%3A-100%2C%22index%22%3A-100%2C%22sin%22%3A0%2C%22cur_page%22%3A1%7D%7D%7D";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getJapanSinger(Context context, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?&g_tk=5381&format=jsonp&inCharset=utf8&outCharset=utf-8&" +
                "notice=0&platform=yqq&needNewCode=0&data=%7B%22comm%22%3A%7B%22ct%22%3A24%2C%22cv%22%3A10000%7D%2C%22" +
                "singerList%22%3A%7B%22module%22%3A%22Music.SingerListServer%22%2C%22method%22%3A%22get_singer_list%22%2C%22" +
                "param%22%3A%7B%22area%22%3A4%2C%22sex%22%3A-100%2C%22genre%22%3A-100%2C%22index%22%3A-100%2C%22sin%22%3A0%2C%22" +
                "cur_page%22%3A1%7D%7D%7D";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getSingerSong(Context context, String singermid, ResultCallback callback) {
        String url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_singer_track_cp.fcg?g_tk=1878528355&format=jsonp&inCharset=utf8" +
                "&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&singermid=" + singermid + "&order=listen&begin=0&num=200&songstatus=1";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getSingerAlbum(Context context, String singermid, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?&g_tk=1878528355&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0" +
                "&platform=yqq&needNewCode=0&data={\"singerAlbum\":{\"method\":\"get_singer_album\",\"param\":{\"singermid\":\"" + singermid + "\",\"order\":" +
                "\"time\",\"begin\":0,\"num\":100,\"exstatus\":1},\"module\":\"music.web_singer_info_svr\"}}";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getRank(Context context, ResultCallback callback) {
        String url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_opt.fcg?page=index&format=html&tpl=macv4&v8debug=1";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getRankSong(Context context, int topID, ResultCallback callback) {
        String url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?topid=" + topID;
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getSearchSong(Context context, String keyword, ResultCallback callback) {
        String url = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=txt.yqq.center&" +
                "searchid=37602803789127241&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&p=1&n=100&w=" + keyword + "&g_tk=5381&format=json&inCharset=utf8" +
                "&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getvkey(Context context, String songmid, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?data={\"req\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"7758618800\",\"songmid\":[\"" + songmid + "\"],\"uin\":\"0\"}}}";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getCategory(Context context, ResultCallback callback) {
        String url = "https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_diss_tag_conf.fcg";
        String header = "https://y.qq.com/portal/playlist.html";
        OkHttpEngine.getInstance(context).getAsynHttp(url, header, callback);
    }

    public static void getMv(Context context, String singerMid, int count, ResultCallback callback) {
        int start = count * 100;
        String url = "https://c.y.qq.com/mv/fcgi-bin/fcg_singer_mv.fcg?cid=205360581&singermid=" + singerMid + "&order=listen&begin=" + start + "&num=100";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }

    public static void getMvurl(Context context, String vid, ResultCallback callback) {
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?data={\"getMvUrl\":{\"module\":\"gosrf.Stream.MvUrlProxy\",\"method\":\"GetMvUrls\",\"param\":{\"vids\":[\""+vid+"\"],\"request_typet\":10001}}}";
        OkHttpEngine.getInstance(context).getAsynHttp(url, callback);
    }
    public static void getMvInfo(Context context,String vid,ResultCallback callback){
        String url="https://u.y.qq.com/cgi-bin/musicu.fcg?data={\"mvinfo\":{\"module\":\"video.VideoDataServer\",\"method\":\"get_video_info_batch\",\"param\":{\"vidlist\":[\""+vid+"\"],\"required\":[\"vid\",\"type\",\"sid\",\"cover_pic\",\"duration\",\"singers\",\"video_switch\",\"msg\",\"name\",\"desc\",\"playcnt\",\"pubdate\",\"isfav\",\"gmid\"]}}," +
                "\"other\":{\"module\":\"video.VideoLogicServer\",\"method\":\"rec_video_byvid\",\"param\":{\"vid\":\""+vid+"\",\"required\":[\"vid\",\"type\",\"sid\",\"cover_pic\",\"duration\",\"singers\",\"video_switch\",\"msg\",\"name\",\"desc\",\"playcnt\",\"pubdate\",\"isfav\",\"gmid\",\"uploader_headurl\",\"uploader_nick\",\"uploader_encuin\",\"uploader_uin\",\"uploader_hasfollow\",\"uploader_follower_num\"],\"support\":1}}}";
        OkHttpEngine.getInstance(context).getAsynHttp(url,callback);
    }
}
