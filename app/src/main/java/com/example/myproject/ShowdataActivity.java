package com.example.myproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowdataActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    ListView listView;
    private Button nextto_adddata;
    ArrayList<String> cusName = new ArrayList<>();
    ArrayList<String> cusData = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdata);

        listView = findViewById(R.id.listview_cus);
        nextto_adddata = findViewById(R.id.nextto_adddata);

        nextto_adddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowdataActivity.this, AddDataActivity.class);
                startActivity(intent);
            }
        });

        listCustomer();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShowdataActivity.this, MapsActivity.class);
                intent.putExtra("Location", location.get(position)); // send location to map
                intent.putExtra("CustomerName",cusName.get(position));
                startActivity(intent);
            }
        });
    }


    private void listCustomer() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Customer");

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Customer value = dataSnapshot.getValue(Customer.class);
                cusName.add(value.toString());
                cusData.add(value.address());
                location.add(value.getlocation());
                CustomAdapter adapter = new CustomAdapter(getApplicationContext(),cusName,cusData);
                listView.setAdapter(adapter);
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


}
