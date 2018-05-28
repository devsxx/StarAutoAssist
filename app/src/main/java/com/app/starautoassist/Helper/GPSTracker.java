package com.app.starautoassist.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


import com.app.starautoassist.Activity.LoginActivity;
import com.app.starautoassist.Activity.Towing_Activity;
import com.app.starautoassist.R;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class GPSTracker extends Service implements LocationListener {

	   private final Context mContext;

	    // flag for GPS status
	    boolean isGPSEnabled = false;

	    // flag for network status
	    boolean isNetworkEnabled = false;

	    // flag for GPS status
	    boolean canGetLocation = false;
//new
	    Location location; // location
	    double latitude; // latitude
	    double longitude; // longitude

	    // The minimum distance to change Updates in meters
	    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	    // The minimum time between updates in milliseconds
	    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	    // Declaring a Towing_Activity Manager
	    protected LocationManager locationManager;

	    public GPSTracker(Context context) {
	        this.mContext = context;
	        getLocation();
	    }

	    public Location getLocation() {
	        try {
	            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

	            // getting GPS status
	            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	            // getting network status
	            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	            if (!isGPSEnabled && !isNetworkEnabled) {
	                // no network provider is enabled
	            	Log.v("no *","no *");
	            } else {
					if (ActivityCompat.checkSelfPermission(mContext, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						ActivityCompat.requestPermissions((Activity)mContext, new String[]{ ACCESS_FINE_LOCATION,  ACCESS_COARSE_LOCATION}, 102);
					} else {
						this.canGetLocation = true;
						if (isNetworkEnabled) {
							locationManager.requestLocationUpdates(
									LocationManager.NETWORK_PROVIDER,
									MIN_TIME_BW_UPDATES,
									MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
							Log.d("Network", "Network");
							if (locationManager != null) {
								location = locationManager
										.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
								if (location != null) {
									latitude = location.getLatitude();
									longitude = location.getLongitude();
									Log.v("network","latitude"+latitude+"longitude"+longitude);
								}
							}
						}
						// if GPS Enabled get lat/long using GPS Services
						if (isGPSEnabled) {
							if (location == null) {
								locationManager.requestLocationUpdates(
										LocationManager.GPS_PROVIDER,
										MIN_TIME_BW_UPDATES,
										MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
								Log.d("GPS Enabled", "GPS Enabled");
								if (locationManager != null) {
									location = locationManager
											.getLastKnownLocation(LocationManager.GPS_PROVIDER);
									if (location != null) {
										latitude = location.getLatitude();
										longitude = location.getLongitude();
										Log.v("gps","latitude"+latitude+"longitude"+longitude);
									}
								}
							}
						}
                        Towing_Activity.onreslat=latitude;
                        Towing_Activity.onreslon = longitude;
					}
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return location;
	    }

	    /**
	     * Stop using GPS listener
	     * Calling this function will stop using GPS in your app
	     * */
	    public void stopUsingGPS(){
	        if(locationManager != null){
	            locationManager.removeUpdates(GPSTracker.this);
	        }
	    }
	   
	    /**
	     * Function to get latitude
	     * */
	    public double getLatitude(){
	        if(location != null){
	            latitude = location.getLatitude();
	        }
	       
	        // return latitude
	        return latitude;
	    }
	   
	    /**
	     * Function to get longitude
	     * */
	    public double getLongitude(){
	        if(location != null){
	            longitude = location.getLongitude();
	        }
	       
	        // return longitude
	        return longitude;
	    }
	   
	    /**
	     * Function to check GPS/wifi enabled
	     * @return boolean
	     * */
	    public boolean canGetLocation() {
	        return this.canGetLocation;
	    }
	   
	    /**
	     * Function to show settings alert dialog
	     * On pressing Settings button will lauch Settings Options
	     * */
	    public void showSettingsAlert(){
	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
	        
	        // Setting Dialog Title
	        alertDialog.setTitle(getResources().getString(R.string.gps_settings));

	        // Setting Dialog Message
	        alertDialog.setMessage(getResources().getString(R.string.gps_notenabled));

	        // On pressing Settings button
	        alertDialog.setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	                mContext.startActivity(intent);
	            }
	        });

	        // on pressing cancel button
	        alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            dialog.cancel();
	            }
	        });

	        // Showing Alert Message
	        alertDialog.show();
	    }
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case 102: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(GPSTracker.this, getString(R.string.location_permission_access), Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(GPSTracker.this, getString(R.string.need_permission_to_access), Toast.LENGTH_SHORT).show();
				}
				return;
			}
		}
	}
	@Override
	public void onLocationChanged(Location location) {
	// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
	// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
	// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
	// TODO Auto-generated method stub
	return null;
	}
	  
	}




