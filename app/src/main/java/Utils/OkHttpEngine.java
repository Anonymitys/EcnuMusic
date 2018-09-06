package Utils;

import android.content.Context;
import android.os.Handler;
import android.service.carrier.CarrierMessagingService;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class OkHttpEngine {
    private static OkHttpEngine mInstance;
    private OkHttpClient client;
    private Handler mHandler;

    public static OkHttpEngine getInstance(Context context){
        if(mInstance==null){
            synchronized (OkHttpEngine.class){
                mInstance=new OkHttpEngine(context);
            }
        }
        return  mInstance;
    }

    private OkHttpEngine(Context context){
        File sdcache=context.getExternalCacheDir();
        int cacheSize=10*1024*1024;
        OkHttpClient.Builder builder=new OkHttpClient.Builder()
                .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(),cacheSize));
        client=builder.build();
        mHandler=new Handler();
    }
    public void getAsynHttp(String url, ResultCallback callback){
        final Request request=new Request.Builder()
                .url(url)
                .build();
        Call call=client.newCall(request);
        dealResult(call,callback);
    }
    public void getAsynHttp(String url,String header, ResultCallback callback){
        final Request request=new Request.Builder()
                .url(url)
                .addHeader("referer",header)
                .build();
        Call call=client.newCall(request);
        dealResult(call,callback);
    }
    public void postAsynHttp(String url, ResultCallback callback){
        String dataUrl="{\"recomPlaylist\":{\"method\":\"get_hot_recommend\",\"param\":{\"async\":1,\"cmd\":2},\"module\":\"playlist.HotRecommendServer\"},\"new_album\":{\"module\":\"music.web_album_library\",\"method\":\"get_album_by_tags\",\"param\":{\"area\":7,\"company\":-1,\"genre\":-1,\"type\":-1,\"year\":-1,\"sort\":2,\"get_tags\":1,\"sin\":0,\"num\":20,\"click_albumid\":0}},\"focus\":{\"module\":\"QQMusic.MusichallServer\",\"method\":\"GetFocus\",\"param\":{}}}";
        RequestBody requestBody=new FormBody.Builder()
                .add("g_tk","698981220")
                .add("format","jsonp")
                .add("inCharset","utf8")
                .add("outCharset","utf8")
                .add("notice","0")
                .add("platform","yqq")
                .add("needNewCode","0")
                .add("data",dataUrl)
                .build();
        Request request=new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call=client.newCall(request);

        dealResult(call,callback);
    }
    private void dealResult(Call call,final ResultCallback callback){
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedCallback(call.request(),e,callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendSuccessCallback(response,callback);
            }
        });
    }
    private void sendSuccessCallback(final Response response,final ResultCallback callback){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(callback!=null){
                    try{
                        callback.onResponse(response);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    private void sendFailedCallback(final Request request, final Exception ex, final ResultCallback callback){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(callback!=null){
                    callback.onError(request,ex);
                }
            }
        });
    }

}
