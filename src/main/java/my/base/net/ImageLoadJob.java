package my.base.net;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageLoadJob {
    private Bitmap bitmap;
    private ImageView imageView;
    private String imgUrl = "";
    private boolean isFinished = false;

    public String getImgUrl() {
        return this.imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ImageView getImageView() {
        return this.imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }
}
