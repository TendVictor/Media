package fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.MediaStore;
import android.rxy.videoplayer.VideoActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.media.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import DataHelper.LoadedImage;
import DataHelper.VideoProvider;
import adapter.VideoApapter;
import bean.Video;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView = null;
    private View rootView = null;
    private VideoApapter videoApapter = null;
    private ArrayList<Video> videos = null;
    private VideoProvider videoProvider = null;
    private OnFragmentInteractionListener mListener;
    private SwipeRefreshLayout refreshLayout = null;

    private TextView memoryTv = null;
    private ProgressBar progressBar = null;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoFragment newInstance(String param1, String param2) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public VideoFragment() {
        // Required empty public constructor

    }


    private void initView() {
        videoProvider = new VideoProvider(getActivity());
        listView = (ListView) rootView.findViewById(R.id.videos);
        videos = (ArrayList<Video>) videoProvider.getList();
        videoApapter = new VideoApapter(getActivity(), videos);
        listView.setAdapter(videoApapter);

        getSDCardMemory();
        memoryTv = (TextView) rootView.findViewById(R.id.memory);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        double a1 = result[0] * 1.0 / (1024 * 1024 * 1024);
        double a2 = result[1] * 1.0 / (1024 * 1024 * 1024);
        memoryTv.setText("Memory: " + "共有" + numberFormat.format(a1) + "GB" +
                "可用：" + numberFormat.format(a2) + "GB");
        progressBar.setProgress(100 - (int) ((a2 / a1) * 100));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction("music_service");
                getActivity().stopService(intent);
                Toast.makeText(getActivity(), videos.get(position).getPath(),
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), VideoActivity.class);
                i.putExtra("path", videos.get(position).getPath());
                getActivity().startActivity(i);
            }
        });
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_video);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                videos = null;
                videos = (ArrayList<Video>) videoProvider.getList();
                videoApapter = null;
                videoApapter = new VideoApapter(getActivity(),videos);
                listView.setAdapter(videoApapter);
                LoadImages();
            }
        });
        LoadImages();
    }

    private static long[] result = new long[2];
    private DecimalFormat numberFormat = new DecimalFormat("##.00");

    private void getSDCardMemory() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(sdCardDir.getPath());

            long bSize = statFs.getBlockSize();
            long bCount = statFs.getBlockCount();
            long availBlocks = statFs.getAvailableBlocks();

            result[0] = bSize * bCount;
            result[1] = bSize * availBlocks;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the video_list_item for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_video, container, false);
            initView();
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void LoadImages() {
        new LoadImageFromSDCard().execute();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            refreshLayout.setRefreshing(false);
        }
    };

    private void addImages() {
        videoApapter.notifyDataSetChanged();
    }

    private Bitmap getVideoThumbnail(String path, int height, int width, int kind) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(path, kind);
        bitmap = ThumbnailUtils.extractThumbnail(
                bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT
        );
        return bitmap;
    }

    class LoadImageFromSDCard extends AsyncTask<Object, LoadedImage, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            Bitmap bitmap = null;
            for (int i = 0; i < videos.size(); i++) {
                bitmap = getVideoThumbnail(videos.get(i).getPath(),
                        180, 360, MediaStore.Images.Thumbnails.MINI_KIND);
                if (bitmap != null) {
                    videos.get(i).setLoadedImage(new LoadedImage(bitmap));
                    publishProgress(new LoadedImage(bitmap));
                    if (i == videos.size() - 1) {
                        mHandler.sendEmptyMessage(0x123);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(LoadedImage... values) {
            addImages();
        }

    }
}
