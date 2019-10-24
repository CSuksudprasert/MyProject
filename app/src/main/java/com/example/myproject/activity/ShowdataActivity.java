package com.example.myproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.myproject.R;
import com.example.myproject.model.CustomAdapter;
import com.example.myproject.model.Customer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.miguelcatalan.materialsearchview.MaterialSearchView;


import java.util.ArrayList;

public class ShowdataActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    ListView listView;
    private Button nextto_adddata;
    ArrayList<String> cusName = new ArrayList<>();
    ArrayList<String> cusData = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    MaterialSearchView searchView;
    ImageButton qrbuttom, toallmap;
    android.support.v7.widget.Toolbar toolbar;
    ImageView imageView;
    MultiFormatWriter multi = new MultiFormatWriter();
    // SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showdata);

        listView = findViewById(R.id.listview_cus);
        nextto_adddata = findViewById(R.id.nextto_adddata);
        toolbar = findViewById(R.id.toolbar);
        qrbuttom = findViewById(R.id.qrcode);
        toallmap = findViewById(R.id.buttonto_allmap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("      รายชื่อ");
        //imageView = findViewById(R.id.iv_qrcode);

        searchView = findViewById(R.id.search_view);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

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
                intent.putExtra("CustomerName", cusName.get(position));
                // intent.putExtra("CusAddress",cusData.get(position));
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);


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
                if (newText != null && !newText.isEmpty()) {
                    list = new ArrayList<>();
                    for (String item : cusName) {
                        if (item.contains(newText)) {
                            list.add(item);
                        }
                    }
                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(), list, cusData);
                    listView.setAdapter(adapter);
                } else {
                    //if search text is null
                    // return defaulf
                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(), cusName, cusData);
                    listView.setAdapter(adapter);
                }
                return true;
            }
        });

        qrbuttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowdataActivity.this, QRcodeActivity.class);
                startActivity(intent);
            }
        });

        toallmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowdataActivity.this, allMapActivity.class);
                startActivity(intent);
            }
        });

//        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                listCustomer();
//            }
//        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
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
                cusName.add(value.name());
                cusData.add(value.address());
                location.add(value.getlocation());
                CustomAdapter adapter = new CustomAdapter(getApplicationContext(), cusName, cusData);
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

    //กดค้างให้ตัวเลือกขึ้น

    String title;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.chioce_menu, menu);

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.qrcode:
                //String cus_item = item.toString();
               // String nameList = listView.getTransitionName();
                System.out.println();
                MyCustomDialog();

                return true;
            default:
                return super.onContextItemSelected(item);

        }

    }

    public void MyCustomDialog(String cus_item) {
//        Dialog myDialog = new Dialog(ShowdataActivity.this);
//        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        myDialog.setContentView(R.layout.show_qrcode);

        setContentView(R.layout.show_qrcode);
        imageView = findViewById(R.id.iv_qrcode);


        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        int x = point.x;
        int y = point.y;

        int icon = x < y ? x : y;
        icon = icon * 3 / 4;


        try {

            BitMatrix bitmatrix = multi.encode(cus_item,BarcodeFormat.QR_CODE,300,300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitmatrix);
            imageView.setImageBitmap(bitmap);
            //Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            System.out.println(bitmap);
            Log.e("Bitmap", String.valueOf(bitmap));
//            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //***ตรงนี้ยังผิดอยู่จ้าาา *****
       // myDialog.show();
    }


}
