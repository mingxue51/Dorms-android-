package entity.Map.Marker;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.MarkerOptions;

import helper.Root;

//TODO Sgn
public abstract class MarkerA extends Root {
    public MarkerA(Context context) {
        super(context);
    }

    protected abstract MarkerOptions build();
    protected abstract Bitmap getBitmap();
    protected abstract void setPosition();
}