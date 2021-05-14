package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.melihkacaman.justsayclient.connection.Client;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView txtUserName;

    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btn_login);
        txtUserName = findViewById(R.id.txt_username);

        try {
            client = Client.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[MainActivity.java] Connetcion Failure");
            Toast.makeText(getApplicationContext(),"Please, check your internet connection!",Toast.LENGTH_LONG).show();
        }
    }

    public void btnLoginClick(View view){
        if (!txtUserName.getText().equals("")){
            boolean res = client.checkUserNameForConvenience(txtUserName.getText().toString());
            if (res)
                System.out.println("basarili");
            else
                System.out.println("basarisiz");
        }
        Toast.makeText(getApplicationContext(),"Please, try different user name!",Toast.LENGTH_LONG).show();
    }
}