package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.adapters.UserAdapter;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;
import com.melihkacaman.justsayclient.connection.UserListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateChatActivity extends AppCompatActivity {

    RecyclerView rcycleUsersList;
    UserAdapter adapter;
    List<User> adapterData;
    Handler handler;
    Client client;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        client = Client.getInstance();

        swipeRefreshLayout = findViewById(R.id.swipe_ref1);
        rcycleUsersList = findViewById(R.id.lst_rcycle);
        handler = new Handler(Looper.getMainLooper());
        adapterData = new LinkedList<>();

        rcycleUsersList.setLayoutManager(new LinearLayoutManager(CreateChatActivity.this));
        adapter = new UserAdapter(CreateChatActivity.this, adapterData);
        adapter.setItemClickListener((view, position) -> {
            User selected = adapter.getDataByPosition(position);
            Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
            intent.putExtra("selectedUser" , selected);
            startActivity(intent);
        });
        rcycleUsersList.setAdapter(adapter);

        // first loading
        client.sendRequestForUserList();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            handler.postDelayed(() -> {
                client.sendRequestForUserList();
                swipeRefreshLayout.setRefreshing(false);
            }, 500);
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(User[] users){
        if (users.length > 1) {
            for (User user : users) {
                if (!adapter.contains(user) && user.getId() != ClientInfo.me.getId()){
                    adapter.addItem(user);
                }
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