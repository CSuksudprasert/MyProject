package com.example.myproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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


public class MainActivity extends AppCompatActivity{

    private Button buttonregister;
    private Button buttonLogin;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseReff;
    private FirebaseDatabase db;
    private EditText edittext_email;
    private EditText edittext_password;

    private String email ,pass = "";

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
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                email = edittext_email.getText().toString();
                pass = edittext_password.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(MainActivity.this,ShowdataActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"อีเมล์หรือรหัสผ่านไม่ถูกต้อง",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
    }

//    private void chechlogiin(){
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        firebaseAuth.signInWithEmailAndPassword(email,pass)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//
//                        }
//                    }
//                });
//    }

}
