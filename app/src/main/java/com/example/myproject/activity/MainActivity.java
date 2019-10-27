package com.example.myproject.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.example.myproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity{

    private Button buttonregister;
    private Button buttonLogin;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseReff;
    private FirebaseDatabase db;
    private EditText edittext_email;
    private EditText edittext_password;

    private String email ,pass = "";
    private static final String MY_PREFS = "prefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonregister = findViewById(R.id.buttonRegister);
        buttonLogin = findViewById(R.id.buttonLogin);
        edittext_email = findViewById(R.id.edittext_email);
        edittext_password = findViewById(R.id.edittext_password);

        email = edittext_email.getText().toString();
        pass = edittext_password.getText().toString();


        buttonregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next page
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                email = edittext_email.getText().toString();
                pass = edittext_password.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(MainActivity.this, ShowdataActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this,"อีเมล์หรือรหัสผ่านไม่ถูกต้อง",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(MainActivity.this,"กรุณากรอกอีเมล์ และ รหัสผ่านให้ครบถ้วน",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



}
