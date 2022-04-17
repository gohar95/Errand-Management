package com.fyp.errandmanagement.CustomerData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.fyp.errandmanagement.Adapters.SlidingImage_Adapter;
import com.fyp.errandmanagement.Adapters.TrendingServicesAdapter;
import com.fyp.errandmanagement.Models.ImageModel;
import com.fyp.errandmanagement.Models.TrendingServiceModel;
import com.fyp.errandmanagement.R;

public class DashboardFragment extends Fragment {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    LinearLayout photo_service,decorator_service,hall_service,catering_service,HomeSanitization_service,Electrician_service,Carpentering_service,ACRepairing_service,Plumbing_service;

    private int[] myImageList = new int[]{R.drawable.food,R.drawable.grocery,R.drawable.luggage,R.drawable.giftbox,R.drawable.document,R.drawable.furniture,R.drawable.goods};

    RecyclerView recyclerView;
    ArrayList<TrendingServiceModel> list;
    TrendingServicesAdapter adapter;

    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView  = inflater.inflate(R.layout.fragment_dashboard, container, false);

        HomeSanitization_service = itemView.findViewById(R.id.HomeSanitization_service);
        Electrician_service = itemView.findViewById(R.id.Electrician_service);
        Carpentering_service = itemView.findViewById(R.id.Carpentering_service);
        ACRepairing_service = itemView.findViewById(R.id.ACRepairing_service);
        Plumbing_service = itemView.findViewById(R.id.Plumbing_service);
        catering_service = itemView.findViewById(R.id.catering_service);
        hall_service = itemView.findViewById(R.id.hall_service);
        hall_service = itemView.findViewById(R.id.hall_service);
        decorator_service = itemView.findViewById(R.id.decorator_service);
        photo_service = itemView.findViewById(R.id.photo_service);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();
        init(itemView);

        recyclerView = itemView.findViewById(R.id.trendingRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        list = new ArrayList<>();

       /* progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Fetching Your Services");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
*/
        FetchTrendingServices("Check Events");
        FetchTrendingServices("Book A Mission");
        FetchTrendingServices("Find a Avatar");
        FetchTrendingServices("Gifts");
        FetchTrendingServices("Goods");
        FetchTrendingServices("Furniture");
        FetchTrendingServices("Luggage");
        HomeSanitization_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Check Events");
                startActivity(intent);
            }
        });
        Electrician_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Book A Mission");
                startActivity(intent);
            }
        });
        Carpentering_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Find a Avatar");
                startActivity(intent);
            }
        });
        ACRepairing_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Gifts");
                startActivity(intent);
            }
        });
        Plumbing_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Goods");
                startActivity(intent);
            }
        });
        catering_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Furniture");
                startActivity(intent);
            }
        });
        hall_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Luggage");
                startActivity(intent);
            }
        });
        decorator_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Decorator");
                startActivity(intent);
            }
        });
        photo_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ServicesCategory.class);
                intent.putExtra("CATEGORY","Photographer");
                startActivity(intent);
            }
        });

        return itemView;
    }

    private void FetchTrendingServices(String stype) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Services").child(stype).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                        reference1.child("Services").child(stype).child(snapshot.getKey()).orderByChild("booked").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot1) {
                                if(dataSnapshot1 !=null){
                                    for(DataSnapshot snapshot1 : dataSnapshot1.getChildren()){
                                        String category = snapshot1.child("category").getValue().toString();
                                        String dateTime = snapshot1.child("dateTime").getValue().toString();
                                        String serviceDescription = snapshot1.child("serviceDescription").getValue().toString();
                                        String serviceID = snapshot1.child("serviceID").getValue().toString();
                                        //String serviceImage = snapshot1.child("serviceImage").getValue().toString();
                                        String serviceName = snapshot1.child("serviceName").getValue().toString();
                                        String servicePrice = snapshot1.child("servicePrice").getValue().toString();
                                        String servicePriceType = snapshot1.child("servicePriceType").getValue().toString();
                                        String serviceType = snapshot1.child("serviceType").getValue().toString();
                                        String booked = snapshot1.child("booked").getValue().toString();
                                        String providerID = snapshot1.child("providerID").getValue().toString();

                                        int bookedValue = Integer.parseInt(booked);
                                        if(bookedValue > 5)
                                            list.add(new TrendingServiceModel(serviceID,providerID,serviceName,serviceDescription,serviceType,servicePrice,servicePriceType,"serviceImage",dateTime,category,booked));
                                    }
                                    adapter = new TrendingServicesAdapter(getContext(),list);
                                    recyclerView.setAdapter(adapter);

                                    if(stype.equals("Decorator"))
                                        progressDialog.dismiss();
                                }
                                else{
                                    if(stype.equals("Decorator"))
                                        progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                if(stype.equals("Decorator"))
                                    progressDialog.dismiss();
                            }
                        });

                    }

                    if(stype.equals("Decorator"))
                        progressDialog.dismiss();
                }
                else{
                    if(stype.equals("Decorator"))
                        progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(stype.equals("Decorator"))
                    progressDialog.dismiss();
            }
        });
    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init(View v) {

        mPager = (ViewPager) v.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(),imageModelArrayList));

        CirclePageIndicator indicator = v.findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}