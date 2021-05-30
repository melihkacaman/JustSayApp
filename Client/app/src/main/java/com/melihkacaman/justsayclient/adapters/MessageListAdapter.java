package com.melihkacaman.justsayclient.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.FileMessage;
import com.melihkacaman.justsayclient.R;
import com.melihkacaman.justsayclient.connection.ClientInfo;

import java.util.Calendar;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<ChatMessage> mMessageList;

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_MESSAGE_IMAGE = 3;

    public MessageListAdapter(Context context, List<ChatMessage> messageList) {
        mContext = context;
        mMessageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = (ChatMessage) mMessageList.get(position);
        if (message instanceof FileMessage){
            return VIEW_TYPE_MESSAGE_IMAGE;
        }else {
            if (message.getSender().getId() == ClientInfo.me.getId()){
                // It's me!
                return VIEW_TYPE_MESSAGE_SENT;
            }else {
                // It's not me! It's my partner.
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
            return new SentMessageHolder(view);
        }else if(viewType == VIEW_TYPE_MESSAGE_RECEIVED){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_other, parent, false);
            return new ReceivedMessageHolder(view);
        }else if (viewType == VIEW_TYPE_MESSAGE_IMAGE){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_image, parent, false);
            return new ImageMessageHolder(view);
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = mMessageList.get(position);
        switch (holder.getItemViewType()){
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder)holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder)holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_IMAGE:
                ((ImageMessageHolder)holder).bind((FileMessage) message);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public int insertItem(ChatMessage chatMessage){
        this.mMessageList.add(chatMessage);
        notifyDataSetChanged();
        return this.mMessageList.size() - 1;
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_other);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_other);
            nameText = (TextView) itemView.findViewById(R.id.text_gchat_user_other);
            profileImage = (ImageView) itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());

            timeText.setText(DateUtils.formatDateTime(mContext, message.getCreatedAt(), DateUtils.FORMAT_SHOW_TIME));
            nameText.setText(message.getSender().getUserName());

            long calendar = message.getCreatedAt();

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_gchat_message_me);
            timeText = (TextView) itemView.findViewById(R.id.text_gchat_timestamp_me);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(DateUtils.formatDateTime(mContext,message.getCreatedAt(), DateUtils.FORMAT_SHOW_TIME));
        }
    }

    private class ImageMessageHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView txt_me, txt_other;

        ImageMessageHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_message);
            txt_me = itemView.findViewById(R.id.tv_image_me);
            txt_other = itemView.findViewById(R.id.tv_image_other);
        }

        void bind(FileMessage fileMessage){
            Object img = fileMessage.getFile();
            byte[] img_array = (byte[])img;
            Bitmap bitmap = BitmapFactory.decodeByteArray(img_array, 0, img_array.length);

            image.setImageBitmap(bitmap);
            txt_me.setText("");
            txt_other.setText("");
            if (ClientInfo.me.getId() != fileMessage.getSender().getId()){
                txt_other.setText(fileMessage.getSender().getUserName());
            }
        }
    }
}