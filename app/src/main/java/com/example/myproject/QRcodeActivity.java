package com.example.myproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static android.Manifest.permission.CAMERA;

public class QRcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private  static  final int REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        int currentapiVertion = Build.VERSION.SDK_INT;
        
        if(currentapiVertion >= android.os.Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(getApplicationContext(),"ได้รับอนุญาติแล้ว" ,Toast.LENGTH_SHORT).show();
            }
            else {
                requestPermissions();
            }
        }

    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    public void onRequestPermissionResult(int requestCode,String permissions[],int[] grantResults){
        switch(requestCode){
            case REQUEST_CAMERA :
                if(grantResults.length>0){

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(getApplicationContext(),"ขออนุญาติเข้าถึงการใช้งานกล้องถ่ายภาพ",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"ไม่อนุญาติให้เข้าถึงกล้องถ่ายภาพ",Toast.LENGTH_SHORT).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)){
                                showMessageOKCancel("คุณต้องการอนุญาติให้สามารถเข้าถึงการใช้งานกล้องถ่ายภาพหรือไม่",
                                        new DialogInterface.OnClickListener(){

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                    requestPermissions(new String[]{CAMERA},REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message,DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(QRcodeActivity.this)
                .setMessage(message)
                .setPositiveButton("ตกลง",okListener)
                .setNegativeButton("ยกเลิก",null)
                .create()
                .show();
    }


    public void onResume() {
        super.onResume();
        int currentapiVersion = Build.VERSION.SDK_INT;
        if(currentapiVersion >= android.os.Build.VERSION_CODES.M){
            if(checkPermission()){
                if(mScannerView == null){
                    mScannerView = new ZXingScannerView(this);
                    setContentView(mScannerView);
                }
                mScannerView.setResultHandler((ZXingScannerView.ResultHandler) this);
                mScannerView.startCamera();

            }
            else{
                requestPermissions();
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mScannerView.stopCamera();
    }


    @Override
    public void handleResult(Result rawResult) {
        String result = rawResult.getText();

        Toast.makeText(getApplicationContext(),"เชื่อมได้แล้วนะ",Toast.LENGTH_LONG).show();
    }
}
