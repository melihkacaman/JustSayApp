package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChatActivity extends AppCompatActivity {

    private FloatingActionButton btnWithPerson, btnInRoom, btnJoinRoom;
    private ExtendedFloatingActionButton menu;
    private TextView txtWithPerson, txtInRoom, txtJoinRoom;

    private TextView txtUsername;

    private boolean isAllFabsVisible;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getExtras();
        userName = bundle.getString("username");

        menu = findViewById(R.id.add_fab);

        btnWithPerson = findViewById(R.id.person_float);
        btnInRoom = findViewById(R.id.room_float);
        btnJoinRoom = findViewById(R.id.join_float);

        txtWithPerson = findViewById(R.id.txt_person);
        txtInRoom = findViewById(R.id.txt_room);
        txtJoinRoom = findViewById(R.id.txt_join);

        txtUsername = findViewById(R.id.txt_chat_username);
        txtUsername.setText(userName);

        setVisibleFabs(View.GONE);
        isAllFabsVisible = false;

        menu.shrink();

        menu.setOnClickListener(v -> {
            if (!isAllFabsVisible){
                btnWithPerson.show();
                btnInRoom.show();
                btnJoinRoom.show();

                txtJoinRoom.setVisibility(View.VISIBLE);
                txtInRoom.setVisibility(View.VISIBLE);
                txtWithPerson.setVisibility(View.VISIBLE);

                menu.extend();

                isAllFabsVisible = true;
            }else {
                btnWithPerson.hide();
                btnInRoom.hide();
                btnJoinRoom.hide();

                txtJoinRoom.setVisibility(View.GONE);
                txtInRoom.setVisibility(View.GONE);
                txtWithPerson.setVisibility(View.GONE);

                menu.shrink();
                isAllFabsVisible = false;
            }
        });

        btnWithPerson.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"with person",Toast.LENGTH_LONG).show();
        });

        btnInRoom.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"new room",Toast.LENGTH_LONG).show();
        });

        btnJoinRoom.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(),"join room",Toast.LENGTH_LONG).show();
        });
    }

    private void setVisibleFabs(int state){
        btnWithPerson.setVisibility(state);
        btnJoinRoom.setVisibility(state);
        btnInRoom.setVisibility(state);

        txtJoinRoom.setVisibility(state);
        txtInRoom.setVisibility(state);
        txtWithPerson.setVisibility(state);
    }

    public void btnLeaveClick(View view) {
    }
}