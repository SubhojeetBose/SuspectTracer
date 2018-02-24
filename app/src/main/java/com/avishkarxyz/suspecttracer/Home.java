package com.avishkarxyz.suspecttracer;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;

public class Home extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public final DBHandler friend = new DBHandler(this,null,null,1);
    private String P_key,P_userName,P_number;
    public FusedLocationProviderClient mFusedLocationClient;
    private Location P_location;
    private LocationManager locationmanager;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        P_key=mDatabase.push().getKey();
        provider=locationmanager.getBestProvider(new Criteria(),false);



        //temporary table

        /*String Friend_name="Subhojeet Bose";
        String Friend_id="123423";
        FriendContact dummy = new FriendContact(Friend_name,Friend_id);
        friend.AddFriend(dummy);
        Friend_name="Ishaan";
        Friend_id="123424";
        dummy = new FriendContact(Friend_name,Friend_id);
        friend.AddFriend(dummy);
        Location location = new Location("");
        Time time = Time.valueOf("2:00:00");
        location.setLatitude(25.495933d);
        location.setLongitude(81.868172d);
        location.setTime(time.getTime());
        friend.AddEntry(location,1);
        location.setLatitude(70.000000d);
        location.setLongitude(25.868172d);
        location.setTime(time.getTime());
        friend.AddEntry(location,1);
        location.setLatitude(70.000000d);
        location.setLongitude(25.868172d);
        location.setTime(time.getTime());
        friend.AddEntry(location,2);
        location.setLatitude(25.495933d);
        location.setLongitude(81.868172d);
        location.setTime(time.getTime());
        friend.AddEntry(location,2);*/
        //temporary table end
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.icon2);
        Location location = friend.RetrieveLast(1);
        // Add a marker in Sydney and move the camera
        LatLng india = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(india).title(friend.Name(1)).icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
        location = friend.RetrieveLast(2);
        india = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(india).title(friend.Name(2)).icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india));
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission. ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission. ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(Home.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission. ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission. ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        locationmanager.requestLocationUpdates(provider,400,1, (LocationListener) this);

                }
                else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }
}
