package com.enugusomlapp.enugusomlapp;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class HFGMapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hfgmap);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //get the strings from successvisitForm activity
        Bundle bundle = getIntent().getExtras();
        //String historyID = bundle.getString("historyID");
        String health_facilityName = bundle.getString("health_facility");
        String facility_street = bundle.getString("facility_street");
        String citystate = bundle.getString("citystate");
        String country = "Nigeria"; //NOT in the bundle
        String fullAddress = facility_street + ", " + citystate  + ", " +  country + ".";

        double latitude;
        double longitude;
        List<Address> geocodeMatches = null;
        try{
            geocodeMatches = new Geocoder(this).getFromLocationName(fullAddress, 1);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(geocodeMatches != null){
            Toast.makeText(getApplicationContext(), "Fetching and setting location. Pls wait...", Toast.LENGTH_LONG).show();
            latitude = geocodeMatches.get(0).getLatitude();
            longitude = geocodeMatches.get(0).getLongitude();
            System.out.println(latitude);
            System.out.println(longitude);
            LatLng hflocation = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(hflocation).title(health_facilityName));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(hflocation));
        }else{
            Toast.makeText(getApplicationContext(), "Address Error: Supplied Address is not well defined. Pls edit address and try again. I will take you to Sydney.", Toast.LENGTH_LONG).show();
            // Add a marker in Sydney and move the camera there
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }


    public void onClick(View v){
        finish();
    }
}