package com.fyp.errandmanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import com.fyp.errandmanagement.CustomerData.CompletedOrder;
import com.fyp.errandmanagement.Models.OrderModel;
import com.fyp.errandmanagement.ProviderData.OrderDetails;
import com.fyp.errandmanagement.R;

public class ProviderOrderAdapter extends RecyclerView.Adapter<ProviderOrderAdapter.MyOrderHolder>{

    private Context ctx;
    private ArrayList<OrderModel> list;

    public ProviderOrderAdapter(Context ctx, ArrayList<OrderModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.request_card,viewGroup,false);
        return new MyOrderHolder(v);
    }
    public void filterList(ArrayList<OrderModel> filteredList){
        list = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull MyOrderHolder holder, int i) {
        OrderModel model = list.get(i);
        Glide.with(ctx).load(model.getImage()).into(holder.cus_img);
        holder.cus_name.setText(model.getNAME());
        holder.order_title.setText(model.getONAME());
        holder.order_date.setText(model.getDATE());
        holder.order_status.setText(model.getOSTATUS());

        if(model.getOSTATUS().equals("Canceled")){
            holder.order_status.setTextColor(Color.parseColor("#af0000"));
        }
        if(model.getOSTATUS().equals("Pending")){
            holder.order_status.setTextColor(Color.parseColor("#0B67C0"));
        }
        if(model.getOSTATUS().equals("Approved")){
            holder.order_status.setTextColor(Color.parseColor("#0ea71e"));
        }
        if(model.getOSTATUS().equals("Completed")){
            holder.order_status.setTextColor(Color.parseColor("#FF7200"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyOrderHolder extends RecyclerView.ViewHolder{

        CircleImageView cus_img;
        TextView cus_name,order_title,order_date,order_status;
        LinearLayout view_order;

        public MyOrderHolder(@NonNull View itemView) {
            super(itemView);

            cus_img = itemView.findViewById(R.id.cus_img);
            cus_name = itemView.findViewById(R.id.cus_name);
            order_title = itemView.findViewById(R.id.order_title);
            order_date = itemView.findViewById(R.id.order_date);
            order_status = itemView.findViewById(R.id.order_status);
            view_order = itemView.findViewById(R.id.view_order);

            view_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderModel model = list.get(getAdapterPosition());

                    if(model.getOSTATUS().equals("Completed")){
                        Intent i = new Intent(ctx, CompletedOrder.class);
                        i.putExtra("OID",model.getOrderNO());
                        ctx.startActivity(i);
                    }
                    else{
                        Intent i = new Intent(ctx, OrderDetails.class);
                        i.putExtra("ADDRESS",model.getADDR());
                        i.putExtra("DATE",model.getDATE());
                        i.putExtra("DAY",model.getDAY());
                        i.putExtra("INSTRUCTIONS",model.getINS());
                        i.putExtra("NAME",model.getNAME());
                        i.putExtra("TITLE",model.getONAME());
                        i.putExtra("STATUS",model.getOSTATUS());
                        i.putExtra("METHOD",model.getORDER());
                        i.putExtra("OID",model.getOrderNO());
                        i.putExtra("ODATE",model.getPDATE());
                        i.putExtra("PHONE",model.getPHONE());
                        i.putExtra("ServiceID",model.getServiceID());
                        i.putExtra("PRICE",model.getPRICE());
                        i.putExtra("ProviderID",model.getProviderID());
                        i.putExtra("CustomerID",model.getCustomerID());
                        i.putExtra("TIME",model.getTIME());
                        i.putExtra("PriceType",model.getServicePriceType());
                        i.putExtra("Image",model.getImage());
                        ctx.startActivity(i);
                    }
                }
            });
        }
    }
}
