package com.niuza.android.module.logic.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider {
    private Gson mExposeGson;
    private Gson mGson;

    private static class GsonProviderHolder {
        private static final GsonProvider instance = new GsonProvider();

        private GsonProviderHolder() {
        }
    }

    private GsonProvider() {
        this.mGson = new GsonBuilder().setPrettyPrinting().create();
        this.mExposeGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    }

    public static GsonProvider getInstance() {
        return GsonProviderHolder.instance;
    }

    public Gson getGson() {
        return this.mGson;
    }

    public Gson getExposeGson() {
        return this.mExposeGson;
    }
}
