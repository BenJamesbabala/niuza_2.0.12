package my.base.net;

import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import my.base.App;
import my.base.util.LogUtils;

public class AsyncRemoteFileDownloader extends AsyncTask<Integer, Integer, String> {
    private static final int BUFFER_SIZE = 8192;
    private static final String tag = "AsyncRemoteFileDownloader";
    private FileDownloadJob fileDownloadJob;
    private FileDownloadThread fileDownloadThread;
    private String name = "";

    public AsyncRemoteFileDownloader(FileDownloadJob fileDownloadJob, int threadIndex) {
        this.fileDownloadJob = fileDownloadJob;
        this.fileDownloadThread = fileDownloadJob.getFileDownloadThread(threadIndex);
        this.name = this.fileDownloadThread.getName();
    }

    protected String doInBackground(Integer... params) {
        IOException e;
        RandomAccessFile randomAccessFile;
        BufferedInputStream bufferedInputStream;
        Log.d(new StringBuilder(String.valueOf(this.fileDownloadJob.getFileName())).append("/").append(this.name).toString(), "url=" + this.fileDownloadJob.getFileUrl() + " / file=" + this.fileDownloadJob.getFileName() + " / start=" + this.fileDownloadThread.getStartPos() + " / end=" + this.fileDownloadThread.getEndPos() + " / current=" + this.fileDownloadThread.getCurPos());
        byte[] buf = new byte[8192];
        try {
            HttpURLConnection conn;
            NetManager.checkNetwork(App.context);
            if (NetManager.networkType == 3) {
                String domain = "";
                String path = "";
                String requestURL = this.fileDownloadJob.getFileUrl();
                int index = requestURL.indexOf("http://");
                if (index >= 0) {
                    requestURL = requestURL.substring(index + 7);
                    index = requestURL.indexOf("/");
                    if (index > 0) {
                        domain = requestURL.substring(0, index);
                        path = requestURL.substring(index);
                    } else {
                        domain = requestURL;
                        path = "/";
                    }
                }
                LogUtils.d(App.tag, "domain:" + domain + " | path:" + path);
                conn = (HttpURLConnection) new URL("http://10.0.0.172" + path).openConnection();
                conn.setRequestProperty("X-Online-Host", domain);
            } else {
                conn = (HttpURLConnection) this.fileDownloadJob.getUrl().openConnection();
            }
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("GET");
            conn.setAllowUserInteraction(true);
            conn.setRequestProperty("Range", "bytes=" + this.fileDownloadThread.getCurPos() + "-" + this.fileDownloadThread.getEndPos());
            conn.setRequestProperty("Connection", "Keep-Alive");
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), 8192);
            try {
                RandomAccessFile fas = new RandomAccessFile(this.fileDownloadJob.getFile(), "rwd");
                fas.seek((long) this.fileDownloadThread.getCurPos());
                while (this.fileDownloadThread.getCurPos() < this.fileDownloadThread.getEndPos()) {
                    try {
                        if (!this.fileDownloadThread.isCanceled()) {
                            int len = bis.read(buf, 0, 8192);
                            if (len == -1) {
                                break;
                            }
                            fas.write(buf, 0, len);
                            this.fileDownloadThread.setCurPos(this.fileDownloadThread.getCurPos() + len);
                            if (this.fileDownloadThread.getCurPos() > this.fileDownloadThread.getEndPos()) {
                                this.fileDownloadThread.setDownloadedSize(this.fileDownloadThread.getDownloadedSize() + ((len - (this.fileDownloadThread.getCurPos() - this.fileDownloadThread.getEndPos())) + 1));
                            } else {
                                this.fileDownloadThread.setDownloadedSize(this.fileDownloadThread.getDownloadedSize() + len);
                            }
                        } else {
                            break;
                        }
                    } catch (IOException e2) {
                        e = e2;
                        randomAccessFile = fas;
                        bufferedInputStream = bis;
                    }
                }
                this.fileDownloadThread.setFinished(true);
                bis.close();
                fas.close();
                randomAccessFile = fas;
                bufferedInputStream = bis;
            } catch (IOException e3) {
                e = e3;
                bufferedInputStream = bis;
                e.printStackTrace();
                Log.e(new StringBuilder(String.valueOf(this.fileDownloadJob.getFileName())).append("/").append(this.name).toString(), e.getMessage());
                Log.d(new StringBuilder(String.valueOf(this.fileDownloadJob.getFileName())).append("/").append(this.name).toString(), "exit! current=" + this.fileDownloadThread.getCurPos() + " / end=" + this.fileDownloadThread.getEndPos());
                return "执行完毕";
            }
        } catch (IOException e4) {
            e = e4;
            e.printStackTrace();
            Log.e(new StringBuilder(String.valueOf(this.fileDownloadJob.getFileName())).append("/").append(this.name).toString(), e.getMessage());
            Log.d(new StringBuilder(String.valueOf(this.fileDownloadJob.getFileName())).append("/").append(this.name).toString(), "exit! current=" + this.fileDownloadThread.getCurPos() + " / end=" + this.fileDownloadThread.getEndPos());
            return "执行完毕";
        }
        Log.d(new StringBuilder(String.valueOf(this.fileDownloadJob.getFileName())).append("/").append(this.name).toString(), "exit! current=" + this.fileDownloadThread.getCurPos() + " / end=" + this.fileDownloadThread.getEndPos());
        return "执行完毕";
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
    }
}
