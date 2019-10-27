package com.example.myproject.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myproject.R;
import com.example.myproject.model.Customer;
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

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QRcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA = 1;
    DatabaseReference mdata, mCount;
    Customer cus;
    String cus_id;
    long id;
    private FusedLocationProviderClient fusedLocationClient;
    boolean check = false;
    String cus_fname, cus_lname, number, drom, roomnum, floor, group, road, alley, subdistrict, district, provices, code;
    double latitude, longtitude;
    ArrayList<Customer> value = new ArrayList<>();
    boolean ch = false;
    String ch_num = null, ch_road = null, ch_drom = null, ch_roomnum = null, ch_floor = null, ch_group = null, ch_provices = null, ch_alley = null, ch_subdistrict = null, ch_district = null, ch_code = null;

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
        final String result = rawResult.getText();
        final String[] cusAddress = result.split(" ");


        final DatabaseReference mDatabaseReff = FirebaseDatabase.getInstance().getReference().child("Customer");

        mDatabaseReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Customer cus = snapshot.getValue(Customer.class);
                    value.add(cus);
                }

//                for (int i = 0; i < value.size(); i++) {
//                    System.out.println("II : "+i);
//                    System.out.println(value.get(i).getNumber());
//                    System.out.println(value.get(i).getCus_fname());
//                    System.out.println(value.get(i).getCus_lname());
//                    System.out.println(value.get(i).getAlley());
//                    System.out.println(value.get(i).getCode());
//                    System.out.println(value.get(i).getDistrict());
//                    System.out.println(value.get(i).getDrom());
//                    System.out.println(value.get(i).getFloor());
//                    System.out.println(value.get(i).getGroup());
//                    System.out.println(value.get(i).getLatitude());
//                    System.out.println(value.get(i).getLongtitude());
//                    System.out.println(value.get(i).getProvince());
//                    System.out.println(value.get(i).getRoad());
//                    System.out.println(value.get(i).getRoomnum());
//                    System.out.println(value.get(i).getSubdistrict());
//                    System.out.println("-------------------------");
//
//                }
                checkCus(cusAddress);
                showDialod(result);
//                System.out.println("เเข้าตรงนี้นะจ๊ะ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //System.out.println("handleResult" + value.get(0).getNumber());


    }

    public void showDialod(String result){

        final String[] cusAddress = result.split(" ");
        //Toast.makeText(getApplicationContext(),"เชื่อมได้แล้วนะ",Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(result);
        if (ch) {
            getCount();
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // ถ้า checkCus ทั้งหมดตรงกับค่าในดาต้าเบส จะไปที่แผนที่
                    cus_fname = cusAddress[0];
                    cus_lname = cusAddress[1];
                    //n = cus_fname + cus_lname;
                    for (int i = 2; i < cusAddress.length; i++) {
                        if (cusAddress[i].contains("บ้านเลขที่") || cusAddress[i].contains("เลขที่")) {
                            number = cusAddress[++i];
                            n = n + number;
                        }
                        if (cusAddress[i].contains("ถนน") || cusAddress[i].contains("ถ.")) {
                            road = cusAddress[i + 1];
                            n = n + road;
                        }
                        if (cusAddress[i].contains("หอพัก")) {
                            drom = cusAddress[i + 1];
                            n = n + drom;
                        }

                        if (cusAddress[i].contains("เลขที่ห้อง") || cusAddress[i].contains("ห้อง")) {
                            roomnum = cusAddress[i + 1];
                            n = n + roomnum;
                        }

                        if (cusAddress[i].contains("ชั้น")) {
                            floor = cusAddress[i + 1];
                            n = n + floor;
                        }

                        if (cusAddress[i].contains("หมู่") || cusAddress[i].contains("ม.")) {
                            group = cusAddress[i + 1];
                            n = n + group;
                        }

                        if (cusAddress[i].contains("จังหวัด") || cusAddress[i].contains("จ.")) {
                            provices = cusAddress[i + 1];
                            n = n + provices;
                        }

                        if (cusAddress[i].contains("ซอย") || cusAddress[i].contains("ซ.")) {
                            alley = cusAddress[i + 1];
                            n = n + alley;
                        }

                        if (cusAddress[i].contains("ตำบล") || cusAddress[i].contains("ต.")) {
                            subdistrict = cusAddress[i + 1];
                            n = n + subdistrict;
                        }

                        if (cusAddress[i].contains("อำเภอ") || cusAddress[i].contains("อ.")) {
                            district = cusAddress[i + 1];
                            n = n + district;
                        }

                        if (cusAddress[i].contains("รหัสไปรษณีย์")) {
                            code = cusAddress[i + 1];
                            n = n + code;
                        }
                    }

                    System.out.println(n);
                    //กดตกลงแล้วจะกลับมาหน้ากล้อง
                    fetchLocation();
                    setAdd();

                    mScannerView.resumeCameraPreview((QRcodeActivity.this));
                }
            })
                    .show();
        } else {
            //AlertDialog.Builder builders = new AlertDialog.Builder(this);
            builder.setTitle("มีข้อมูลอยู่แล้ว")
                    .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            QRcodeActivity.this.finish();
                        }
                    })
                    .show();
        }
    }

    //เช็คว่ารายชื่อมีอยู่แล้วหรือยัง ถ้ามีแล้วให้ไปหน้าmap ถ้ายังจะเพิ่มข้อมูล

    public void checkCus(String[] cusAddress) {

        for (int i = 2; i < cusAddress.length; i++) {

            //System.out.println("*****");
            if (cusAddress[i].contains("บ้านเลขที่") || cusAddress[i].contains("เลขที่")) {
                ch_num = cusAddress[++i];
            }
            if (cusAddress[i].contains("ถนน") || cusAddress[i].contains("ถ.")) {
                ch_road = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("หอพัก") || cusAddress[i].contains("หอ")) {
                ch_drom = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("เลขที่ห้อง") || cusAddress[i].contains("ห้อง")) {
                ch_roomnum = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("ชั้น")) {
                ch_floor = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("หมู่") || cusAddress[i].contains("ม.")) {
                ch_group = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("จังหวัด") || cusAddress[i].contains("จ.")) {
                ch_provices = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("ซอย") || cusAddress[i].contains("ซ.")) {
                ch_alley = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("ตำบล") || cusAddress[i].contains("ต.")) {
                ch_subdistrict = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("อำเภอ") || cusAddress[i].contains("อ.")) {
                ch_district = cusAddress[i + 1];
            }
            if (cusAddress[i].contains("รหัสไปรษณีย์")) {
                ch_code = cusAddress[i + 1];
            }
        }

        //System.out.println(value.size());
        for (int i = 0; i < value.size(); i++) {
//            System.out.println("++++");
//            System.out.println("size : " + value.size());
//            System.out.println("name - "+value.get(i).getCus_fname() +" "+ cusAddress[0]);
//            System.out.println("lname - "+value.get(i).getCus_lname()+" "+ cusAddress[1]);
//            System.out.println("sub - "+ value.get(i).getSubdistrict()+" "+ ch_subdistrict);
//            System.out.println("num "+value.get(i).getNumber() +" "+ ch_num);

            if (value.get(i).getCus_fname().equals(cusAddress[0]) && value.get(i).getCus_lname().equals(cusAddress[1])) {
                if (value.get(i).getSubdistrict().contains(ch_subdistrict)) {
                    System.out.println("IN1");
                    if (value.get(i).getNumber().contains(ch_num)) {
                        System.out.println("IN2");
                        ch = false;
                        break;
                    } else {
                        ch = true;
                        break;
                    }
                } else {
                    ch = true;
                    break;
                }

            } else {
                ch = true;
                //break;
            }

        }
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
        final DatabaseReference mDatabaseReff = FirebaseDatabase.getInstance().getReference("Customer");
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
