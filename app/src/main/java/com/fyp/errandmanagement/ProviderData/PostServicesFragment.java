package com.fyp.errandmanagement.ProviderData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fyp.errandmanagement.Adapters.MyServicesAdapter;
import com.fyp.errandmanagement.Models.AnyServiceModel;
import com.fyp.errandmanagement.R;

public class PostServicesFragment extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener{

    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaBtn;
    private RapidFloatingActionHelper rfabHelper;

    private RecyclerView recyclerView;
    private ArrayList<AnyServiceModel> list;
    MyServicesAdapter adapter;
    private TextView nothingHere;
    ProgressDialog progressDialog;

    FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_post_services, container, false);
        Intent sender = new Intent("custom-event-name");
        sender.putExtra("message", "Post Errand");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(sender);

        rfaLayout = itemView.findViewById(R.id.activity_main_rfal);
        rfaBtn = itemView.findViewById(R.id.activity_main_rfab);

        recyclerView = itemView.findViewById(R.id.plumingServiceRecycler);
        list = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nothingHere = itemView.findViewById(R.id.nothingHere);

        auth = FirebaseAuth.getInstance();

        nothingHere.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Check Events")
                .setResId(R.drawable.food)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setLabelColor(0xffd84315)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Book A Mission")
                .setResId(R.drawable.grocery)
                .setIconNormalColor(0xff4e342e)
                .setIconPressedColor(0xff3e2723)
                .setLabelColor(0xff4e342e)
                .setLabelSizeSp(14)
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Find an Avatar")
                .setResId(R.drawable.document)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Gifts")
                .setResId(R.drawable.giftbox)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(0xff283593)
                .setWrapper(3)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Goods")
                .setResId(R.drawable.goods)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(0xff283593)
                .setWrapper(4)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Furniture")
                .setResId(R.drawable.furniture)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(0xff283593)
                .setWrapper(5)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel("Luggage")
                .setResId(R.drawable.luggage)
                .setIconNormalColor(0xff283593)
                .setIconPressedColor(0xff1a237e)
                .setLabelColor(0xff283593)
                .setWrapper(6)
        );
        rfaContent
                .setItems(items)
                .setIconShadowColor(0xff888888);
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                rfaLayout,
                rfaBtn,
                rfaContent
        ).build();

        /*progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Fetching Your Services");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();*/

        FetchingServices("Check Events");
        FetchingServices("Book A Mission");
        FetchingServices("Find a Avatar");
        FetchingServices("Gifts");
        FetchingServices("Goods");
        FetchingServices("Furniture");
        FetchingServices("Luggage");

        return itemView;
    }

    private void FetchingServices(String stype) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Services").child(stype).child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot !=null){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        String category = snapshot.child("category").getValue().toString();
                        String dateTime = snapshot.child("dateTime").getValue().toString();
                        String serviceDescription = snapshot.child("serviceDescription").getValue().toString();
                        String serviceID = snapshot.child("serviceID").getValue().toString();

                        String serviceName = snapshot.child("serviceName").getValue().toString();
                        String servicePrice = snapshot.child("servicePrice").getValue().toString();
                        String servicePriceType = snapshot.child("servicePriceType").getValue().toString();
                        String serviceType = snapshot.child("serviceType").getValue().toString();
                        String booked = snapshot.child("booked").getValue().toString();
                        String providerID = snapshot.child("providerID").getValue().toString();

                        list.add(new AnyServiceModel(serviceID,providerID,serviceName,serviceDescription,serviceType,servicePrice,servicePriceType,dateTime,category,booked));
                    }
                    adapter = new MyServicesAdapter(getContext(),list);
                    recyclerView.setAdapter(adapter);

                    nothingHere.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

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

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        Intent i = new Intent(getContext(), PostingServices.class);
        i.putExtra("TYPE",item.getLabel());
        startActivity(i);
        rfabHelper.toggleContent();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        Intent i = new Intent(getContext(), PostingServices.class);
        i.putExtra("TYPE",item.getLabel());
        startActivity(i);
        rfabHelper.toggleContent();
    }
}