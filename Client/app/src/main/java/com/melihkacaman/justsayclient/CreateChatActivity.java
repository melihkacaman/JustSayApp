package com.melihkacaman.justsayclient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;
import com.melihkacaman.justsayclient.connection.UserListener;

public class CreateChatActivity extends AppCompatActivity {

    ListView usersList;
    User[] userNames;

    Handler handler;
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        client = Client.getInstance();

        usersList = findViewById(R.id.lst_createChat);
        handler = new Handler(Looper.getMainLooper());

        client.sendRequestForUserList();

        // first loading
        client.addListener(new UserListener() {
            @Override
            public void getUsersInfo(User[] users) {
                handler.post(() -> {
                    if (users.length > 1){
                        String[] names = new String[users.length];
                        int i = 0;
                        for (User user : users){
                            if (user.getId() != ClientInfo.me.getId()){
                                names[i] = user.getUserName();
                                i++;
                            }
                        }


                        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateChatActivity.this,
                                android.R.layout.simple_list_item_1, android.R.id.text1,names);
                        usersList.setAdapter(adapter);
                    }
                });
            }
        });
    }
}