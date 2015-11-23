package android.rxy.videoplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.media.R;

import java.util.ArrayList;

import DataHelper.MusicProvider;
import bean.Constant;
import bean.Music;
import services.MusicService;
import view.LyricView;

/**
 * Created by chen on 15/11/12.
 */
public class MusicActivity extends Activity implements View.OnClickListener{

    private String path;//歌曲路径
    private int position;//歌曲在musics的位置
    private int currentTime;//歌曲的当前时间
    private long duration;//歌曲现在的间隔
    private int flag;//播放标志

    private Button shuffleBtn,repeatBtn;
    private Button playBtn,previousBtn, nextBtn;
    private SeekBar musicProgress;

    private ArrayList<Music> musics;
    private MusicProvider musicProvider;


    private int repeatState = 2;
    private final int isSingalRepeat = 1;
    private final int isCircleRepeat = 2;
    private final int isNoneCircleRepeat = 3;
    private boolean isPlaying;//正在播放
    private boolean isPause;//暂停
    private boolean isOrdered;//顺序播放
    private boolean isShuffled;//随机播放

    private TextView tv_current,tv_duration,tv_musictitle;

    public static LyricView lyricView;

    private PlayerRecevier myPlayerRecevier;


    public static final String UPDATE_ACTION = "action.UPDATE_ACTION";//更新动作
    public static final String CTL_ACTION = "action.CTL_ACTION";//控制动作
    public static final String MUSIC_CURRENT = "action.MUSIC_CURRENT";//音乐当前shi时间改变动作
    public static final String MUSIC_PLAYING = "action.MUSIC_PLAYING";//音乐正在播放动作
    public static final String MUSIC_DURATION = "action.MUSIC_DURATION";//音乐播放长度动作
    public static final String REPEAT_ACTION = "action.REPEAT_ACTION";//音乐重复播放动作
    public static final String SHUFFLE_ACTION = "action.SHUFFLE_ACTION";//音乐随机播放动作

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        myPlayerRecevier = new PlayerRecevier();

        Intent intent1 = getIntent();
        position = intent1.getIntExtra("position",-1);
System.out.println("position is :" + position);
        initView();
        initService();

        tv_musictitle.setText(musics.get(position).getTitle());

        path = musics.get(position).getPath();
        duration = musics.get(position).getDuration();


        setAllUi();

        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_ACTION);
        filter.addAction(MUSIC_CURRENT);
        filter.addAction(MUSIC_DURATION);
        registerReceiver(myPlayerRecevier, filter);



        playBtn.performClick();
        isPlaying = true;
        isPause = false;
        System.out.println("onCreate Done!!!");
    }

    private void initService() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("path",musics.get(position).getPath());
        intent.putExtra("MSG", Constant.PLAY_MSG);
        intent.setClass(this, MusicService.class);
        startService(intent);
System.out.println("Service Start!!");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        flag = bundle.getInt("Msg");
//        repeatState = bundle.getInt("repeatState");
        position = bundle.getInt("position");
        isShuffled = bundle.getBoolean("ShuffleState");
        path = bundle.getString("path");
        currentTime = bundle.getInt("currentTime");
        duration = bundle.getInt("duration");

    }

    private void setAllUi(){
        musicProgress.setProgress(currentTime);
        musicProgress.setMax((int) duration);
        switch(repeatState){
            case isSingalRepeat://单曲循环
                shuffleBtn.setClickable(true);
                break;
            case isCircleRepeat://循环播放
                shuffleBtn.setClickable(true);
                break;
            case isNoneCircleRepeat://无重复
                shuffleBtn.setClickable(true);
                break;
        }
        if(isShuffled){
            isOrdered = false;
            repeatBtn.setClickable(false);
        }else{
            isOrdered = true;
            repeatBtn.setClickable(true);
        }
        if(flag == Constant.PLAYING_MSG){
            Toast.makeText(this, "正在播放" ,Toast.LENGTH_SHORT).show();
        }
        else if(flag == Constant.PLAY_MSG){
            play();
        }
    }

    private void play() {
        //默认顺序播放
        repeatMode(Constant.ONE_ORDER_REPEAT);
        Intent intent = new Intent();
        intent.setAction("music_service");
        intent.putExtra("path", path);
        intent.putExtra("position", position);
        intent.putExtra("MSG", flag);
        startService(intent);
    }

    private void initView() {
        musicProgress = (SeekBar) findViewById(R.id.sb_musicbar);
        playBtn = (Button) findViewById(R.id.bt_musicplay);
        previousBtn = (Button) findViewById(R.id.bt_musicprevious);
        nextBtn = (Button) findViewById(R.id.bt_musicnext);
        shuffleBtn = (Button) findViewById(R.id.btn_shuffle);
        repeatBtn = (Button)findViewById(R.id.btn_repeat);
        tv_duration = (TextView)findViewById(R.id.tv_duration);
        tv_current = (TextView) findViewById(R.id.tv_current);
        tv_musictitle = (TextView) findViewById(R.id.tv_music_title);
        lyricView  = (LyricView) findViewById(R.id.tv_lyricview);

        musicProvider = new MusicProvider(this);
        musics = (ArrayList<Music>) musicProvider.getList();

        playBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        shuffleBtn.setOnClickListener(this);
        repeatBtn.setOnClickListener(this);

        musicProgress.setOnSeekBarChangeListener(new seekBarChangeListener());
    }

    @Override
    protected void onStop(){
        super.onStop();
        unregisterReceiver(myPlayerRecevier);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch(v.getId()){
            case R.id.bt_musicplay:
                if(isPlaying){
                    intent.setAction("music_service");
                    intent.putExtra("MSG", Constant.PAUSE_MSG);
                    startService(intent);
                    isPlaying = false;
                    isPause = true;
                    playBtn.setBackgroundResource(R.drawable.bg_btn_play);
                }else if(isPause){
                    intent.setAction("music_service");
                    intent.putExtra("MSG", Constant.CONTINUE_MSG);
                    startService(intent);
                    isPause = false;
                    isPlaying = true;
                    playBtn.setBackgroundResource(R.drawable.bg_btn_pause);
                }
                break;
            case R.id.bt_musicprevious:
                previousMusic();
                break;
            case R.id.bt_musicnext:
                nextMusic();
                break;
            case R.id.btn_repeat:
                System.out.println("btn_repeat " + repeatState);
                ModifyRepeatNumber(repeatState);
                Intent repeatIntent = new Intent(REPEAT_ACTION);
                SendRepeatModeToService(repeatState, repeatIntent);
                break;
            case R.id.btn_shuffle:
                System.out.println("btn_shuffle");
                Intent shuffleIntent = new Intent(SHUFFLE_ACTION);
                if(isOrdered){
                    shuffleBtn.setBackgroundResource(R.drawable.bg_btn_shuffle);
                    System.out.println("IsOrdered Changed to Shuffled");
                    isOrdered = false;
                    isShuffled = true;
                    repeatMode(Constant.SHUTTLE);
                    shuffleIntent.putExtra("shuffleState", true);
                    sendBroadcast(shuffleIntent);
                }
                else if(isShuffled){
                    shuffleBtn.setBackgroundResource(R.drawable.bg_btn_shufflenone);
                    System.out.println("IsShuffledChanged to Ordered");
                    isShuffled = false;
                    isOrdered = true;
                    shuffleIntent.putExtra("shuffleState",false);
                    sendBroadcast(shuffleIntent);
                }
                break;
        }
    }

    private void ModifyRepeatNumber(int state){
        switch(state){
            case isNoneCircleRepeat:
                repeatMode(Constant.SINGLE_REPEAT);
                repeatState = isSingalRepeat;
                break;
            case isSingalRepeat:
                repeatMode(Constant.CIRCLE_REPEAT);
                repeatState = isCircleRepeat;
                break;
            case isCircleRepeat:
                repeatMode(Constant.ONE_ORDER_REPEAT);
                repeatState = isNoneCircleRepeat;
                break;
        }
    }

    private void SendRepeatModeToService(int state, Intent repeatIntent){
        switch (state){
            case isSingalRepeat:
                repeatBtn.setBackgroundResource(R.drawable.bg_btn_repeatcurrent);
                repeatIntent.putExtra("repeatState", isSingalRepeat);
                sendBroadcast(repeatIntent);
                break;
            case isCircleRepeat:
                repeatBtn.setBackgroundResource(R.drawable.bg_btn_repeatall);
                repeatIntent.putExtra("repeatState", isCircleRepeat);
                sendBroadcast(repeatIntent);
                break;
            case isNoneCircleRepeat:
                repeatBtn.setBackgroundResource(R.drawable.bg_btn_repeatnone);
                repeatIntent.putExtra("repeatState", isNoneCircleRepeat);
                sendBroadcast(repeatIntent);
                break;
        }
    }

    private void repeatMode(int mode){
        Intent intent = new Intent(CTL_ACTION);
        intent.putExtra("control", mode);
        sendBroadcast(intent);
    }

    private void audioProgressChange(int progress){
        Intent intent = new Intent();
        intent.setAction("music_service");
        intent.putExtra("position", position);
        intent.putExtra("path", path);
        System.out.println("isPause : " + isPause);
        if(isPause){
            intent.putExtra("MSG",Constant.PAUSE_MSG);
        }
        else{
            intent.putExtra("MSG",Constant.PROGRASS_MSG);
        }
        intent.putExtra("Progress", progress);
        startService(intent);
    }

    private void nextMusic() {
        playBtn.setBackgroundResource(R.drawable.bg_btn_pause);
        isPause = false;
        isPlaying = true;


        if(isShuffled)
            position = getRandomIndex(musics.size()-1);
        else{
            if(repeatState != isSingalRepeat)
                position++;
        }
        if(position > musics.size()-1)
            position = 0;
        path = musics.get(position).getPath();
        Intent intent = new Intent();
        intent.setAction("music_service");
        intent.putExtra("path",path);
        intent.putExtra("position", position);
        intent.putExtra("MSG",Constant.NEXT_MSG);
        startService(intent);
    }

    private void previousMusic() {
        playBtn.setBackgroundResource(R.drawable.bg_btn_pause);
        isPause = false;
        isPlaying = true;
        if(isShuffled)
            position = getRandomIndex(musics.size()-1);
        else{
            if(repeatState != isSingalRepeat)
                position--;
        }
        if(position < 0)
            position = musics.size()-1;
        path = musics.get(position).getPath();
        Intent intent = new Intent();
        intent.setAction("music_service");
        intent.putExtra("path", path);
        intent.putExtra("position",position);
        intent.putExtra("MSG",Constant.PREVIOUS_MSG);
        startService(intent);
    }

    //随机选取歌曲
    private int getRandomIndex(int end) {
        int index = (int) (Math.random() * end);
        return index;
    }

    public String formatTime(long time){
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }

    public class PlayerRecevier extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MUSIC_CURRENT)){
                currentTime = intent.getIntExtra("currentTime" , -1);
                musicProgress.setProgress(currentTime);
                tv_current.setText(formatTime(currentTime));
            }else if(action.equals(MUSIC_DURATION)){
                duration = intent.getIntExtra("duration", -1);
                musicProgress.setMax((int) duration);
                tv_duration.setText(formatTime(duration));
            }else if(action.equals(UPDATE_ACTION)){
                position = intent.getIntExtra("current", -1);
                path = musics.get(position).getPath();
                tv_musictitle.setText(musics.get(position).getTitle());
            }
        }
    }

    private class seekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                audioProgressChange(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}
