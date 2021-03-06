package com.melihkacaman.justsayclient.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.R;

import java.util.List;

public class UserAdapter extends CustomRecyclerAdapter<User> {

    public UserAdapter(Context context, List<User> dataSource) {
        super(context, dataSource);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = super.layoutInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(datasource.get(position).getUserName());
        if (datasource.get(position) instanceof Room){
            holder.txtDescription.setText(((Room) datasource.get(position)).getTopic());
            holder.image.setImageResource(R.drawable.lst_group);
        }else {
            holder.txtDescription.setVisibility(View.INVISIBLE);
            holder.image.setImageResource(R.drawable.lst_user_foreground_foreground);
        }
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public void addItem(User user){
        this.datasource.add(user);
        notifyDataSetChanged();
    }

    public User getDataByPosition(int position){
        return datasource.get(position);
    }

    public boolean contains(User user){
        for (User usr : this.datasource) {
            if (user.getId() == usr.getId()){
                return true;
            }
        }
        return false;
    }
}
