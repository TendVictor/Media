package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.chen.media.R;
import java.util.List;
import bean.Music;

/**
 * Created by Ran on 2015/11/11 0011.
 */
public class MusicAdapter extends BaseAdapter {
    private Context context = null;
    private List<Music> musics = null;

    public MusicAdapter(Context context, List<Music> musics) {
        this.context = context;
        this.musics = musics;
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
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
                    R.layout.music_list_item,null
            );
            holder = new ViewHolder();
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            holder.title = title;
            holder.time = time;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(musics.get(position).getTitle());
        holder.time.setText(musics.get(position).getDuaration()/1000 + "s");

        return convertView;
    }

    class ViewHolder{
        TextView title = null;
        TextView time = null;
    }
}
