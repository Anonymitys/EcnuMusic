package service;

import classcollection.Music;
import classcollection.Song;


/**
 * 播放进度监听器
 * Created by hzwangchenyan on 2015/12/17.
 */
public interface OnPlayerEventListener {

    /**
     * 切换歌曲
     */
    void onChange(Song song, Music music);

    /**
     * 继续播放
     */
    void onPlayerStart();

    /**
     * 暂停播放
     */
  //  void onPlayerPause();

    /**
     * 更新进度
     */
   // void onPublish(int progress);

    /**
     * 缓冲百分比
     */
  //  void onBufferingUpdate(int percent);
}
