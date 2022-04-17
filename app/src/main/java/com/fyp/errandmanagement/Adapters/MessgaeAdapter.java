package com.fyp.errandmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.fyp.errandmanagement.Models.Chat;
import com.fyp.errandmanagement.R;

public class MessgaeAdapter extends RecyclerView.Adapter<MessgaeAdapter.Holder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context ctx;
    private List<Chat> list;
    private String RecieverImage;
    private String SenderImage;

    FirebaseUser fUser;

    public MessgaeAdapter(Context ctx, List<Chat> list, String RecieverImage, String SenderImage) {
        this.ctx = ctx;
        this.list = list;
        this.RecieverImage = RecieverImage;
        this.SenderImage = SenderImage;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View v = LayoutInflater.from(ctx).inflate(R.layout.chat_item_right,parent,false);
            return new Holder(v);
        }
        else{
            View v = LayoutInflater.from(ctx).inflate(R.layout.chat_item_left,parent,false);
            return new Holder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Chat model = list.get(position);
        holder.shopMessage.setText(model.getMessage());

        if(list.get(position).getSender().equals(fUser.getUid())){
            Glide.with(ctx).load(SenderImage).into(holder.profileImage);
        }
        else
            Glide.with(ctx).load(RecieverImage).into(holder.profileImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        CircleImageView profileImage;
        TextView shopMessage;

        public Holder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            shopMessage = itemView.findViewById(R.id.shopMessage);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(list.get(position).getSender().equals(fUser.getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }
}
