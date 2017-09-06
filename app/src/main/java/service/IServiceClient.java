package service;

import android.location.Location;

public interface IServiceClient {
	/**
	 * New message to the client of the service.
	 */
	public void onNotifyMessage(long noticeId);
	
	/**
	 * A new location fix has been obtained.
	 * @param loc
	 */
	public void onLocationUpdate(Location location);

}
