package com.fyp.errandmanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.fyp.errandmanagement.CustomerData.UserMessgaeActivity;
import com.fyp.errandmanagement.Models.ProviderModel;
import com.fyp.errandmanagement.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Holder> {
    private Context ctx;
    private List<ProviderModel> list;

    public UserAdapter(Context ctx, List<ProviderModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.user_card,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ProviderModel model = list.get(position);
        Glide.with(ctx).load(model.getImage()).into(holder.headerImage);
        holder.header_name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        CircleImageView headerImage;
        TextView header_name;

        public Holder(@NonNull View itemView) {
            super(itemView);

            headerImage = itemView.findViewById(R.id.headerImage);
            header_name = itemView.findViewById(R.id.header_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProviderModel model = list.get(getAdapterPosition());

                    Intent i = new Intent(ctx, UserMessgaeActivity.class);
                    i.putExtra("ProviderID",model.getKey());
                    i.putExtra("name",model.getName());
                    i.putExtra("image",model.getImage());
                    i.putExtra("address",model.getAddress());
                    i.putExtra("email",model.getEmail());
                    i.putExtra("phone",model.getPhone());
                    ctx.startActivity(i);
                }
            });
        }
    }
}
