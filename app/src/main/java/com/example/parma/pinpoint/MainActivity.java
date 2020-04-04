package com.example.parma.pinpoint;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView locationResult;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationResult = findViewById(R.id.locationResult);
        locationResult.setText("Please turn on your GPS");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> currentLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if(currentLocation != null && currentLocation.size() > 0 ){
                        String result = "";
                        if(currentLocation.get(0).getAddressLine(0) != null) {
                            result += "Address: " + currentLocation.get(0).getAddressLine(0) + "\n\n";
                        }
                        if(currentLocation.get(0).getAdminArea() != null) {
                            result += "State: " + currentLocation.get(0).getAdminArea() + "\n\n";
                        }
                        if(currentLocation.get(0).getPostalCode() != null) {
                            result += "Pin code: " + currentLocation.get(0).getPostalCode() + "\n\n";
                        }
                        if(currentLocation.get(0).getCountryName() != null) {
                            result += "Country: " + currentLocation.get(0).getCountryName() + "\n\n";
                        }
                        locationResult.setText(result);
                        locationResult.setScaleX(0);
                        locationResult.setScaleY(0);
                        locationResult.animate().scaleX(1f).scaleY(1f).setDuration(500).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
        }
    }
}
