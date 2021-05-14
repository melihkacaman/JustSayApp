package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    TextView txtUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btn_login);
        txtUserName = findViewById(R.id.txt_username);
    }

    public void btnLoginClick(View view){
        if (!txtUserName.getText().equals("")){
            // Todo: Check the server for userName
        }
        Toast.makeText(getApplicationContext(),"Please, try different user name!",Toast.LENGTH_LONG).show();
    }
}