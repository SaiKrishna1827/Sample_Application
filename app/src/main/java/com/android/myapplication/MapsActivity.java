package com.android.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    ArrayList<DetailsModel> list = new ArrayList<DetailsModel>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        Intent intent = getIntent();
        String jobjStr = intent.getStringExtra("jobj");
        JSONArray jobj = null;
        try {
            jobj = new JSONArray(jobjStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(jobj != null){

            JSONObject detailsObj;

            for(int i=0;i<jobj.length();i++){
                try {
                    detailsObj = (JSONObject) jobj.get(i);

                    DetailsModel model = new DetailsModel();
                    model.setFirst_name(detailsObj.getString("first_name"));
                    model.setLast_name(detailsObj.getString("last_name"));
                    model.setEmail(detailsObj.getString("email"));
                    model.setCategory(detailsObj.getString("category"));
                    model.setDescription(detailsObj.getString("description"));
                    model.setPrice(detailsObj.getString("price"));
                    model.setAvailability(detailsObj.getString("availability"));
                    model.setLatitude(detailsObj.getDouble("latitude"));
                    model.setLongitude(detailsObj.getDouble("longitude"));
                    model.setId(detailsObj.getInt("id"));

                    list.add(model);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }else{
            Snackbar.make(findViewById(android.R.id.content), "Please check your Network Connection", Snackbar.LENGTH_LONG).show();
        }

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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL );

        for(int i=0;i<list.size();i++){

            DetailsModel model = list.get(i);

            LatLng locatioN = new LatLng(model.getLatitude(), model.getLongitude());
            mMap.addMarker(new MarkerOptions().position(locatioN).title(model.getFirst_name()).snippet(String.valueOf(model.getId())));

        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.getTitle().equals("Current Position") == false){

                    int id = Integer.parseInt(marker.getSnippet())-1;
                    Intent intent = new Intent(MapsActivity.this,Details.class);

                    DetailsModel model = list.get(id);
                    intent.putExtra("id",model.getId());
                    intent.putExtra("first_name",model.getFirst_name());
                    intent.putExtra("last_name",model.getLast_name());
                    intent.putExtra("email",model.getEmail());
                    intent.putExtra("category",model.getCategory());
                    intent.putExtra("description",model.getDescription());
                    intent.putExtra("price",model.getPrice());
                    intent.putExtra("availability",model.getAvailability());
                    intent.putExtra("latitude",model.getLatitude());
                    intent.putExtra("longitude",model.getLongitude());

                    startActivity(intent);
                }

                return false;
            }
        });

        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }



}
