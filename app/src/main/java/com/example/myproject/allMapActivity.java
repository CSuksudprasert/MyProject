package com.example.myproject;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public class allMapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private Button buttonNormal;
    private Button buttonSatellite;
    private Button buttonHybrid;
    private SupportMapFragment mapFragment;
    private double latitude = 0.0;
    private double longtitue = 0.0;
    String name;
    DatabaseReference databaseReference;
    ArrayList<String> lalong = new ArrayList<>();
    Map<String,Object> cusName = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_map);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        buttonNormal = findViewById(R.id.buttonNormal);
        buttonSatellite = findViewById(R.id.buttonSatellite);
        buttonHybrid = findViewById(R.id.buttonHybrid);

        buttonNormal.setOnClickListener(this);
        buttonSatellite.setOnClickListener(this);
        buttonHybrid.setOnClickListener(this);


//        Bundle bundle = getIntent().getExtras();
//        String location = bundle.getString("Location");
//        name = bundle.getString("CustomerName");
//        // address = bundle.getString("CusAddress");
//        String[] lct = location.split(" ");
//        latitude = Double.parseDouble(lct[0]);
//        longtitue = Double.parseDouble(lct[1]);
//        //System.out.println(lct[1] + " : "+lct[2]);
//        System.out.println("latitude : " + latitude + " longtitude : " + longtitue);

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoomLevel = 16.0f;
        LatLng location = new LatLng(latitude, longtitue);

//        int padding = 0; // offset from edges of the map in pixels
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        mMap.addMarker(new MarkerOptions().position(location).title(name)); //mark ที่ปัจจุบัน
         mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,zoomLevel));

    }

    @Override
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

    private void addMapMarker(){

    }


}
