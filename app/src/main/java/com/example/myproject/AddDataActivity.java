package com.example.myproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    private Spinner spinner_province;
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
    String cus_fname,cus_lname,number,drom,roomnum,floor,group,road,alley,subdistrict,district,provices,code,cus_id,prov;
    boolean check = false;
    double latitude,longtitude;

    ArrayList<String> provinceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

       // getSupportActionBar().hide();  //ทำให้บาข้างบนสุดหายไป
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
        editText_province=findViewById(R.id.edittext_province);
        //spinner_province = findViewById(R.id.edittext_province);
        editText_code = findViewById(R.id.edittext_code);
        addData = findViewById(R.id.addData);
        location_button = findViewById(R.id.location);
        latitude_textview = findViewById(R.id.latitude_textview);
        longtitude_txetview = findViewById(R.id.longtitude_textview);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        //เก็บข้อมูลลง firebase
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
                provices = editText_province.getText().toString();
                //provices = spinner_province.toString();
                code = editText_code.getText().toString();

//                createProvice();
//                final ArrayAdapter<String> adapter_province = new ArrayAdapter<String>(AddDataActivity.this,
//                        android.R.layout.simple_dropdown_item_1line,provinceList);
//
//                spinner_province.setAdapter(adapter_province);
//                spinner_province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        prov = provinceList.get(position);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });


                if(cus_fname.isEmpty() || cus_lname.isEmpty() || number.isEmpty()|| drom.isEmpty() ||roomnum.isEmpty()||
                    floor.isEmpty() || group.isEmpty() || road.isEmpty() ||alley.isEmpty() ||subdistrict.isEmpty()||
                    district.isEmpty() || provices.isEmpty()|| code.isEmpty() || !check){

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


//ขอพิกัดปัจจุบัน
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
                                latitude = location.getLatitude();
                                longtitude = location.getLongitude();

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
        //cus.setProvince(prov);
        cus.setCode(editText_code.getText().toString().trim());
        cus.setLatitude(String.valueOf(latitude));
        cus.setLongtitude(String.valueOf(longtitude));

    }

    private void setAdd() {
        cus_id = String.valueOf(id);

        mDatabaseReff = FirebaseDatabase.getInstance().getReference("Customer");
        //mDatabaseReff.child("Customer").push().setValue()
       //String cus_id = mDatabaseReff.getKey();
        cus = new Customer();
        mdata = mDatabaseReff.child(cus_id);

        mDatabaseReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getValue();
                mdata.setValue(cus);
                //mDatabaseReff.child("Customer").push().setValue(cus);
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

    private void createProvice(){
        provinceList.add("กรุงเทพมหานคร ");
        provinceList.add("กระบี่");
        provinceList.add("กาญจนบุรี");
        provinceList.add("กาฬสินธุ์");
        provinceList.add("กำแพงเพชร");
        provinceList.add("ขอนแก่น");
        provinceList.add("จันทบุรี");
        provinceList.add("ฉะเชิงเทรา");
        provinceList.add("ชลบุรี");
        provinceList.add("ชัยนาท");
        provinceList.add("ชัยภูมิ");
        provinceList.add("ชุมพร");
        provinceList.add("เชียงราย");
        provinceList.add("เชียงใหม่");
        provinceList.add("ตรัง");
        provinceList.add("ตราด");
        provinceList.add("ตาก");
        provinceList.add("นครนายก");
        provinceList.add("นครปฐม");
        provinceList.add("นครราชสีมา");
        provinceList.add("นครศรีธรรมราช");
        provinceList.add("นครสวรรค์");
        provinceList.add("นนทบุรี");
        provinceList.add("นราธิวาส");
        provinceList.add("น่าน");
        provinceList.add("บึงกาฬ");
        provinceList.add("บุรีรัมย์");
        provinceList.add("ปทุมธานี");
        provinceList.add("ประจวบคีรีขันธ์");
        provinceList.add("ปราจีนบุรี");
        provinceList.add("ปัตตานี");
        provinceList.add("พระนครศรีอยุธยา");
        provinceList.add("พังงา");
        provinceList.add("พัทลุง");
        provinceList.add("พิจิตร");
        provinceList.add("พิษณุโลก");
        provinceList.add("เพชรบุรี");
        provinceList.add("เพชรบูรณ์");
        provinceList.add("แพร่");
        provinceList.add("พะเยา");
        provinceList.add("ภูเก็ต");
        provinceList.add("มหาสารคาม");
        provinceList.add("มุกดาหาร");
        provinceList.add("แม่ฮ่องสอน");
        provinceList.add("ยะลา");
        provinceList.add("ยโสธร");
        provinceList.add("ร้อยเอ็ด");
        provinceList.add("ระนอง ");
        provinceList.add("ระยอง");
        provinceList.add("ราชบุรี ");
        provinceList.add("ลพบุรี ");
        provinceList.add("ลำปาง");
        provinceList.add("ลำพูน");
        provinceList.add("เลย");
        provinceList.add("ศรีสะเกษ");
        provinceList.add("สกลนคร ");
        provinceList.add("สงขลา ");
        provinceList.add("สตูล");
        provinceList.add("สมุทรปราการ");
        provinceList.add("สมุทรสงคราม");
        provinceList.add("สมุทรสาคร");
        provinceList.add("สระแก้ว");
        provinceList.add("สระบุรี");
        provinceList.add("สิงห์บุรี");
        provinceList.add("สุโขทัย");
        provinceList.add("สุพรรณบุรี");
        provinceList.add("สุราษฎร์ธานี");
        provinceList.add("สุรินทร์");
        provinceList.add("หนองคาย");
        provinceList.add("หนองบัวลำภู");
        provinceList.add("อ่างทอง");
        provinceList.add("อุดรธานี");
        provinceList.add("อุทัยธานี");
        provinceList.add("อุตรดิตถ์");
        provinceList.add("อุบลราชธานี");
        provinceList.add("อำนาจเจริญ");
    }

}
