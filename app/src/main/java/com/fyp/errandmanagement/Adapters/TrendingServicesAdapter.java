package com.fyp.errandmanagement.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fyp.errandmanagement.CustomerData.ServiceDetails;
import com.fyp.errandmanagement.Models.TrendingServiceModel;
import com.fyp.errandmanagement.R;

public class TrendingServicesAdapter extends RecyclerView.Adapter<TrendingServicesAdapter.Holder> {
    private Context ctx;
    private ArrayList<TrendingServiceModel> list;

    public TrendingServicesAdapter(Context ctx, ArrayList<TrendingServiceModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.trending_card,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        TrendingServiceModel model = list.get(position);
        Glide.with(ctx).load(model.getServiceImage()).into(holder.headerImage);
        holder.header_text.setText(model.getServiceName());
        holder.header_price.setText(model.getServicePrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        ImageView headerImage;
        TextView header_text, header_price;

        public Holder(@NonNull View itemView) {
            super(itemView);

            headerImage = itemView.findViewById(R.id.headerImage);
            header_text = itemView.findViewById(R.id.header_text);
            header_price = itemView.findViewById(R.id.header_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TrendingServiceModel model = list.get(getAdapterPosition());
                    FetchProvider(model.getProviderID(),getAdapterPosition());
                }
            });
        }
    }

    private void FetchProvider(String ProviderID,int position) {
        TrendingServiceModel model = list.get(position);
        ProgressDialog progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Fetching Services Provider Details");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        reference1.child("ServiceProviders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                if(dataSnapshot1 !=null){
                    for(DataSnapshot snapshot1 : dataSnapshot1.getChildren()){
                        if(ProviderID.equals(snapshot1.getKey())){
                            String name = snapshot1.child("name").getValue().toString();
                            String image = snapshot1.child("image").getValue().toString();
                            String email = snapshot1.child("email").getValue().toString();
                            String address = snapshot1.child("address").getValue().toString();
                            String latitude = snapshot1.child("latitude").getValue().toString();
                            String longitude = snapshot1.child("longitude").getValue().toString();
                            String phone = snapshot1.child("phone").getValue().toString();
                            String fcmToken = snapshot1.child("fcmToken").getValue().toString();

                            double latt = Double.parseDouble(latitude);
                            double lngg = Double.parseDouble(longitude);

                            Intent i = new Intent(ctx, ServiceDetails.class);
                            i.putExtra("ServiceID",model.getServiceID());
                            i.putExtra("Booked",model.getBooked());
                            i.putExtra("Category",model.getCategory());
                            i.putExtra("DateTime",model.getDateTime());
                            i.putExtra("ServiceDescription",model.getServiceDescription());
                            i.putExtra("ServiceImage",model.getServiceImage());
                            i.putExtra("ServiceName",model.getServiceName());
                            i.putExtra("ServicePrice",model.getServicePrice());
                            i.putExtra("ServicePriceType",model.getServicePriceType());
                            i.putExtra("ServiceType",model.getServiceType());
                            i.putExtra("ProviderID",model.getProviderID());

                            i.putExtra("latt",latt);
                            i.putExtra("lngg",lngg);
                            i.putExtra("name",name);
                            i.putExtra("image",image);
                            i.putExtra("address",address);
                            i.putExtra("email",email);
                            i.putExtra("phone",phone);
                            i.putExtra("fcmToken",fcmToken);
                            progressDialog.dismiss();
                            ctx.startActivity(i);
                        }
                    }
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
    }
}
