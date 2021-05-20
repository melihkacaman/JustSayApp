package com.melihkacaman.justsayclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.melihkacaman.justsayclient.R;

import java.util.List;

public abstract class CustomRecyclerAdapter<T> extends RecyclerView.Adapter<CustomRecyclerAdapter<T>.ViewHolder> {

    protected List<T> datasource;
    protected LayoutInflater layoutInflater;
    protected ItemClickListener itemClickListener;

    public CustomRecyclerAdapter(Context context, List<T> dataSource) {
        this.layoutInflater = LayoutInflater.from(context);
        this.datasource = dataSource;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        TextView txtDescription;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_lst_name);
            txtDescription = itemView.findViewById(R.id.txt_lst_description);
            image = itemView.findViewById(R.id.lst_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
