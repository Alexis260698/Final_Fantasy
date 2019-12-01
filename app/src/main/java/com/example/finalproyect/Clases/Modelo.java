package com.example.finalproyect.Clases;

import android.net.Uri;

public class Modelo {
    public static final int IMAGE_TYPE=0;
    public static final int AUDIO_TYPE=1;
    public static final int VIDEO_TYPE=2;

    public int type;
    public Uri data;

    public Modelo(int type, Uri data)
    {

        this.type = type;
        this.data = data;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Uri getData() {
        return data;
    }

    public void setData(Uri data) {
        this.data = data;
    }

}
