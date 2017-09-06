package service;

import android.content.Context;
import android.location.Location;

import com.mc.youthhostels.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

import helper.Root;

public class PointInfo extends Root {
	public static final int DECIMAL_PLACES = 5;  // decimal places of location value

	private Location mLocation;
	private boolean isReady;
	private long mDeviceTime;
	private long mNetworkTime;

	public PointInfo(Context context){
		super(context);
		mLocation = null;
		isReady = false;
		mDeviceTime = System.currentTimeMillis();
		mNetworkTime = System.currentTimeMillis();
	}
	
	public PointInfo(Context context, Location location){
		this(context);
		mLocation = location;
	}

	public void setLocation(Location location){
		mLocation = location;
		mDeviceTime = System.currentTimeMillis(); 
//		mNetworkTime = location.getTime();
		isReady = true;
	}


	public boolean isReady(){
		return mLocation != null && isReady;
	}

	public void resetReady(){
		isReady = false;
	}

/*	public String getLatitude(){
		if (mLocation != null) {
			return String.valueOf(new BigDecimal(mLocation.getLatitude()).setScale(DECIMAL_PLACES, RoundingMode.UP).doubleValue());
		}
		return "";
	}
    public String getLongitude(){
        if (mLocation != null) {
            return String.valueOf(new BigDecimal(mLocation.getLongitude()).setScale(DECIMAL_PLACES, RoundingMode.UP).doubleValue());
        }
        return "";
    }*/

	public Double getLongitude(){
        Double result=null;
		if (mLocation != null) {
			result = new BigDecimal(mLocation.getLongitude()).setScale(DECIMAL_PLACES, RoundingMode.UP).doubleValue();
		}
		return result;
	}
    public Double getLatitude(){
        Double result=null;
        if (mLocation != null) {
            result = new BigDecimal(mLocation.getLatitude()).setScale(DECIMAL_PLACES, RoundingMode.UP).doubleValue();
        }
        return result;
    }
    public Location getLocation(){
        return mLocation;
    }
}
