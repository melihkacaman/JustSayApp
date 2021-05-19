package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.melihkacaman.entity.Room;
import com.melihkacaman.justsayclient.adapters.UserRecyclerViewAdapter;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.RoomListener;

import java.util.List;

public class JoinRoomActivity extends AppCompatActivity implements RoomListener {
    private RecyclerView recyclerViewRooms;
    private SwipeRefreshLayout swipeRefreshLayout;
    private UserRecyclerViewAdapter adapter; // TODO: 19.05.2021 change the name of this class

    Handler handler;

    private Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        client = Client.getInstance();
        handler =new Handler(Looper.getMainLooper());

        recyclerViewRooms = findViewById(R.id.lst_rcycle_rooms);
        swipeRefreshLayout = findViewById(R.id.swipe_ref_room);

        client.sendRequestForRoomList(new RoomListener() {
            @Override
            public void getRoomInfo(Room room) {
                return;
            }

            @Override
            public void getRoomList(List<Room> rooms) {
                
            }
        });
    }

    @Override
    public void getRoomInfo(Room room) {
        return;
    }

    @Override
    public void getRoomList(List<Room> rooms) {
        return;
    }
}