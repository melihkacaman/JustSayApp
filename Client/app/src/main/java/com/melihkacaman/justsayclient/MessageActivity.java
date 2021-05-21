package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.Message;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.adapters.MessageListAdapter;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView chatRecycler;
    private MessageListAdapter messageListAdapter;

    private TextView txtUsernameTop;
    private ImageView imgBackButton;

    private Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        client = Client.getInstance();

        Bundle extras = getIntent().getExtras();
        User pair = (User) extras.getSerializable("selectedUser");

        txtUsernameTop = findViewById(R.id.txt_username_message);
        imgBackButton = findViewById(R.id.back_button);
        imgBackButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        });

        chatRecycler = findViewById(R.id.recycler_gchat);
        messageListAdapter = new MessageListAdapter(this, null);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(messageListAdapter);


    }
}