package bean;

/**
 * Created by Admin on 2015/11/10 0010.
 */
public class Music  extends  MediaContainer{
  public Music(int id, String title, String mimeType, String path, String album, String artist, String displayname, long size, long duration){
      super(id,title,mimeType,path,album,artist,displayname,size,duration);
  }
}
