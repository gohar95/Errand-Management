package com.fyp.errandmanagement.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fyp.errandmanagement.CustomerData.CompletedOrder;
import com.fyp.errandmanagement.CustomerData.EditOrder;
import com.fyp.errandmanagement.CustomerData.OrderFinished;
import com.fyp.errandmanagement.Models.OrderModel;
import com.fyp.errandmanagement.R;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyOrderHolder>{

    private Context ctx;
    private ArrayList<OrderModel> list;

    public MyOrderAdapter(Context ctx, ArrayList<OrderModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public MyOrderHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.myorder_itemdesign,viewGroup,false);
        return new MyOrderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderHolder holder, int i) {
        OrderModel model = list.get(i);
        holder.morderID.setText(model.getOrderNO());
        holder.morderNAME.setText(model.getONAME());
        holder.morderQUAN.setText(model.getOSTATUS());
        holder.morderPLAN.setText(model.getDAY()+" "+model.getDATE());
        holder.morderTIME.setText(model.getTIME());
        holder.morderCOST.setText("Rs. "+model.getPRICE());

        if(model.getOSTATUS().equals("Canceled")){
            holder.morderQUAN.setTextColor(Color.parseColor("#af0000"));
        }
        if(model.getOSTATUS().equals("Pending")){
            holder.morderQUAN.setTextColor(Color.parseColor("#0B67C0"));
        }
        if(model.getOSTATUS().equals("Approved")){
            holder.morderQUAN.setTextColor(Color.parseColor("#0ea71e"));
        }
        if(model.getOSTATUS().equals("Completed")){
            holder.morderQUAN.setTextColor(Color.parseColor("#FF7200"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyOrderHolder extends RecyclerView.ViewHolder{

        TextView morderID,editORDER,morderNAME,morderQUAN,morderPLAN,morderTIME,morderCOST;

        public MyOrderHolder(@NonNull View itemView) {
            super(itemView);

            morderID = itemView.findViewById(R.id.morderID);
            editORDER = itemView.findViewById(R.id.editORDER);
            morderNAME = itemView.findViewById(R.id.morderNAME);
            morderQUAN = itemView.findViewById(R.id.morderQUAN);
            morderPLAN = itemView.findViewById(R.id.morderPLAN);
            morderTIME = itemView.findViewById(R.id.morderTIME);
            morderCOST = itemView.findViewById(R.id.morderCOST);

            editORDER.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderModel model = list.get(getAdapterPosition());
                    Intent i = new Intent(ctx, EditOrder.class);
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
                    ctx.startActivity(i);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderModel model = list.get(getAdapterPosition());

                    if(model.getOSTATUS().equals("Completed")){
                        Intent i = new Intent(ctx, CompletedOrder.class);
                        i.putExtra("OID",model.getOrderNO());
                        ctx.startActivity(i);
                    }
                    else{
                        Intent i = new Intent(ctx, OrderFinished.class);
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
                        ctx.startActivity(i);
                    }
                }
            });
        }
    }
}
