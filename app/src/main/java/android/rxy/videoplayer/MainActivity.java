package android.rxy.videoplayer;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.media.R;

import fragments.MusicFragment;
import fragments.VideoFragment;
import io.vov.vitamio.LibsChecker;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, MusicFragment.OnFragmentInteractionListener
        , VideoFragment.OnFragmentInteractionListener {


    private Toolbar mToolbar = null;
    private DrawerLayout drawerLayout = null;
    private ActionBarDrawerToggle actionBarDrawerToggle = null;

    private TextView musicTv = null;
    private TextView videoTv = null;
    private TextView mTv_c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ////
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        ////

        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                mToolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        musicTv = (TextView) findViewById(R.id.music);
        videoTv = (TextView) findViewById(R.id.video);
        musicTv.setOnClickListener(this);
        videoTv.setOnClickListener(this);
        if (videofragment == null)
            videofragment = new VideoFragment();
        getFragmentManager().beginTransaction().replace(
                R.id.content, videofragment
        ).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:

                break;
            case R.id.action_settings:
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private static VideoFragment videofragment = null;
    private static MusicFragment musicFragment = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.music:
                Toast.makeText(this, "music", Toast.LENGTH_SHORT).show();
                if (musicFragment == null)
                    musicFragment = new MusicFragment();
                getFragmentManager().beginTransaction().replace(
                        R.id.content, musicFragment
                ).commit();
                break;
            case R.id.video:
                Toast.makeText(this, "video", Toast.LENGTH_SHORT).show();
                if (videofragment == null)
                    videofragment = new VideoFragment();
                getFragmentManager().beginTransaction().replace(
                        R.id.content, videofragment
                ).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
