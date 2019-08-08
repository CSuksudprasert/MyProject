package com.example.myproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddDataActivity extends AppCompatActivity {
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
    private DatabaseReference mDatabaseReff;
    private DatabaseReference mdata;
    private FirebaseDatabase db;
    Customer cus;
    public static int id = 0;

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

        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdd();
            }
        });

    }
    private void getValue(){
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

    }
    private void setAdd(){
        String cus_id = String.valueOf(id);
        db = FirebaseDatabase.getInstance();
        mDatabaseReff = db.getReference("Customer");
        cus = new Customer();
        mdata =  mDatabaseReff.child(cus_id);

        mDatabaseReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getValue();
                mdata.setValue(cus);
                Toast.makeText(AddDataActivity.this,"เพิ่มข้อมูลสำเร็จ",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        id++;

    }
}
