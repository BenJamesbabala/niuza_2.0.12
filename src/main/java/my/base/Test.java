package my.base;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.niuza.android.R;
import my.base.model.AppInfo;
import my.base.util.ApplicationUtils;

public class Test extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        TextView tv = (TextView) findViewById(2131034113);
        ImageView icon = (ImageView) findViewById(R.raw.keep);
        AppInfo appInfo = ApplicationUtils.getApkFileInfo(getApplication(), "/sdcard/com.codingcaveman.Solo.1309782241674.apk");
        icon.setImageDrawable(appInfo.getIcon());
        tv.setText(appInfo.getAppName() + " /");
        tv.append(appInfo.getPackageName() + " /");
        tv.append(appInfo.getVersionCode() + " /");
    }
}
