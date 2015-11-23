package android.rxy.videoplayer;

import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.chen.media.R;

import java.io.File;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoActivity extends AppCompatActivity implements View.OnTouchListener{
    private static final String TAG = "VideoActivity";
    /**
     * TODO: Set the path variable to a streaming video URL or a local media file
     * path.
     */
    private String path = "";
    private VideoView mVideoView = null;
    private GestureDetector mGestureDetector;


    private int ScreenWidth = 0;
    private int ScreenHeight = 0;
    private int ScreenMedium;

    private boolean IsSlectedVolume = false;

    private int mVolume = -1;
    private int maxVolume;

    private float mBrightness = -1f;

    private float px1;
    private float px2;
    private float py1;
    private float py2;

    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        getScreenSize();


        path = getIntent().getStringExtra("path");
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        pre = (Long)getLastCustomNonConfigurationInstance()==null?0:
                (Long)getLastCustomNonConfigurationInstance();


        if (path == "") {
            Toast.makeText(VideoActivity.this, "Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
            return;
        } else {
//Log.i(TAG,findSub());
            mVideoView.setVideoPath(path);
            mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
            mVideoView.setMediaController(new MediaController(this));
            mVideoView.requestFocus();

            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    // optional need Vitamio 4.0
                    mediaPlayer.setPlaybackSpeed(1.0f);
                    mediaPlayer.seekTo(pre);
                }
            });
            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Toast.makeText(VideoActivity.this, "播放完成", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void getScreenSize(){
        WindowManager wm = this.getWindowManager();
        ScreenWidth =  wm.getDefaultDisplay().getWidth();
        ScreenHeight = wm.getDefaultDisplay().getHeight();
        ScreenMedium = ScreenWidth / 2;
    }

    /*
    屏幕旋转时调用此方法
     */
     long pre = 0;
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //newConfig.orientation当前屏幕的方向
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        Long prePos = new Long(mVideoView.getCurrentPosition());
        return prePos;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVideoView != null)
        pre = mVideoView.getCurrentPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String findSub() {
        String result = null;
        File file = new File(path);
        if (file.exists()) {
            int length = file.getName().length();
            String fileName = file.getName().substring(
                    0,length-3
            );//文件名（除去文件后缀名）
            File parentFile = file.getParentFile();
            File[] subfiles = parentFile.listFiles();
            String subPath = file.getParentFile().getAbsolutePath();
            looper:for (int i = 0; i < subfiles.length; i++) {
                File tmp = subfiles[i];
                if(tmp.getName().contains(fileName)&& !tmp.getName().equals(file.getName())){
                 //包含文件名且不是本视频文件则为字幕文件
                 result = subPath+"/"+tmp.getName();
                 break looper;
                }
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            px1 = event.getX();
            py1 = event.getY();
            if(event.getX() <= ScreenMedium){
                IsSlectedVolume = true;
            }else{
                IsSlectedVolume = false;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
//            px2 = event.getX();
//            py2 = event.getY();
//            float distance =  py1 - py2;
//
//            if(IsSlectedVolume)
//                VolumneChanged(distance / ScreenHeight);
//            else
//                BrightnessChanged(distance / ScreenHeight);
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            px2 = event.getX();
            py2 = event.getY();
            float distance =  py1 - py2;

            if(IsSlectedVolume)
                VolumneChanged(distance / ScreenHeight);
            else
                BrightnessChanged(distance / ScreenHeight);
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            px1 = event.getX();
            py1 = event.getY();
            if(event.getX() <= ScreenMedium){
                IsSlectedVolume = true;
            }else{
                IsSlectedVolume = false;
            }
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            px2 = event.getX();
            py2 = event.getY();
            float distance =  py1 - py2;

            if(IsSlectedVolume)
                VolumneChanged(distance / ScreenHeight);
            else
                BrightnessChanged(distance / ScreenHeight);
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
        }
        super.onTouchEvent(event);
        return true;
    }

        private void VolumneChanged(float percent){
            System.out.println(percent + " @@@@@@@@@@@@@@@@@@ " + ScreenHeight);
            if(mVolume == -1){
                mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if(mVolume < 0)
                mVolume = 0;
            }
            System.out.println(mVolume +" ^^^^^^^^^");

            int index =(int) (percent * maxVolume) ;//+ mVolume;
            if(index > maxVolume)
                index = maxVolume;
            else if(index < 0)
                index = 0;
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index , AudioManager.FLAG_SHOW_UI);
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }

        private void BrightnessChanged(float percent){
            if(mBrightness < 0){
                mBrightness = getWindow().getAttributes().screenBrightness;
                if (mBrightness <= 0.00f)
                    mBrightness = 0.50f;
                if (mBrightness < 0.01f)
                    mBrightness = 0.01f;
            }

            WindowManager.LayoutParams lpa = getWindow().getAttributes();
            lpa.screenBrightness = mBrightness + percent;
            if(lpa.screenBrightness > 1.0f)
                lpa.screenBrightness = 1.0f;
            else if(lpa.screenBrightness < 0.01f)
                lpa.screenBrightness = 0.01f;

            getWindow().setAttributes(lpa);
            mBrightness = getWindow().getAttributes().screenBrightness;
        }
}
