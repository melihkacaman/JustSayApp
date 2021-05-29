package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toolbar;

import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.adapters.CustomRecyclerAdapter;
import com.melihkacaman.justsayclient.adapters.RoomAdapter;
import com.melihkacaman.justsayclient.adapters.UserAdapter;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;
import com.melihkacaman.justsayclient.connection.RoomListener;
import com.melihkacaman.justsayclient.model.Chat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

public class JoinRoomActivity extends AppCompatActivity{
    private RecyclerView recyclerViewRooms;
    private SwipeRefreshLayout swipeRefreshLayout;
    UserAdapter adapter;
    Handler handler;
    List<User> rooms;

    private Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);
        client = Client.getInstance();
        handler =new Handler(Looper.getMainLooper());

        recyclerViewRooms = findViewById(R.id.lst_rcycle_rooms);
        swipeRefreshLayout = findViewById(R.id.swipe_ref_room);

        rooms = new LinkedList<>();
        recyclerViewRooms.setLayoutManager(new LinearLayoutManager(JoinRoomActivity.this));
        adapter = new UserAdapter(JoinRoomActivity.this, rooms);
        adapter.setItemClickListener((view, position) -> {
            Room room = (Room) adapter.getDataByPosition(position);
            if (room.contains(ClientInfo.me)){

            }else {
                //Todo: Do you want to join this room ?

            }
        });

        recyclerViewRooms.setAdapter(adapter);

        client.sendRequestForRoomList();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            handler.postDelayed(() -> {
                client.sendRequestForRoomList();
                swipeRefreshLayout.setRefreshing(false);
            }, 500);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(List<Room> rooms){
        for (User usr : rooms){
            if (!adapter.contains(usr)){
                adapter.addItem(usr);
            }
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