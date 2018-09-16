package Utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import classcollection.Music;

public class MusicUtil {
    private static final Uri albumArtUri=Uri.parse("content://media/external/audio/albumart");
    public static List<Music> getMusic(final Context context) {
        List<Music> musicList=new ArrayList<>();
        FutureTask<List<Music>> task=new FutureTask<List<Music>>(new Callable<List<Music>>() {
            @Override
            public List<Music> call() throws Exception {
                List<Music> localMusics = new ArrayList<>();
                ContentResolver contentResolver = context.getContentResolver();
                Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        Music m = new Music();
                        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                        long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                        String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                        String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                        long album_id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                        int ismusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                        String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        if (duration / (500 * 60) >= 1) {
                            m.setId(id);
                            m.setTitle(title);
                            m.setArtist(artist);
                            m.setDuration(duration);
                            m.setSize(size);
                            m.setUrl(url);
                            m.setAlbum(album);
                            m.setAlbum_id(album_id);
                            m.setPath(path);
                            localMusics.add(m);
                        }
                    }
                    cursor.close();
                }
                return localMusics;
            }
        });
        new Thread(task).start();
       try{
        musicList=task.get();
       }catch (Exception e){
           e.printStackTrace();
       }
      return musicList;
    }

    public static Bitmap getAlbumBitmap(Context context, int musicPicSize, long album_id) {
        Uri uri= ContentUris.withAppendedId(albumArtUri,album_id);
        try{
            InputStream inputStream=context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream,null,options);

           // int imageWidth = options.outWidth;

           // int sample = imageWidth / musicPicSize;
           int dstSample = 1;
           // if (sample > dstSample) {
           //     dstSample = sample;
           // }
            options.inJustDecodeBounds = false;
            //设置图片采样率
            options.inSampleSize = dstSample;
            //设置图片解码格式
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            inputStream=context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream,null,options);
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }
}

