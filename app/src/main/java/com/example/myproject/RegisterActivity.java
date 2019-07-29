package com.example.myproject;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String email,pass,cfpass,fname,lname ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editEmail = (EditText) findViewById(R.id.editemail);
        editPassword = (EditText) findViewById(R.id.editpassword);
        confirmpass = (EditText) findViewById(R.id.cfpassword);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        buttonFinish = (Button) findViewById(R.id.buttonFinish);


        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editEmail.getText().toString();
                pass = editPassword.getText().toString();
                cfpass = confirmpass.getText().toString();
                fname = firstname.getText().toString();
                lname = lastname.getText().toString();

                if(email.isEmpty() || pass.isEmpty() || cfpass.isEmpty() || fname.isEmpty() || lname.isEmpty() ){
                    //Toast.makeText(RegisterActivity.this,"กรุณากรอกข้อมูลให้ครบถ้วน",Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder dialog = new AlertDialog.Builder(RegisterActivity.this);
                    dialog.setTitle("ลงทะเบียนไม่สำเร็จ");
                    dialog.setMessage("กรุณากรอกข้อมูลให้ครบ");
                    dialog.setPositiveButton("ยืนยัน",null);
                    dialog.show();
                }

                else{
                    if(checkPassword() && checkEmail()){
                        registerUserToFirebase();
                    }
                    else {
                        checkEmail();
                        checkPassword();
                    }

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

        email= editEmail.getText().toString().trim();
        pass = editPassword.getText().toString().trim();

        firebaseAuth = FirebaseAuth.getInstance();


        firebaseAuth.createUserWithEmailAndPassword(email,pass)
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
                    }
                });

    }

    private boolean checkPassword(){
        pass = editPassword.getText().toString().trim();
        cfpass = confirmpass.getText().toString().trim();
        if(pass.length() >= 8 && pass.equals(cfpass)){
            return true;
        }
        else{
            Toast.makeText(RegisterActivity.this,"รหัสผ่านไม่ถูกต้อง", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkEmail(){
        email = editEmail.getText().toString().trim();
        if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }
        else {
            Toast.makeText(RegisterActivity.this,"รูปแบบอีเมล์ไม่ถูกต้อง",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

//    //แก้ตรวนี้
//    private void checkcfPassword(){
//        mDatabaseReff = FirebaseDatabase.getInstance().getReference();
//        final String cfpass = confirmpass.getText().toString().trim();
//        final List<String> Users = new ArrayList<>();
//        final boolean[] ch = {true};
//
//        mDatabaseReff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Users.clear();
//                List<String> keys = new ArrayList<>();
//
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    keys.add(snapshot.getKey().);
//                    User user = snapshot.getValue(User.class);
//                    Users.add(user.getPass());
//                    if(Users.contains(cfpass)){
//                        ch[0] = false;
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }




}