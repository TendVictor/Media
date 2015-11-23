package bean;

/**
 * Created by Admin on 2015/11/23 0023.
 */
public class MediaContainer {
    protected int id;
    protected String title = null;
    protected String mimeType = null;
    protected String path = null;
    protected String album = null;
    protected String artist = null;
    protected String displayname = null;
    protected long size;
    protected long duration;

    public MediaContainer(int id, String title, String mimeType, String path, String album, String artist, String displayname, long size, long duration) {
        this.id = id;
        this.title = title;
        this.mimeType = mimeType;
        this.path = path;
        this.album = album;
        this.artist = artist;
        this.displayname = displayname;
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

    public String getAlbum() {
        return album;
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
