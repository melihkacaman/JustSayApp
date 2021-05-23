package com.melihkacaman.justsayclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.R;

import java.util.List;

public class RoomAdapter extends CustomRecyclerAdapter<Room>{

    public RoomAdapter(Context context, List<Room> dataSource) {
        super(context, dataSource);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = super.layoutInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new RoomAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(datasource.get(position).getUserName());
        holder.txtDescription.setText(datasource.get(position).getTopic());
        holder.image.setImageResource(R.drawable.lst_group);
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public Room getDataByPosition(int position){
        return datasource.get(position);
    }
}
