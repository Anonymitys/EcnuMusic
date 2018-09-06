package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MediaPlayerService extends Service {
    public MediaPlayerService() {
    }
    private MusicPlayBinder mBinder=new MusicPlayBinder();
    @Override
    public IBinder onBind(Intent intent) {
       return mBinder;
    }
    class MusicPlayBinder extends Binder{

    }
}
