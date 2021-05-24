package com.melihkacaman.justsayclient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javafaker.Faker;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;
import com.melihkacaman.justsayclient.model.ConvenienceUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView txtUserName;

    private Client client;

    private int INTERNET_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btn_login);
        txtUserName = findViewById(R.id.txt_username);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED){
            requestInternetPermission();
        }

        client = Client.getInstance();
        if (client == null){
            Toast.makeText(getApplicationContext(),"Please, check your internet connection!",Toast.LENGTH_LONG).show();
        }
    }

    public void btnLoginClick(View view){
        client = Client.getInstance();
        if (client == null)
            Toast.makeText(getApplicationContext(),"Please, check your internet connection!",Toast.LENGTH_LONG).show();

        if (client != null && txtUserName.getText().length() > 0){
            client.checkUserNameForConvenience(txtUserName.getText().toString());
            btnLogin.setClickable(false);
        }
    }

    private void requestInternetPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of Internet connection.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}
                        ,INTERNET_PERMISSION_CODE);
                    })
                    .setNegativeButton("CANCEL", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}
                    ,INTERNET_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ConvenienceUser state){
        if(state.isConvenience()){
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra("username", txtUserName.getText().toString());
            ClientInfo.generateFakeData();
            startActivity(intent);
        }else {
            Toast.makeText(getApplicationContext(),"Please, try different user name!",Toast.LENGTH_LONG).show();
            txtUserName.setText("");  // todo set delay
            btnLogin.setClickable(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}