package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.media.R;

import java.util.List;

import bean.Video;

/**
 * Created by Admin on 2015/10/15 0015.
 */
public class VideoApapter extends BaseAdapter {
    private Context context = null;
    private List<Video> videos = null;

    public VideoApapter(Context context, List<Video> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
           convertView = LayoutInflater.from(context).inflate(
                   R.layout.video_list_item,null
           );
           holder = new ViewHolder();
           TextView title = (TextView) convertView.findViewById(R.id.title);
           TextView time = (TextView) convertView.findViewById(R.id.time);
           ImageView img = (ImageView) convertView.findViewById(R.id.img);
           holder.title = title;
           holder.time = time;
           holder.imageView = img;
           convertView.setTag(holder);
        }else{
           holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(videos.get(position).getTitle());
        holder.time.setText(videos.get(position).getDuration()/1000 + "s");

        Bitmap bitmap = null;
        if(videos.get(position).getLoadedImage()!=null){
            bitmap = videos.get(position).getLoadedImage().getBitmap();
        }
        if(bitmap==null)
        holder.imageView.setBackgroundResource(R.mipmap.ic_launcher);
        else{
            holder.imageView.setBackground(null);
            holder.imageView.setImageBitmap(bitmap);
        }
        return convertView;
    }

    class ViewHolder{
        TextView title = null;
        TextView time = null;
        ImageView imageView = null;
    }
}
