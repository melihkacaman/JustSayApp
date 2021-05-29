package com.melihkacaman.justsayclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.Room;
import com.melihkacaman.justsayclient.R;
import com.melihkacaman.justsayclient.model.Chat;

import java.util.List;
import java.util.Stack;

public class ChatAdapter extends CustomRecyclerAdapter<Chat> {

    Stack<Chat> datasource;

    public ChatAdapter(Context context, Stack<Chat> dataSource) {
        super(context, null);
        this.datasource = dataSource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = super.layoutInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(datasource.get(position).getWho().getUserName());
        holder.txtDescription.setText(datasource.get(position).getLastMessage());
        if (datasource.get(position).getWho() instanceof Room){
            holder.image.setImageResource(R.drawable.lst_group);
        }else {
            holder.image.setImageResource(R.drawable.lst_user_foreground_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public Chat getItemById(int position){
        return this.datasource.elementAt(position);
    }

    public void addNewChat(ChatMessage chatMessage){
        Chat chat = new Chat(chatMessage.getSender());
        chat.addMessage(chatMessage);
        datasource.push(chat);
        notifyDataSetChanged();
    }

    public void addNewChat(Chat chat, ChatMessage chatMessage){
        int chatIdx = findById(chat.getId());
        if(chatIdx != -1){
            Chat chatPrev = datasource.get(chatIdx);
            datasource.remove(chatIdx);

            chatPrev.addMessage(chatMessage);
            datasource.push(chatPrev);
            notifyDataSetChanged();
        }
    }

    private int findById(int id){
        int i = 0;
        for (Chat chat:datasource) {
            if (chat.getId() == id){
                return i;
            }
            i++;
        }

        return -1;
    }
}
