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
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.adapters.MessageListAdapter;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;
import com.melihkacaman.justsayclient.model.Chat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private RecyclerView chatRecycler;
    private MessageListAdapter messageListAdapter;

    private TextView txtUsernameTop;
    private ImageView imgBackButton;

    private EditText edtMessageText;
    private Button btnSend;

    private Client client;
    private Chat chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        client = Client.getInstance();

        Bundle extras = getIntent().getExtras();
        User selectedUser = (User) extras.getSerializable("selectedUser");

        Chat prev = ClientInfo.checkPreviousChat(selectedUser);
        if (prev != null){// TODO: 22.05.2021 user can leave ?
            chat = prev;
            // TODO: 27.05.2021 if there is prev messages, focus last message.  
        }else {
            chat = new Chat(selectedUser);
            ClientInfo.addChat(chat);
        }

        txtUsernameTop = findViewById(R.id.txt_username_message);
        txtUsernameTop.setText(chat.getWho().getUserName());

        imgBackButton = findViewById(R.id.back_button);
        imgBackButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        });

        edtMessageText = findViewById(R.id.edit_gchat_message);
        btnSend = findViewById(R.id.button_gchat_send);

        chatRecycler = findViewById(R.id.recycler_gchat);
        messageListAdapter = new MessageListAdapter(this, chat.getMessages());
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(messageListAdapter);
    }

    public void btnSendClick(View view) {
        if (!edtMessageText.getText().toString().isEmpty()){
            ChatMessage chatMessage = new ChatMessage(ClientInfo.me, chat.getWho(),edtMessageText.getText().toString());
            client.sendChatMessage(chatMessage);
            chatRecycler.smoothScrollToPosition(messageListAdapter.insertItem(chatMessage));
            edtMessageText.setText("");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChatMessage chatMessage){
        if(chatMessage.getSender().getId() == chat.getWho().getId()){
            chatRecycler.smoothScrollToPosition(messageListAdapter.insertItem(chatMessage));
        }else {
            Chat chatPrev = ClientInfo.checkPreviousChat(chatMessage.getSender());
            if (chatPrev == null){
                Chat chat1 = new Chat(chatMessage.getSender());
                chat1.addMessage(chatMessage);
                ClientInfo.addChat(chat1);
            }else {
                chatPrev.addMessage(chatMessage);
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