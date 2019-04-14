package my.base.net;

import android.os.AsyncTask;
import my.base.util.StringUtils;

public class AsyncHttpGetter extends AsyncTask<Object, Object, Boolean> {
    private HttpGetJob httpGetJob;

    public AsyncHttpGetter(HttpGetJob httpGetJob) {
        this.httpGetJob = httpGetJob;
    }

    protected Boolean doInBackground(Object... params) {
        if (StringUtils.isNotBlank(this.httpGetJob.getUrl())) {
            this.httpGetJob.setResult(HttpManager.doGetText(this.httpGetJob.getUrl()));
        }
        this.httpGetJob.setFinished(true);
        return null;
    }
}
