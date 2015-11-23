package bean;

import java.io.Serializable;

import DataHelper.LoadedImage;

/**
 * Created by Admin on 2015/10/15 0015.
 */
public class Video  extends MediaContainer implements Serializable{


    private LoadedImage loadedImage = null;
    public Video(int id, String title, String mimeType, String path, String album, String artist, String displayname, long size, long duration){
        super(id,title,mimeType,path,album,artist,displayname,size,duration);
    }
    public LoadedImage getLoadedImage() {
        return loadedImage;
    }
    public void setLoadedImage(LoadedImage loadedImage) {
        this.loadedImage = loadedImage;
    }
}
