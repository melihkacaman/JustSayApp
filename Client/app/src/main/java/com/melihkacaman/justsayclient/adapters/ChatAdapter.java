package com.melihkacaman.justsayclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melihkacaman.entity.Room;
import com.melihkacaman.justsayclient.R;
import com.melihkacaman.justsayclient.model.Chat;

import java.util.List;

public class ChatAdapter extends CustomRecyclerAdapter<Chat> {

    public ChatAdapter(Context context, List<Chat> dataSource) {
        super(context, dataSource);
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
}
