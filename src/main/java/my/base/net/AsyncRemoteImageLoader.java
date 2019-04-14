package my.base.net;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class AsyncRemoteImageLoader extends AsyncTask<String, Integer, Bitmap> {
    private static final String tag = "AsyncRemoteImageLoader";
    private Map<String, SoftReference<Bitmap>> imageCache = new HashMap();
    private ImageLoadJob imageLoadJob;

    public AsyncRemoteImageLoader(ImageLoadJob imageLoadJob) {
        this.imageLoadJob = imageLoadJob;
    }

    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap;
        if (this.imageCache.containsKey(params[0])) {
            bitmap = (Bitmap) ((SoftReference) this.imageCache.get(params[0])).get();
            if (bitmap != null) {
                return bitmap;
            }
        }
        bitmap = HttpManager.downloadBitmap(params[0]);
        if (bitmap != null) {
            this.imageCache.put(params[0], new SoftReference(bitmap));
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            this.imageLoadJob.setBitmap(bitmap);
            this.imageLoadJob.getImageView().setImageBitmap(bitmap);
            this.imageLoadJob.setFinished(true);
        }
    }
}
