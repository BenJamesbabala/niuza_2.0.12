package my.base.net;

import android.widget.ProgressBar;
import android.widget.TextView;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

public class FileDownloadJob {
    private int downloadedSize = 0;
    private File file = null;
    private LinkedList<FileDownloadThread> fileDownloadThreadList = new LinkedList();
    private String fileName = "";
    private String fileTitle = "";
    private String fileUrl = "";
    private boolean isDownloadFinished = false;
    private boolean isRestart = false;
    private int jobID = 0;
    private int lastSize = 0;
    private String path = "";
    private ProgressBar progressBar = null;
    private int status = 1;
    private TextView textView = null;
    private int threadNum = 1;
    private int totalSize = 0;
    private URL url = null;

    public String getFileTitle() {
        return this.fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getDownloadedSize() {
        this.downloadedSize = 0;
        Iterator it = this.fileDownloadThreadList.iterator();
        while (it.hasNext()) {
            this.downloadedSize += ((FileDownloadThread) it.next()).getDownloadedSize();
        }
        return this.downloadedSize;
    }

    public void setDownloadedSize(int downloadedSize) {
        this.downloadedSize = downloadedSize;
    }

    public boolean isDownloadFinished() {
        this.isDownloadFinished = true;
        Iterator it = this.fileDownloadThreadList.iterator();
        while (it.hasNext()) {
            if (!((FileDownloadThread) it.next()).isFinished()) {
                this.isDownloadFinished = false;
            }
        }
        return this.isDownloadFinished;
    }

    public void setDownloadFinished(boolean isDownloadFinished) {
        this.isDownloadFinished = isDownloadFinished;
    }

    public boolean isRestart() {
        return this.isRestart;
    }

    public void setRestart(boolean isRestart) {
        this.isRestart = isRestart;
    }

    public LinkedList<FileDownloadThread> getFileDownloadThreadList() {
        return this.fileDownloadThreadList;
    }

    public void setFileDownloadThreadList(LinkedList<FileDownloadThread> fileDownloadThreadList) {
        this.fileDownloadThreadList = fileDownloadThreadList;
    }

    public int getThreadNum() {
        return this.threadNum;
    }

    public void setThreadNum(int threadNum) {
        this.threadNum = threadNum;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public FileDownloadThread getFileDownloadThread(int index) {
        if (index > this.fileDownloadThreadList.size()) {
            return null;
        }
        return (FileDownloadThread) this.fileDownloadThreadList.get(index - 1);
    }

    public void start() {
        try {
            File dir = new File(this.path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            this.file = new File(this.path + this.fileName);
            if (!this.file.exists()) {
                RandomAccessFile rafile = new RandomAccessFile(this.file, "rwd");
                rafile.setLength((long) this.totalSize);
                rafile.close();
            }
            int blockSize = this.totalSize / this.threadNum;
            if (this.totalSize % this.threadNum != 0) {
                this.threadNum++;
            }
            for (int i = 0; i < this.threadNum; i++) {
                FileDownloadThread fdt = new FileDownloadThread(this);
                if (i < this.threadNum - 1) {
                    fdt.setStartPos(i * blockSize);
                    fdt.setCurPos(i * blockSize);
                    fdt.setEndPos(((i + 1) * blockSize) - 1);
                } else {
                    fdt.setStartPos(i * blockSize);
                    fdt.setCurPos(i * blockSize);
                    fdt.setEndPos(this.totalSize);
                }
                fdt.setId(i + 1);
                this.fileDownloadThreadList.add(fdt);
                fdt.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        try {
            File dir = new File(this.path);
            if (!dir.exists()) {
                dir.mkdir();
            }
            this.file = new File(this.path + this.fileName);
            if (!this.file.exists()) {
                RandomAccessFile rafile = new RandomAccessFile(this.file, "rwd");
                rafile.setLength((long) this.totalSize);
                rafile.close();
            }
            Iterator it = this.fileDownloadThreadList.iterator();
            while (it.hasNext()) {
                FileDownloadThread fileDownloadThread = (FileDownloadThread) it.next();
                fileDownloadThread.setCanceled(false);
                if (fileDownloadThread.getCurPos() < fileDownloadThread.getEndPos()) {
                    fileDownloadThread.setFinished(false);
                } else {
                    fileDownloadThread.setFinished(true);
                }
                if (!fileDownloadThread.isFinished()) {
                    fileDownloadThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancel() {
        Iterator it = this.fileDownloadThreadList.iterator();
        while (it.hasNext()) {
            ((FileDownloadThread) it.next()).setCanceled(true);
        }
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getJobID() {
        return this.jobID;
    }

    public void setLastSize(int lastSize) {
        this.lastSize = lastSize;
    }

    public int getLastSize() {
        return this.lastSize;
    }
}
