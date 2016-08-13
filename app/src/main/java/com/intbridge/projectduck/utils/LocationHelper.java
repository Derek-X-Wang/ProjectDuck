package com.intbridge.projectduck.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 *
 * Created by Derek on 6/6/2015.
 */
public class LocationHelper {

    private final Context context;

    // Flag for GPS status
    boolean isGPSEnabled = false;

    // Flag for network status
    boolean isNetworkEnabled = false;

    // Flag for GPS status
    boolean canGetLocation = false;

    Location location; // Location
    double latitude; // Latitude
    double longitude; // Longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    public LocationHelper(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager == null){
            Log.e("LocationManager", "null");
        }else{
            // Getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network or gps provider is enabled
                location = null;
                Toast.makeText(context, "Please open GPS or WiFi", Toast.LENGTH_LONG).show();
                Log.e("GPS or Wifi Location", "null");
            }else{
                this.canGetLocation = true;
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    Log.e("GPS Enabled", "GPS Enabled");
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        Log.e("GPS Location", "null");
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        Log.e("GPS Location", "use network instead");
                        if (location == null) {
                            Log.e("GPS Location", "Network Location null");
                        }
                    }
                }else{
                    Log.e("Network Enabled", "Network Enabled");
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location == null) {
                        Log.e("Network Location", "null");
                    }
                }
            }   
        }
        
        return location;
    }

    
    public void refresh(){

        if (locationManager != null) {
            // Getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // No network provider is enabled
                location = null;
                Toast.makeText(context, "Please open GPS or WiFi", Toast.LENGTH_LONG).show();
            }else{
                this.canGetLocation = true;
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    Log.d("GPS Enabled", "GPS Enabled");
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location == null) {
                        Log.d("refresh", "null");
                        Log.d("GPS Location", "null");
                    }
                }else{
                    Log.d("Network Enabled", "Network Enabled");
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        Log.d("refresh", "null");
                        Log.d("Network Location", "null");
                    }
                }
            }
        }else{
            Log.d("refresh", "manager null");
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
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
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     * */
    public boolean getLocationStatus() {
        return this.canGetLocation;
    }


    /**
     * Function to show settings alert dialog.
     * On pressing the Settings button it will launch Settings Options.
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Improve your location");

        // Setting Dialog Message
        alertDialog.setMessage("Location service is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public static boolean isGoogleMapsInstalled(Activity host)
    {
        try
        {
            ApplicationInfo info = host.getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

}
