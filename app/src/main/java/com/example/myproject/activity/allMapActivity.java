package com.example.myproject.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myproject.R;
import com.example.myproject.model.Customer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    DatabaseReference databaseReference,mCount;
    ArrayList<Customer> lalong = new ArrayList<>();
    Map<String, Object> cusName = new HashMap<>();
    LocationManager locationManager;
    static long id;


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



    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final float zoomLevel = 10.0f;
        //LatLng location = new LatLng(latitude, longtitue);

//        int padding = 0; // offset from edges of the map in pixels
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);


        final DatabaseReference mDatabaseReff = FirebaseDatabase.getInstance().getReference().child("Customer");

        mDatabaseReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Customer cus = snapshot.getValue(Customer.class);
                    lalong.add(cus);
                }

                id = lalong.size();
//
                for(int i=0;i<id;i++){

                    String name = lalong.get(i).address();
                    double lati = Double.parseDouble((lalong.get(i).getLatitude()));
                    double longti = Double.parseDouble(lalong.get(i).getLongtitude());

                    LatLng picker = new LatLng(lati,longti);
                    MarkerOptions marker = new MarkerOptions().position(picker).title(name);

                    mMap.addMarker(marker);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(picker,zoomLevel));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
//        mMap.addMarker(new MarkerOptions().position(location).title(name)); //mark ที่ปัจจุบัน
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));

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

//    public void getCustomer(){
//        final DatabaseReference mDatabaseReff = FirebaseDatabase.getInstance().getReference().child("Customer");
//
//        mDatabaseReff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Customer cus = snapshot.getValue(Customer.class);
//                    lalong.add(cus);
//                }
//
////
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

//    private void getCount(){
//        mCount = FirebaseDatabase.getInstance().getReference("Customer");
//        mCount.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                id = dataSnapshot.getChildrenCount();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
