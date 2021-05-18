package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.melihkacaman.entity.Room;
import com.melihkacaman.justsayclient.connection.Client;

public class CreateRoomActivity extends AppCompatActivity {

    EditText txtRoomName, txtRoomTopic;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        client = Client.getInstance();

        txtRoomName = findViewById(R.id.txt_room_name);
        txtRoomTopic = findViewById(R.id.txt_room_topic);
    }

    public void createRoomClick(View view) {
        if (!txtRoomName.getText().toString().isEmpty() &&  !txtRoomTopic.getText().toString().isEmpty()){
            //Room room = new Room(); id ?
        }
    }
}