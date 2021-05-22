package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.UUID;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView chatRecycler;
    private MessageListAdapter messageListAdapter;

    private TextView txtUsernameTop;
    private ImageView imgBackButton;

    private EditText edtMessageText;
    private Button btnSend;

    private Client client;
    private User pair;

    private List<ChatMessage> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        client = Client.getInstance();

        Bundle extras = getIntent().getExtras();
        pair = (User) extras.getSerializable("selectedUser");  // TODO: 22.05.2021 user can leave ?

        messageList = new LinkedList<>();

        txtUsernameTop = findViewById(R.id.txt_username_message);
        txtUsernameTop.setText(pair.getUserName());

        imgBackButton = findViewById(R.id.back_button);
        imgBackButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        });

        edtMessageText = findViewById(R.id.edit_gchat_message);
        btnSend = findViewById(R.id.button_gchat_send);

        chatRecycler = findViewById(R.id.recycler_gchat);
        messageListAdapter = new MessageListAdapter(this, messageList);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(messageListAdapter);
    }

    public void btnSendClick(View view) {
        if (!edtMessageText.getText().toString().isEmpty()){
            ChatMessage chatMessage = new ChatMessage(ClientInfo.me, pair,edtMessageText.getText().toString());
            client.sendChatMessage(chatMessage);
            chatRecycler.smoothScrollToPosition(messageListAdapter.insertItem(chatMessage));
            edtMessageText.setText("");
        }
    }
}