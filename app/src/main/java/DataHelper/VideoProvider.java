package DataHelper;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import bean.Video;

/**
 * Created by Admin on 2015/10/15 0015.
 */
public class VideoProvider implements AbstructProvider {

    private Context context = null;
    public VideoProvider(Context context){
       this.context = context;
    }
    @Override
    public List<?> getList() {
        List<Video> videos = null;
        if(context != null){
            Cursor cursor = context.getContentResolver().query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,null,
                    null,null
            );
            if(cursor != null){
                videos = new ArrayList<Video>();
                while (cursor.moveToNext()){
                    int id = cursor.getInt(cursor
                            .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                    String title = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                    String album = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                    String artist = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                    String displayName = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                    String mimeType = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                    String path = cursor
                            .getString(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                    long duration = cursor
                            .getInt(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                    long size = cursor
                            .getLong(cursor
                                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                    Video video = new Video(id, title,mimeType, path, album, artist, displayName, size, duration);
                    videos.add(video);
                }
                cursor.close();
            }
        }
        return videos;
    }
}
