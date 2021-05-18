package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.adapters.UserRecyclerViewAdapter;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;
import com.melihkacaman.justsayclient.connection.UserListener;

import java.util.ArrayList;
import java.util.List;

public class CreateChatActivity extends AppCompatActivity implements UserListener {

    RecyclerView rcycleUsersList;
    UserRecyclerViewAdapter adapter;

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

        // first loading
        client.sendRequestForUserList();
        client.addUserListener(this);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            handler.postDelayed(() -> {
                client.sendRequestForUserList();
                client.addUserListener(CreateChatActivity.this);
                swipeRefreshLayout.setRefreshing(false);
            }, 500);
        });
    }

    @Override
    public void getUsersInfo(User[] users) {
        if (users.length > 1) {
            List<User> adapterData = new ArrayList<>();
            for (User user : users) {
                if (user.getId() != ClientInfo.me.getId()) {
                    adapterData.add(user);
                }
            }


            runOnUiThread(() -> {
                rcycleUsersList.setLayoutManager(new LinearLayoutManager(CreateChatActivity.this));
                adapter = new UserRecyclerViewAdapter(CreateChatActivity.this, adapterData);
                adapter.setItemClickListener((view, position) -> {
                    // Todo: when clicked do something, in a new page.
                    adapter.getDataByPosition(position);
                });
                rcycleUsersList.setAdapter(adapter);
            });
        }
    }
}