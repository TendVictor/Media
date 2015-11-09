package bean;

import java.io.Serializable;

import DataHelper.LoadedImage;

/**
 * Created by Admin on 2015/10/15 0015.
 */
public class Video implements Serializable {

    private int id;
    private String title = null;
    private String album = null;
    private String artist = null;
    private String displayname = null;
    private String mimeType = null;
    private String path = null;
    private long size;
    private long duration;
    private LoadedImage loadedImage = null;

    public Video(int id, String title, String album, String artist, String displayname, String mimeType, String path, long size, long duration) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.displayname = displayname;
        this.mimeType = mimeType;
        this.path = path;
        this.size = size;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public LoadedImage getLoadedImage() {
        return loadedImage;
    }

    public void setLoadedImage(LoadedImage loadedImage) {
        this.loadedImage = loadedImage;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
