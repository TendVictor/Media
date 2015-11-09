package DataHelper;

import android.graphics.Bitmap;

/**
 * Created by Admin on 2015/10/15 0015.
 */
public class LoadedImage {
    private Bitmap bitmap = null;

    public LoadedImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
