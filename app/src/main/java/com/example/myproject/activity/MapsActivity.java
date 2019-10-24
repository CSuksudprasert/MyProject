package com.example.myproject.activity;

import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myproject.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private Button buttonNormal;
    private Button buttonSatellite;
    private Button buttonHybrid;
    private SupportMapFragment mapFragment;
    private double latitude = 0.0;
    private double longtitue = 0.0;
    String name,address ;
//    DatabaseReference databaseReference ;
//    ArrayList<String> lalong = new ArrayList<>();
//    Map<String ,ArrayList> markerCus = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonNormal = findViewById(R.id.buttonNormal);
        buttonSatellite = findViewById(R.id.buttonSatellite);
        buttonHybrid = findViewById(R.id.buttonHybrid);

        buttonNormal.setOnClickListener(this);
        buttonSatellite.setOnClickListener(this);
        buttonHybrid.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        String location = bundle.getString("Location");
        name = bundle.getString("CustomerName");
       // address = bundle.getString("CusAddress");
        String[] lct = location.split(" ");
        latitude = Double.parseDouble(lct[0]);
        longtitue = Double.parseDouble(lct[1]);
        //System.out.println(lct[1] + " : "+lct[2]);
        System.out.println("latitude : " + latitude + " longtitude : " + longtitue);
        //**ละติจูตลองติจูตไม่ตรง
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.buttonHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }
        //  mapFragment.getMapAsync(this);

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
        float zoomLevel = 16.0f;
        LatLng location = new LatLng(latitude, longtitue);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        mMap.addMarker(new MarkerOptions().position(location).title(name)); //mark ที่ปัจจุบัน
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoomLevel));

    }


//    private void markeCus(){
//        databaseReference = FirebaseDatabase.getInstance().getReference("Customer");
//
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Customer cus = dataSnapshot.getValue(Customer.class);
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }


}
