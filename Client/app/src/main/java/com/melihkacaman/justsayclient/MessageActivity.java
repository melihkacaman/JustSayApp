package com.melihkacaman.justsayclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.IOException;
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
        if (prev != null) {// TODO: 22.05.2021 user can leave ?
            chat = prev;
            // TODO: 27.05.2021 if there is prev messages, focus last message.  
        } else {
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
        chatRecycler.smoothScrollToPosition(messageListAdapter.getItemCount() > 0 ? messageListAdapter.getItemCount() - 1 : 0);
    }

    public void btnSendClick(View view) {
        if (!edtMessageText.getText().toString().isEmpty()) {
            ChatMessage chatMessage = new ChatMessage(ClientInfo.me, chat.getWho(), edtMessageText.getText().toString());
            client.sendChatMessage(chatMessage);
            chatRecycler.smoothScrollToPosition(messageListAdapter.insertItem(chatMessage));
            edtMessageText.setText("");
        }
    }

    public void btnFileSend(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery, 2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery, 2);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            Bitmap bitmap = null;
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), imageData);
                    bitmap = ImageDecoder.decodeBitmap(source);
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageData);
                }
                // TODO: 30.05.2021 /*
                //  1. should be shown on recycler first
                //  2. send server
                //  3. send target client
                //  4. show image target's client screen
                //  */


            } catch (IOException e) {
                System.out.println("ERROR : " + e.getMessage());
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChatMessage chatMessage) {
        messageListAdapter.notifyDataSetChanged();
        chatRecycler.smoothScrollToPosition(messageListAdapter.getItemCount() - 1);
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