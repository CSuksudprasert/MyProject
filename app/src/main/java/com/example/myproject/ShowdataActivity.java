package com.example.myproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShowdataActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    ListView listView;
    private Button nextto_adddata;
    ArrayList<String> cusName = new ArrayList<>();
    ArrayList<String> cusData = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    MaterialSearchView searchView;
    ImageButton qrbuttom;
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdata);

        listView = findViewById(R.id.listview_cus);
        nextto_adddata = findViewById(R.id.nextto_adddata);
        toolbar = findViewById(R.id.toolbar);
        qrbuttom = findViewById(R.id.qrcode);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("      รายชื่อ");

        searchView = findViewById(R.id.search_view);
        toolbar.setTitleTextColor(Color.parseColor("#000000"));

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
               // intent.putExtra("CusAddress",cusData.get(position));
                startActivity(intent);
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> list;
                if(newText != null && !newText.isEmpty()){
                    list = new ArrayList<>();
                    for(String item:cusName){
                        if(item.contains(newText)){
                            list.add(item);
                        }
                    }
                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(),list,cusData);
                    listView.setAdapter(adapter);
                }
                else{
                    //if search text is null
                    // return defaulf
                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(),cusName,cusData);
                    listView.setAdapter(adapter);
                }
                return true;
            }
        });

        qrbuttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowdataActivity.this,QRcodeActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;

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
