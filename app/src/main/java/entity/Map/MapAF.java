package entity.Map;

import android.content.Context;

import entity.Map.Location.LocationI;
import helper.Root;

public abstract class MapAF extends Root {
    public MapAF(Context context){
        super(context);
    }

    public abstract LocationI getLocation(String provider);
}