package my.base.io;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.File;
import java.io.IOException;

public class MyFile implements Parcelable {
    public static final Creator<MyFile> CREATOR = new Creator<MyFile>() {
        public MyFile createFromParcel(Parcel in) {
            return new MyFile(in);
        }

        public MyFile[] newArray(int size) {
            return new MyFile[size];
        }
    };
    private String name;
    private String path;
    private long size;
    private FILE_TYPE type;

    public enum FILE_TYPE {
        UNKNOWN,
        NORMAL,
        AUDIO,
        VIDEO
    }

    public MyFile(File file, FILE_TYPE type) {
        this.name = "";
        this.size = 0;
        this.path = "";
        this.type = FILE_TYPE.NORMAL;
        if (file != null) {
            this.name = file.getName();
            this.size = file.length();
            try {
                this.path = file.getCanonicalPath();
                this.path = this.path.substring(0, this.path.lastIndexOf("/") + 1);
            } catch (IOException e) {
            }
            this.type = type;
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return this.size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public FILE_TYPE getType() {
        return this.type;
    }

    public void setType(FILE_TYPE type) {
        this.type = type;
    }

    public MyFile() {
        this.name = "";
        this.size = 0;
        this.path = "";
        this.type = FILE_TYPE.NORMAL;
    }

    private MyFile(Parcel in) {
        this.name = "";
        this.size = 0;
        this.path = "";
        this.type = FILE_TYPE.NORMAL;
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.name = in.readString();
        this.size = in.readLong();
        this.path = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeLong(this.size);
        dest.writeString(this.path);
    }
}
