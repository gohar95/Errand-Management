package com.fyp.errandmanagement.ProviderData;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.fyp.errandmanagement.R;

public class AccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_account, container, false);
        Intent sender = new Intent("custom-event-name");
        sender.putExtra("message", "Account");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(sender);

        return itemView;
    }
}