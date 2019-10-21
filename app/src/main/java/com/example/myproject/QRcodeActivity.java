package com.example.myproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.example.myproject.AddDataActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QRcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;
    DatabaseReference mDatabaseReff, mdata, mCount;
    Customer value, cus;
    String cus_id;
    long id;
    private FusedLocationProviderClient fusedLocationClient;
    boolean check = false;
    String cus_fname, cus_lname, number, drom, roomnum, floor, group, road, alley, subdistrict, district, provices, code;
    double latitude, longtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        int currentapiVertion = Build.VERSION.SDK_INT;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (currentapiVertion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "สแกนคิวอาร์โค้ด", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions();
            }
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "ขออนุญาติเข้าถึงการใช้งานกล้องถ่ายภาพ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "ไม่อนุญาติให้เข้าถึงกล้องถ่ายภาพ", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("คุณต้องการอนุญาติให้สามารถเข้าถึงการใช้งานกล้องถ่ายภาพหรือไม่",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(QRcodeActivity.this)
                .setMessage(message)
                .setPositiveButton("ตกลง", okListener)
                .setNegativeButton("ยกเลิก", null)
                .create()
                .show();
    }


    public void onResume() {
        super.onResume();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (mScannerView == null) {
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler((ZXingScannerView.ResultHandler) this);
                mScannerView.startCamera();

            } else {
                requestPermissions();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }

    //อ่าน QRcode
    String n = "";

    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();
        final String[] cusAddress = result.split(" ");
        //Toast.makeText(getApplicationContext(),"เชื่อมได้แล้วนะ",Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

         builder.setMessage(result);

        getCount();
        builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                checkCus();
                // ถ้า checkCus ทั้งหมดตรงกับค่าในดาต้าเบส จะไปที่แผนที่
                cus_fname = cusAddress[0];
                cus_lname = cusAddress[1];
                //n = cus_fname + cus_lname;
                for (int i = 2; i < cusAddress.length; i++) {
                    if (cusAddress[i].contains("บ้านเลขที่")|| cusAddress[i].contains("เลขที่")) {
                        number = cusAddress[++i];
                        n = n + number;
                    }
                    if (cusAddress[i].equals("ถนน")) {
                        road = cusAddress[i+1];
                        n = n + road;
                    }
                    if (cusAddress[i].contains("หอพัก")) {
                        drom = cusAddress[i + 1];
                        n = n + drom;
                    }

                    if (cusAddress[i].contains("เลขที่ห้อง")||cusAddress[i].contains("ห้อง")) {
                        roomnum = cusAddress[i + 1];
                        n = n + roomnum;
                    }

                    if (cusAddress[i].contains("ชั้น")) {
                        floor = cusAddress[i + 1];
                        n = n + floor;
                    }

                    if (cusAddress[i].contains("หมู่")) {
                        group = cusAddress[i + 1];
                        n = n + group;
                    }

                    if (cusAddress[i].contains("จังหวัด")) {
                        provices = cusAddress[i + 1];
                        n = n + provices;
                    }

                    if (cusAddress[i].contains("ซอย")) {
                        alley = cusAddress[i + 1];
                        n = n + alley;
                    }

                    if (cusAddress[i].contains("ตำบล")) {
                        subdistrict = cusAddress[i + 1];
                        n = n + subdistrict;
                    }

                    if (cusAddress[i].contains("อำเภอ")) {
                        district = cusAddress[i + 1];
                        n = n + district;
                    }

                    if (cusAddress[i].contains("รหัสไปรษณีย์")) {
                        code = cusAddress[i + 1];
                        n = n + code;
                    }
                }

//        for (String i : cusAddress) {
//            n += i;
//        }
                System.out.println(n);
                //กดตกลงแล้วจะกลับมาหน้ากล้อง
                fetchLocation();
                setAdd();

                mScannerView.resumeCameraPreview((QRcodeActivity.this));
            }
        })
                .show();

    }

    //เช็คว่ารายชื่อมีอยู่แล้วหรือยัง ถ้ามีแล้วให้ไปหน้าmap ถ้ายังจะเพิ่มข้อมูล

    public void checkCus() {
        mDatabaseReff = FirebaseDatabase.getInstance().getReference("Customer");

        mDatabaseReff.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                value = dataSnapshot.getValue(Customer.class);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //ขอพิกัดปัจจุบัน
    public void fetchLocation() {
        if (ContextCompat.checkSelfPermission(QRcodeActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(QRcodeActivity.this,
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
                                ActivityCompat.requestPermissions(QRcodeActivity.this,
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
                ActivityCompat.requestPermissions(QRcodeActivity.this,
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
                                latitude = location.getLatitude();
                                longtitude = location.getLongitude();

//                                latitude_textview.setText("Latitude : " + latitude );
//                                longtitude_txetview.setText("Longtitude : " + longtitude);
                            }
                        }
                    });
        }
        check = true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }


    private void getValue() {
        cus.setCus_fname(cus_fname);
        cus.setCus_lname(cus_lname);
        cus.setNumber(number);
        cus.setDrom(drom);
        cus.setRoomnum(roomnum);
        cus.setFloor(floor);
        cus.setGroup(group);
        cus.setRoad(road);
        cus.setAlley(alley);
        cus.setSubdistrict(subdistrict);
        cus.setDistrict(district);
        cus.setProvince(provices);
        //cus.setProvince(prov);
        cus.setCode(code);
        cus.setLatitude(String.valueOf(latitude));
        cus.setLongtitude(String.valueOf(longtitude));

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
                Toast.makeText(QRcodeActivity.this, "เพิ่มข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getCount() {
        mCount = FirebaseDatabase.getInstance().getReference("Customer");
        mCount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                id = dataSnapshot.getChildrenCount() + 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
