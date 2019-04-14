package org.szuwest.lib;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;


//import com.mobclick.android.MobclickAgent;

//import com.niuza.android.R;

import com.niuza.android.R;

import org.szuwest.view.CustomDialog;

import my.base.util.LogUtils;

public class BaseActivity extends FragmentActivity {
	private CustomDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActivity(this);
    }
	@Override
	protected void onStart() {
		super.onStart();
		LogUtils.d(this.getClass().getSimpleName(), "onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
        // umeng
//        MobclickAgent.onResume(this);

		AppManager.getInstance().setCurrentActivity(this);
		LogUtils.d(getClass().getSimpleName() , "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.d( getClass().getSimpleName() , "onPause");
//        MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		LogUtils.d(getClass().getSimpleName() , "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

        //最好所有的Activity的最顶层的Layout都将其id设成rootLayout，这样统一在destroy中释放资源
        unbindDrawables(findViewById(R.id.rootLayout));

		AppManager.getInstance().removeActivity(this);
		LogUtils.d(getClass().getSimpleName() , "onDestory");

        System.gc();
        Runtime.getRuntime().gc();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		LogUtils.d(getClass().getSimpleName() , "onLowMemory");
		System.gc();
        Runtime.getRuntime().gc();
	}

//    protected HintDialog waitingDialog;
//    public void showWaiting(String msg){
//        showWaiting(msg, true);
//    }
//
//    public synchronized void showWaiting(String msg, boolean cancelable) {
//        hideWaiting();
//        if (isFinishing()) return;
//        waitingDialog = HintDialog.showWaiting(this);
//        waitingDialog.setCancelable(cancelable);
//        if (msg != null ) {
//            waitingDialog.setHintText(msg);
//        }
//        if ( !waitingDialog.isShowing() && !isFinishing()) {
//            waitingDialog.show();
//        }
//    }
//
//    public void hideWaiting() {
//        if (waitingDialog != null && waitingDialog.isShowing()) {
//            waitingDialog.dismiss();
//        }
//        waitingDialog = null;
//    }

	public CustomDialog showLoadingDialog(String msg) {
		if (this.loadingDialog == null) {
			this.loadingDialog = new CustomDialog(this, 3);
			this.loadingDialog.setCanceledOnTouchOutside(false);
			this.loadingDialog.setCancelable(true);
		}
		this.loadingDialog.setMessage(msg);
		if (!this.loadingDialog.isShowing()) {
			this.loadingDialog.show();
		}
		return this.loadingDialog;
	}

	public void dismissDialog() {
		if (this.loadingDialog != null && this.loadingDialog.isShowing()) {
			this.loadingDialog.dismiss();
		}
		this.loadingDialog = null;
	}

    protected void unbindDrawables(View view) {
        if (view == null) return;
        try {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
                view.setBackgroundDrawable(null);
            }
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(null);
            }
            else if (view instanceof ViewGroup ) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                if (!(view instanceof AdapterView)) {
                    ((ViewGroup) view).removeAllViews();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
