package com.xunlei.library.asymmetricgridview.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import java.util.Stack;

class ViewPool<T extends View> implements Parcelable {
    PoolObjectFactory<View> factory;
    Stack<View> stack;
    PoolStats stats;

    static class PoolStats {
        int created = 0;
        int hits = 0;
        int misses = 0;
        int size = 0;

        PoolStats() {
        }

        String getStats(String name) {
            return String.format("%s: size %d, hits %d, misses %d, created %d", new Object[]{name, Integer.valueOf(this.size), Integer.valueOf(this.hits), Integer.valueOf(this.misses), Integer.valueOf(this.created)});
        }
    }

    ViewPool() {
        this.stack = new Stack();
        this.factory = null;
        this.stats = new PoolStats();
    }

    ViewPool(PoolObjectFactory<View> factory) {
        this.stack = new Stack();
        this.factory = null;
        this.factory = factory;
    }

    View get() {
        PoolStats poolStats = null;
        if (this.stack.size() > 0) {
            poolStats = this.stats;
            poolStats.hits++;
            poolStats = this.stats;
            poolStats.size--;
            return (View) this.stack.pop();
        }
        poolStats = this.stats;
        poolStats.misses++;
        View object = this.factory != null ? this.factory.createObject() : null;
        if (object != null) {
            poolStats = this.stats;
            poolStats.created++;
        }
        return object;
    }

    void put(View object) {
        this.stack.push(object);
        PoolStats poolStats = this.stats;
        poolStats.size++;
    }

    void clear() {
        this.stats = new PoolStats();
        this.stack.clear();
    }

    String getStats(String name) {
        return this.stats.getStats(name);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }
}
