package com.fyp.errandmanagement.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fyp.errandmanagement.Models.AnyServiceModel;
import com.fyp.errandmanagement.R;

public class MyServicesAdapter extends RecyclerView.Adapter<MyServicesAdapter.Holder> {
    private Context ctx;
    private ArrayList<AnyServiceModel> list;

    public MyServicesAdapter(Context ctx, ArrayList<AnyServiceModel> list) {
        this.ctx = ctx;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.service_card,parent,false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        AnyServiceModel model = list.get(position);
        //new DownloadImageFromInternet(holder.serviceImage).execute(model.getServiceImage());
        holder.serviceName.setText(model.getServiceName());
        holder.serviceDesc.setText(model.getServiceDescription());
        holder.serviceType.setText(model.getServiceType());
        holder.servicePrice.setText(model.getServicePrice());
        holder.servicePriceType.setText(model.getServicePriceType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        ImageView serviceImage;
        TextView serviceName, serviceDesc, serviceType, servicePrice, servicePriceType;

        public Holder(@NonNull View itemView) {
            super(itemView);

            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDesc = itemView.findViewById(R.id.serviceDesc);
            serviceType = itemView.findViewById(R.id.serviceType);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            servicePriceType = itemView.findViewById(R.id.servicePriceType);
        }
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(result);
        }
    }
}
