package service;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;

import helper.H;

public class BaseLocationListener implements LocationListener, GpsStatus.Listener{
	private ApplicationService mService;
	
	public BaseLocationListener(ApplicationService service){
		H.logD("BaseLocationListener constructor");
		mService = service;
	}
	@Override
	public void onGpsStatusChanged(int status) {
		mService.onGpsStatusEvent(status);
		switch (status)
		{
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				break;

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				//H.logD("Gps Status", "GPS_EVENT_SATELLITE_STATUS");
				break;

			case GpsStatus.GPS_EVENT_STARTED:
				H.logD("Gps Status", "GPS_EVENT_STARTED");
				break;

			case GpsStatus.GPS_EVENT_STOPPED:
				H.logD("Gps Status", "GPS_EVENT_STOPPED");
				break;
		}
	}

	@Override
    public void onLocationChanged(Location location) {
        mService.onLocationChanged(location);
    }
	@Override
	public void onProviderDisabled(String provider) {
		H.logD(provider, "Location provider disabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		H.logD(provider, "Location provider enabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch(status){
		case LocationProvider.OUT_OF_SERVICE:
			H.logD(provider, "Location : out of service");
			break;
		case LocationProvider.AVAILABLE:
			H.logD(provider, "Location : available");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			H.logD(provider, "Location : temporarily unavailable");
			break;
		}		
	}
}
