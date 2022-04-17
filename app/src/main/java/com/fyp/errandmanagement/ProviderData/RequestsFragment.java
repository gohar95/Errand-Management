package com.fyp.errandmanagement.ProviderData;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import com.fyp.errandmanagement.R;


public class RequestsFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_requests, container, false);
        Intent sender = new Intent("custom-event-name");
        sender.putExtra("message", "Requests");
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(sender);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager = itemView.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = itemView.findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setupWithViewPager(mViewPager);

        return itemView;
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    UpcomingOrdersFragment upcoming = new UpcomingOrdersFragment();
                    return upcoming;
                case 1:
                    InProgressFragment inProgress = new InProgressFragment();
                    return inProgress;
                case 2:
                    CompletedOrderFragment completed = new CompletedOrderFragment();
                    return completed;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "UpComing";
                case 1:
                    return "In Progress";
                case 2:
                    return "Completed";
                default:
                    return null;
            }
        }
    }
}