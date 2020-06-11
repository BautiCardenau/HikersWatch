package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView contentTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager =(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            if (lastKnownLocation != null){
                updateLocationInfo(lastKnownLocation);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void startListening (){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }
    }


    public void updateLocationInfo (Location location){
        contentTextView = findViewById(R.id.contentTextView);
        Geocoder geocoder =  new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if (addressList != null && addressList.size() > 0) {

                String address = "";

                address += "Latitude: " + Double.toString(addressList.get(0).getLatitude())+ "\r\n" + "\r\n";
                address += "Longitude: " + Double.toString(addressList.get(0).getLongitude()) + "\r\n" + "\r\n";
                address += "Accuracy: " + String.valueOf(location.getAccuracy())+ "\r\n" + "\r\n";
                address += "Altitude: " + String.valueOf(location.getAltitude()) + "\r\n" + "\r\n";
                address += "Address: ";

                String addressName = "";

                if (addressList.get(0).getThoroughfare() != null){
                    addressName += addressList.get(0).getThoroughfare() + " ";
                }

                if (addressList.get(0).getPostalCode() != null){
                    addressName += addressList.get(0).getPostalCode() + " ";
                }


                if (addressList.get(0).getLocality() != null){
                    addressName += addressList.get(0).getLocality() + " ";
                }

                if (addressList.get(0).getAdminArea() != null){
                    addressName += addressList.get(0).getAdminArea();
                }

                Log.i("address name", addressName);
                if (addressName == "") {
                    addressName = "Location not found!";
                }
                address += addressName;

                contentTextView.setText(address);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
