package com.example.myproject.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myproject.R;
import com.example.myproject.model.CustomAdapter;
import com.example.myproject.model.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class listOfSendActivity extends AppCompatActivity {
    ListView listview_forsend;
    TextView userEmail;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ArrayList<Customer> cusData = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    ArrayList<String> key = new ArrayList<>();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_send);

        listview_forsend = findViewById(R.id.listview_forsend);
        userEmail = findViewById(R.id.text_email);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        userEmail.setText(firebaseUser.getEmail());

        uid = firebaseUser.getUid();

        System.out.println("UID : " + uid);
        listCustomer();

        registerForContextMenu(listview_forsend);

        listview_forsend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(listOfSendActivity.this, MapsActivity.class);
                intent.putExtra("Location", location.get(position)); // send location to map
                intent.putExtra("CustomerName", cusData.get(position).toString());
                // intent.putExtra("CusAddress",cusData.get(position));
                startActivity(intent);
            }
        });


    }

    private void listCustomer() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ListSave").child(uid);


//        System.out.println("listCustomer : ----------");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Customer value = snapshot.getValue(Customer.class);
                    cusData.add(value);
                    location.add(value.getlocation());
                    key.add(snapshot.getKey());
                }
//                cusName.add(value.name());

                CustomAdapter adapter = new CustomAdapter(getApplicationContext(), cusData);
                listview_forsend.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.meun_delete, menu);

    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index = info.position;
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ListSave").child(uid);

        switch (item.getItemId()) {
            case R.id.delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage("ต้องการลบออกจากบันทึกหรือไม่")
                        .setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String k = key.get(index);
                                System.out.println("key >> " + k);
                                deleteData(k);
                            }
                        });

                builder.setNegativeButton("ยกเลิก", null);
                builder.show();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void deleteData(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ListSave").child(uid).child(id);

        databaseReference.removeValue();

        Toast.makeText(listOfSendActivity.this, "ลบข้อมูลสำเร็จ", Toast.LENGTH_SHORT).show();
    }
}
