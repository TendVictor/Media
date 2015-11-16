package services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.rxy.videoplayer.MusicActivity;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import DataHelper.MusicProvider;
import bean.Constant;
import bean.Music;

/**
 * Created by chen on 15/11/12.
 */
public class MusicService extends Service {

    private MediaPlayer musicPlayer;
    private MusicProvider musicProvider;
    private ArrayList<Music> musics;
    private String path;
    private boolean isPause;
    private int duration;
    private int status = 3;//默认顺序播放

    private int msg;
    private int current;//当前歌曲第几首

    private int currentTime;//当前歌曲播放进度

    private MyReceiver myReceiver;

    public static final String UPDATE_ACTION = "action.UPDATE_ACTION";//更新动作
    public static final String CTL_ACTION = "action.CTL_ACTION";//控制动作
    public static final String MUSIC_CURRENT = "action.MUSIC_CURRENT";//当前音乐播放时间动作
    public static final String MUSIC_DURATION = "action.MUSIC_DURATION";//新音乐长度更新动作

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (musicPlayer != null) {
                    currentTime = musicPlayer.getCurrentPosition();
                    Intent intent = new Intent();
                    intent.putExtra("currentTime",currentTime);
                    intent.setAction(MUSIC_CURRENT);
                    sendBroadcast(intent);//给MusicActiivty发送广播
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "service created");
        musicPlayer = new MediaPlayer();
        musicProvider = new MusicProvider(this);
        musics = (ArrayList<Music>) musicProvider.getList();

        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (status == 1) {//单曲循环
                    musicPlayer.start();
                } else if (status == 2) {
                    current++;
                    if (current > musics.size() - 1) {
                        current = 0;
                    }
                    Intent sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current", current);
                    sendBroadcast(sendIntent);
                    path = musics.get(current).getPath();
                    play(0);
                } else if (status == 3) {//顺序播放(不循环）
                    current++;
                    if (current <= musics.size() - 1) {
                        Intent sendIntent = new Intent(UPDATE_ACTION);
                        sendIntent.putExtra("current", current);
                        sendBroadcast(sendIntent);
                        path = musics.get(current).getPath();
                        play(0);
                    } else {
                        musicPlayer.seekTo(0);
                        current = 0;
                        Intent sendIntent = new Intent(UPDATE_ACTION);
                        sendIntent.putExtra("current", current);
                        sendBroadcast(sendIntent);
                    }
                } else if (status == 4) {//随机
                    current = getRandomIndex(musics.size() - 1);
                    Intent sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current", current);
                    sendBroadcast(sendIntent);
                    path = musics.get(current).getPath();
                    play(0);
                }
            }
        });
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MusicActivity.CTL_ACTION);
        registerReceiver(myReceiver, filter);

    }

    private int getRandomIndex(int end) {
        int index = (int) (Math.random() * end);
        return index;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        path = intent.getStringExtra("path");
        current = intent.getIntExtra("position", -1);
        msg = intent.getIntExtra("MSG", 0);
        System.out.println("position : " + current +  " path is : " + path +" MSG : " + msg);
        if (msg == Constant.PLAY_MSG) {
            play(0);
        } else if (msg == Constant.PAUSE_MSG) {
            pause();
        } else if (msg == Constant.STOP_MSG) {
            stop();
        } else if (msg == Constant.CONTINUE_MSG) {
            resume();
        } else if (msg == Constant.PREVIOUS_MSG) {
            previous();
        } else if (msg == Constant.NEXT_MSG) {
            next();
        } else if (msg == Constant.PROGRASS_MSG) {//进度跟新
            currentTime = intent.getIntExtra("Progress", -1);
            play(currentTime);
        } else if (msg == Constant.PLAYING_MSG) {
            handler.sendEmptyMessage(1);
        }
        super.onStart(intent, startId);
    }

    private void play(int currentTime) {
        try {
            musicPlayer.reset();
            musicPlayer.setDataSource(path);
            musicPlayer.setOnPreparedListener(new PreparedListener(currentTime));
            musicPlayer.prepare();
            handler.sendEmptyMessage(1);
            System.out.println(" handler send!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pause() {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.pause();
            isPause = true;
        }
    }

    private void resume() {
        if (isPause) {
            musicPlayer.start();
            isPause = false;
        }
    }

    private void previous() {
        Intent sendIntent = new Intent(UPDATE_ACTION);
        sendIntent.putExtra("current", current);
        sendBroadcast(sendIntent);
        play(0);
    }

    private void next() {
        Intent sendIntent = new Intent(UPDATE_ACTION);
        sendIntent.putExtra("current", current);
        sendBroadcast(sendIntent);
        play(0);
    }

    private void stop() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            try {
                musicPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
        }
    }

    private final class PreparedListener implements android.media.MediaPlayer.OnPreparedListener {

        private int currentTime;

        public PreparedListener(int currentTime) {
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(android.media.MediaPlayer mp) {
            musicPlayer.start();
            if (currentTime > 0) {
                System.out.println("拖动");
                musicPlayer.seekTo(currentTime);
            }
            Intent intent = new Intent();
            intent.setAction(MUSIC_DURATION);
            duration = (int) musicPlayer.getDuration();
            intent.putExtra("duration", duration);
            sendBroadcast(intent);
        }
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("control", -1);
            switch (control) {
                case 1:
                    status = 1;//单曲循环
                    break;
                case 2:
                    status = 2;//全部循环
                    break;
                case 3:
                    status = 3;//顺序(不循环）
                    break;
                case 4:
                    status = 4;//随机
                    break;
            }
            System.out.println(status);
        }
    }
}
