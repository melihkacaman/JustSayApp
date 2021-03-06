package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.melihkacaman.entity.Room;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;
import com.melihkacaman.justsayclient.connection.RoomListener;
import com.melihkacaman.justsayclient.model.Chat;

import java.util.List;

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
            Room mRoom = new Room(txtRoomName.getText().toString(),
                                    txtRoomTopic.getText().toString(), ClientInfo.me);

            txtRoomTopic.setEnabled(false);
            txtRoomName.setEnabled(false);

            client.sendRequestForCreateRoom(mRoom, new RoomListener() {
                @Override
                public void getRoomInfo(Room room) {
                    ClientInfo.addChat(new Chat(room));
                }

                @Override
                public void getRoomList(List<Room> rooms) {
                    return;
                }
            });
            Toast.makeText(getApplicationContext(),"Room just has been created by you. You can see it your chat menu."
                    ,Toast.LENGTH_LONG).show();
            // TODO: 23.05.2021 forward chat activity.
        }
    }
}