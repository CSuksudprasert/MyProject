package com.example.myproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddDataActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1 ;
    private EditText editTextcus_fname;
    private EditText editTextcus_lname;
    private EditText editText_number;
    private EditText editText_drom;
    private EditText editText_roomnum;
    private EditText editText_floor;
    private EditText editText_group;
    private EditText editText_road;
    private EditText editText_alley;
    private EditText editText_subdistrict;
    private EditText editText_district;
    private EditText editText_province;
    private EditText editText_code;
    private Button addData;
    private Button location_button;
    private DatabaseReference mDatabaseReff,mdata,mCount;
    private LocationManager locationManager;
    private TextView latitude_textview;
    private TextView longtitude_txetview;
    Customer cus;
    long id;
    private FusedLocationProviderClient fusedLocationClient;
    String cus_fname,cus_lname,number,drom,roomnum,floor,group,road,alley,subdistrict,district,provice,code,cus_id;
    boolean check = false;

    ArrayList<String> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        getSupportActionBar().hide();  //ทำให้บาข้างบนสุดหายไป
        editTextcus_fname = findViewById(R.id.edittext_cus_fname);
        editTextcus_lname = findViewById(R.id.edittext_cus_lname);
        editText_number = findViewById(R.id.edittxet_number);
        editText_drom = findViewById(R.id.edittxet_drom);
        editText_roomnum = findViewById(R.id.edittxet_roomnum);
        editText_floor = findViewById(R.id.edittxet_floor);
        editText_group = findViewById(R.id.edittext_group);
        editText_road = findViewById(R.id.edittext_road);
        editText_alley = findViewById(R.id.edittext_alley);
        editText_subdistrict = findViewById(R.id.edittext_subdistrict);
        editText_district = findViewById(R.id.edittext_district);
        editText_province = findViewById(R.id.edittext_province);
        editText_code = findViewById(R.id.edittext_code);
        addData = findViewById(R.id.addData);
        location_button = findViewById(R.id.location);
        latitude_textview = findViewById(R.id.latitude_textview);
        longtitude_txetview = findViewById(R.id.longtitude_textview);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //buildAlertMessageNoGps();
        getCount();
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cus_fname = editTextcus_fname.getText().toString();
                cus_lname = editTextcus_lname.getText().toString();
                number = editText_number.getText().toString();
                drom = editText_drom.getText().toString();
                roomnum = editText_roomnum.getText().toString();
                floor = editText_floor.getText().toString();
                group = editText_group.getText().toString();
                road = editText_road.getText().toString();
                alley = editText_alley.getText().toString();
                subdistrict = editText_subdistrict.getText().toString();
                district = editText_district.getText().toString();
                provice = editText_province.getText().toString();
                code = editText_code.getText().toString();


                if(cus_fname.isEmpty() || cus_lname.isEmpty() || number.isEmpty()|| drom.isEmpty() ||roomnum.isEmpty()||
                    floor.isEmpty() || group.isEmpty() || road.isEmpty() ||alley.isEmpty() ||subdistrict.isEmpty()||
                    district.isEmpty() || provice.isEmpty() || code.isEmpty() || !check){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddDataActivity.this)
                            .setTitle("กรุณากรอกข้อมูลให้ครบถ้วน")
                            .setMessage("หากไม่มีข้อมูลให้ใส่ '' - '' ในข้อมูลแทน")
                            .setPositiveButton("ยืนยัน",null);
                    dialog.show();
                }
                else{
                    setAdd();
                }

            }
        });

        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 fetchLocation();

            }
        });

    }

    private void fetchLocation() {
        if (ContextCompat.checkSelfPermission(AddDataActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddDataActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this)
                        .setTitle("Required Location Permission")
                        .setMessage("You have to give this permission to acess the feature")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(AddDataActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                            }
                        })
                        .setNegativeButton("Cancael", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(AddDataActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                double latitude = location.getLatitude();
                                double longtitude = location.getLongitude();

                                latitude_textview.setText("Latitude : " + latitude );
                                longtitude_txetview.setText("Longtitude : " + longtitude);
                            }
                        }
                    });
        }
        check = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
            else{

            }
        }
    }

    private void getValue() {
        cus.setCus_fname(editTextcus_fname.getText().toString().trim());
        cus.setCus_lname(editTextcus_lname.getText().toString().trim());
        cus.setNumber(editText_number.getText().toString().trim());
        cus.setDrom(editText_drom.getText().toString().trim());
        cus.setRoomnum(editText_roomnum.getText().toString().trim());
        cus.setFloor(editText_floor.getText().toString().trim());
        cus.setGroup(editText_group.getText().toString().trim());
        cus.setRoad(editText_road.getText().toString().trim());
        cus.setAlley(editText_alley.getText().toString().trim());
        cus.setSubdistrict(editText_subdistrict.getText().toString().trim());
        cus.setDistrict(editText_district.getText().toString().trim());
        cus.setProvince(editText_province.getText().toString().trim());
        cus.setCode(editText_code.getText().toString().trim());
        cus.setLatitude(latitude_textview.getText().toString());
        cus.setLongtitude(longtitude_txetview.getText().toString());

    }

    private void setAdd() {
        cus_id = String.valueOf(id);
        mDatabaseReff = FirebaseDatabase.getInstance().getReference("Customer");
        cus = new Customer();
        mdata = mDatabaseReff.child(cus_id);

        mDatabaseReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getValue();
                mdata.setValue(cus);
                Toast.makeText(AddDataActivity.this, "เพิ่มข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getCount(){
        mCount = FirebaseDatabase.getInstance().getReference("Customer");
        mCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id = dataSnapshot.getChildrenCount()+1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    //ขอให้เปิด gps
//    protected void buildAlertMessageNoGps() {
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("ต้องการเปิดตำแหน่งที่อยู่ของคุณหรือไม่")
//                .setCancelable(false)
//                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }



}
