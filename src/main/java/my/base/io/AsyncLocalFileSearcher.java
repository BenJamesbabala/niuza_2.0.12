package my.base.io;

import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.util.Iterator;

public class AsyncLocalFileSearcher extends AsyncTask<Integer, Integer, String> {
    private static String tag = "AsyncLocalFileSearcher";
    private FileSearchJob fileSearchJob;

    public FileSearchJob getFileSearchJob() {
        return this.fileSearchJob;
    }

    public void setFileSearchJob(FileSearchJob fileSearchJob) {
        this.fileSearchJob = fileSearchJob;
    }

    public AsyncLocalFileSearcher(FileSearchJob fileSearchJob) {
        this.fileSearchJob = fileSearchJob;
    }

    protected String doInBackground(Integer... params) {
        Log.d(tag, "AsyncLocalFileSearcher start!");
        Iterator it = this.fileSearchJob.getSearchPathList().iterator();
        while (it.hasNext()) {
            String path = (String) it.next();
            if (this.fileSearchJob.isCanceled()) {
                break;
            }
            listFile(new File(path));
        }
        this.fileSearchJob.setFinished(true);
        Log.d(tag, "AsyncLocalFileSearcher finished!");
        return "执行完毕";
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
    }

    private void listFile(File filePath) {
        if (!this.fileSearchJob.isCanceled() && filePath != null && !filePath.isFile()) {
            File[] files = filePath.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length && !this.fileSearchJob.isCanceled(); i++) {
                    File file = files[i];
                    this.fileSearchJob.setSearchingFile(file.getAbsolutePath());
                    if (file == null || !file.isFile()) {
                        if (file != null && file.isDirectory()) {
                            listFile(file);
                        }
                    } else if (this.fileSearchJob.getSearchFileFilter() == null || (this.fileSearchJob.getSearchFileFilter() != null && this.fileSearchJob.getSearchFileFilter().accept(file, file.getName().toLowerCase()))) {
                        this.fileSearchJob.setSearchedFileNum(this.fileSearchJob.getSearchedFileNum() + 1);
                        this.fileSearchJob.addSearchedFile(new MyFile(file, this.fileSearchJob.getSearchFileFilter().getFileType()));
                    }
                }
            }
        }
    }
}
