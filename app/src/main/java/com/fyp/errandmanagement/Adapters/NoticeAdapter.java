package com.fyp.errandmanagement.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.errandmanagement.Models.NotificationModel;
import com.fyp.errandmanagement.R;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.Holder> {


    private Context ctx;
    private List<NotificationModel> list;

    public NoticeAdapter(Context ctx, List<NotificationModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.notice_card, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NotificationModel model = list.get(position);
        holder.n_title.setText(model.getNotiTitle());
        holder.n_body.setText(model.getNotiBody());
        holder.n_datetime.setText(model.getDateTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView n_title,n_body,n_datetime;

        public Holder(@NonNull View itemView) {
            super(itemView);

            n_title = itemView.findViewById(R.id.n_title);
            n_body = itemView.findViewById(R.id.n_body);
            n_datetime = itemView.findViewById(R.id.n_datetime);

        }
    }

}
