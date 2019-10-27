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
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myproject.R;
import com.example.myproject.model.CustomAdapter;
import com.example.myproject.model.Customer;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    ArrayList<Customer> cusData = new ArrayList<>();
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
                intent.putExtra("CustomerName", cusData.get(position).toString());
                // intent.putExtra("CusAddress",cusData.get(position));
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);



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

    public void search(){

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
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Customer> list;
                if (newText != null && !newText.isEmpty()) {
                    list = new ArrayList<>();
                    for (Customer item : cusData) {
                        System.out.println("DATA222 >> "+item.name());
                        System.out.println("QUERY222 >> "+newText);
                        System.out.println("-----------");
                        if (item.name().contains(newText)) {
                            System.out.println("IN");
                            list.add(item);
                            System.out.println("********");
                        }
                    }
                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(), list);
                    listView.setAdapter(adapter);
                } else {
                    //if search text is null
                    // return defaulf
                    CustomAdapter adapter = new CustomAdapter(getApplicationContext(), cusData);
                    listView.setAdapter(adapter);
                }
                return true;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;

    }


    private void listCustomer() {

        final DatabaseReference mDatabaseReff = FirebaseDatabase.getInstance().getReference("Customer");

        mDatabaseReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Customer value = snapshot.getValue(Customer.class);
                    cusData.add(value);
                    location.add(value.getlocation());
                }
//                cusName.add(value.name());

                CustomAdapter adapter = new CustomAdapter(getApplicationContext(), cusData);
                listView.setAdapter(adapter);
                search();

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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();



        int index = info.position; //บอกตำแหน่งของ listview ที่กด
        //item.toString();  // ออกมาเป็นคำว่า "คิวอาร์โค้ด"

        switch (item.getItemId()) {
            case R.id.qrcode:
                // String lname = (String)
                String data =   cusData.get(index).name()+" "+ cusData.get(index).address();
                System.out.println("DATA >>"+data);

                //listView.getItemAtPosition(info.position);

                System.out.println("index : "  );
                MyCustomDialog(data);

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
//
//        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
//        Point point = new Point();
//        display.getSize(point);
//
//        int x = point.x;
//        int y = point.y;
//
//        int icon = x < y ? x : y;
//        icon = icon * 3 / 4;


        try {
            BitMatrix bitmatrix = multi.encode(cus_item, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitmatrix);
            imageView.setImageBitmap(bitmap);
            //Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            System.out.println("BITMAP >> "+ bitmap);
            Log.e("Bitmap", String.valueOf(bitmap));
//            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // myDialog.show();
    }


}
