package com.xunlei.library.pulltorefresh.extras;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import com.xunlei.library.pulltorefresh.PullToRefreshBase;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.Mode;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.OnPullEventListener;
import com.xunlei.library.pulltorefresh.PullToRefreshBase.State;
import java.util.HashMap;

public class SoundPullEventListener<V extends View> implements OnPullEventListener<V> {
    private final Context mContext;
    private MediaPlayer mCurrentMediaPlayer;
    private final HashMap<State, Integer> mSoundMap = new HashMap();

    public SoundPullEventListener(Context context) {
        this.mContext = context;
    }

    public final void onPullEvent(PullToRefreshBase<V> pullToRefreshBase, State event, Mode direction) {
        Integer soundResIdObj = (Integer) this.mSoundMap.get(event);
        if (soundResIdObj != null) {
            playSound(soundResIdObj.intValue());
        }
    }

    public void addSoundEvent(State event, int resId) {
        this.mSoundMap.put(event, Integer.valueOf(resId));
    }

    public void clearSounds() {
        this.mSoundMap.clear();
    }

    public MediaPlayer getCurrentMediaPlayer() {
        return this.mCurrentMediaPlayer;
    }

    private void playSound(int resId) {
        if (this.mCurrentMediaPlayer != null) {
            this.mCurrentMediaPlayer.stop();
            this.mCurrentMediaPlayer.release();
        }
        this.mCurrentMediaPlayer = MediaPlayer.create(this.mContext, resId);
        if (this.mCurrentMediaPlayer != null) {
            this.mCurrentMediaPlayer.start();
        }
    }
}
