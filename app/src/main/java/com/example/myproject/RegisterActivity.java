package com.example.myproject;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText editEmail;
    private EditText editPassword;
    private EditText confirmpass;
    private EditText firstname;
    private EditText lastname;
    private Button buttonFinish;
    private DatabaseReference mDatabaseReff;
    private FirebaseDatabase db;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = (EditText) findViewById(R.id.editemail);
        editPassword = (EditText) findViewById(R.id.editpassword);
        confirmpass = (EditText) findViewById(R.id.cfpassword);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        buttonFinish = findViewById(R.id.buttonFinish);

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this,"xxxxx",Toast.LENGTH_SHORT);
                String Email = editEmail.getText().toString().trim();
                String Password = editPassword.getText().toString().trim();

                if(!Email.isEmpty() && !Password.isEmpty()){
                    registerUserToFirebase();
                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                }

            }
        });
    }

    private void getValue(){
        user.setFname(firstname.getText().toString().trim());
        user.setLname(lastname.getText().toString().trim());
        user.setEmail(editEmail.getText().toString().trim());
        user.setPass(editPassword.getText().toString().trim());
    }


    private void registerUserToFirebase(){

        final String Email = editEmail.getText().toString().trim();
        final String Password = editPassword.getText().toString().trim();

        firebaseAuth = FirebaseAuth.getInstance();


        firebaseAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            //เรียกดาต้าเบสมาใช้ vvv
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert  firebaseUser != null;
                            final String userid = firebaseUser.getUid();

                            db = FirebaseDatabase.getInstance();
                            mDatabaseReff = db.getReference("User");
                            user = new User();


                            mDatabaseReff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    getValue();
                                    mDatabaseReff.child(userid).setValue(user);
                                    Toast.makeText(RegisterActivity.this,"ลงทะเบียนสำเร็จ",Toast.LENGTH_SHORT).show();
                                    finish(); //ปิดหน้าปัจจุบัน
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                        else{

                            AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                            dialog.setTitle("ลงทะเบียนไม่สำเร็จ");
                            dialog.setMessage("กรุณากรอกข้อมูลให้ครบ");
                            dialog.setPositiveButton("ยืนยัน",null);
                            dialog.show();
                        }
                    }
                });

    }
}