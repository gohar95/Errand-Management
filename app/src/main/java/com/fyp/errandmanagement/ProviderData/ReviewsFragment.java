package com.fyp.errandmanagement.ProviderData;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.fyp.errandmanagement.Models.ReviewModel;
import com.fyp.errandmanagement.R;

public class ReviewsFragment extends Fragment {

    TextView yourRating,five_review_count,four_review_count,three_review_count,two_review_count,one_review_count,peopleCount,jobCount;
    ProgressBar five_review_bar,four_review_bar,three_review_bar,two_review_bar,one_review_bar;
    Button readReviews;

    List<Float> oneList;
    List<Float> twoList;
    List<Float> threeList;
    List<Float> fourList;
    List<Float> fiveList;

    ArrayList<ReviewModel> reviewList;
    List<String> jobList;

    float myRating = 0.0f;

    FirebaseAuth auth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_reviews, container, false);
        Intent sender = new Intent("custom-event-name");
        sender.putExtra("message", "Reviews");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(sender);

        yourRating = v.findViewById(R.id.yourRating);
        five_review_count = v.findViewById(R.id.five_review_count);
        five_review_bar = v.findViewById(R.id.five_review_bar);
        four_review_count = v.findViewById(R.id.four_review_count);
        four_review_bar = v.findViewById(R.id.four_review_bar);
        three_review_count = v.findViewById(R.id.three_review_count);
        three_review_bar = v.findViewById(R.id.three_review_bar);
        two_review_count = v.findViewById(R.id.two_review_count);
        two_review_bar = v.findViewById(R.id.two_review_bar);
        one_review_count = v.findViewById(R.id.one_review_count);
        one_review_bar = v.findViewById(R.id.one_review_bar);
        readReviews = v.findViewById(R.id.readReviews);
        peopleCount = v.findViewById(R.id.peopleCount);
        jobCount = v.findViewById(R.id.jobCount);

        auth = FirebaseAuth.getInstance();
        oneList = new ArrayList<>();
        twoList = new ArrayList<>();
        threeList = new ArrayList<>();
        fourList = new ArrayList<>();
        fiveList = new ArrayList<>();
        reviewList = new ArrayList<>();
        jobList = new ArrayList<>();

        FetchallProgress();
        FetchJobs();

        readReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),ReadReviews.class));
            }
        });

        return v;
    }

    private void FetchJobs() {
        String provider = auth.getCurrentUser().getUid();

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        reference2.child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        String providerID = snapshot.child("providerID").getValue().toString();
                        String ostatus = snapshot.child("ostatus").getValue().toString();

                        if(provider.equals(providerID) && ostatus.equals("Completed")){
                            jobList.add(ostatus);
                        }
                    }

                    jobCount.setText(Integer.toString(jobList.size()));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                jobCount.setText("0");
            }
        });
    }

    private void FetchallProgress() {

        String provider = auth.getCurrentUser().getUid();

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        reference2.child("Reviews").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        String providerID = snapshot.child("providerID").getValue().toString();

                        if(provider.equals(providerID)){
                            String customerID = snapshot.child("customerID").getValue().toString();
                            String rating = snapshot.child("rating").getValue().toString();
                            String reviewText = snapshot.child("reviewText").getValue().toString();
                            String serviceID = snapshot.child("serviceID").getValue().toString();

                            float rate = Float.parseFloat(rating);

                            reviewList.add(new ReviewModel(reviewText,rating,serviceID,providerID,customerID));

                            if(rate == 4.5 || rate == 5 || rate > 5)
                                fiveList.add(rate);
                            if(rate == 3.5 || rate == 4)
                                fourList.add(rate);
                            if(rate == 2.5 || rate == 3)
                                threeList.add(rate);
                            if(rate == 1.5 || rate == 2)
                                twoList.add(rate);
                            if(rate == 0.5 || rate == 1)
                                oneList.add(rate);
                        }
                    }

                    if(reviewList.size() > 0){

                        for(int i=0; i<reviewList.size(); i++){
                           ReviewModel myRate = reviewList.get(i);
                            myRating = myRating + Float.parseFloat(myRate.getRating());
                        }
                        myRating = myRating/reviewList.size();
                        if(myRating >= 5.0) {
                            yourRating.setText("5.0");
                        }
                        else {
                            yourRating.setText(Float.toString(myRating));
                        }
                    }

                    //5 BAR data
                    if(fiveList.size() > 0){
                        myRating = 0.0f;
                        for(int i=0; i<fiveList.size(); i++){
                            float r5 = fiveList.get(i);
                            myRating = myRating + r5;
                        }
                        int value5 = (int) myRating;
                        five_review_bar.setProgress(value5);
                        five_review_count.setText(Integer.toString(fiveList.size()));
                    }
                    else{
                        five_review_bar.setProgress(0);
                        five_review_count.setText("0");
                    }

                    //4 BAR data
                    if(fourList.size() > 0){
                        myRating = 0.0f;
                        for(int i=0; i<fourList.size(); i++){
                            float r4 = fourList.get(i);
                            myRating = myRating + r4;
                        }
                        int value4 = (int) myRating;
                        four_review_bar.setProgress(value4);
                        four_review_count.setText(Integer.toString(fourList.size()));
                    }
                    else{
                        four_review_bar.setProgress(0);
                        four_review_count.setText("0");
                    }

                    //3 BAR data
                    if(threeList.size() > 0){
                        myRating = 0.0f;
                        for(int i=0; i<threeList.size(); i++){
                            float r3 = threeList.get(i);
                            myRating = myRating + r3;
                        }
                        int value3 = (int) myRating;
                        three_review_bar.setProgress(value3);
                        three_review_count.setText(Integer.toString(threeList.size()));
                    }
                    else{
                        three_review_bar.setProgress(0);
                        three_review_count.setText("0");
                    }

                    //2 BAR data
                    if(twoList.size() > 0){
                        myRating = 0.0f;
                        for(int i=0; i<twoList.size(); i++){
                            float r2 = twoList.get(i);
                            myRating = myRating + r2;
                        }
                        int value2 = (int) myRating;
                        two_review_bar.setProgress(value2);
                        two_review_count.setText(Integer.toString(twoList.size()));
                    }
                    else{
                        two_review_bar.setProgress(0);
                        two_review_count.setText("0");
                    }

                    //1 BAR data
                    if(oneList.size() > 0){
                        myRating = 0.0f;
                        for(int i=0; i<oneList.size(); i++){
                            float r1 = oneList.get(i);
                            myRating = myRating + r1;
                        }
                        int value1 = (int) myRating;
                        one_review_bar.setProgress(value1);
                        one_review_count.setText(Integer.toString(oneList.size()));
                    }
                    else{
                        one_review_bar.setProgress(0);
                        one_review_count.setText("0");
                    }

                    peopleCount.setText(Integer.toString(reviewList.size()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                yourRating.setText("0.0");
            }
        });
    }
}