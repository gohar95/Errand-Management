package com.fyp.errandmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.fyp.errandmanagement.Models.ReadReviewModel;
import com.fyp.errandmanagement.R;

public class ReadReviewAdapter extends RecyclerView.Adapter<ReadReviewAdapter.MyOrderHolder>{

    private Context ctx;
    private ArrayList<ReadReviewModel> list;

    public ReadReviewAdapter(Context ctx, ArrayList<ReadReviewModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.review_card,viewGroup,false);
        return new MyOrderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderHolder holder, int i) {
        ReadReviewModel model = list.get(i);
        holder.c_name.setText(model.getName());
        holder.c_review.setText(model.getReviewText());
        holder.rateBar.setRating(Float.parseFloat(model.getRating()));
        Glide.with(ctx).load(model.getImage()).into(holder.c_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyOrderHolder extends RecyclerView.ViewHolder{

        CircleImageView c_image;
        RatingBar rateBar;
        TextView c_name,c_review;

        public MyOrderHolder(@NonNull View itemView) {
            super(itemView);

            c_image = itemView.findViewById(R.id.c_image);
            c_name = itemView.findViewById(R.id.c_name);
            c_review = itemView.findViewById(R.id.c_review);
            rateBar = itemView.findViewById(R.id.rateBar);

        }
    }
}
