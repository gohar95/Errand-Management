package com.fyp.errandmanagement.CustomerData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.errandmanagement.R;

public class ContactUsFragment extends Fragment {

    private RelativeLayout copy_em,copy_ad2;
    private ImageButton copy_p1, copy_p2;
    TextView ad2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_contact_us, container, false);

        ad2 = v.findViewById(R.id.tvNumber6);

        //Copy Phone 1
        copy_p1 = v.findViewById(R.id.copyPhoneOne);
        copy_p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(getActivity(),"+923036240478");
                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        //Copy Phone 2
        copy_p2 = v.findViewById(R.id.copyPhoneTwo);
        copy_p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setClipboard(getActivity(),"+923216013994");
                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        //Copy Email
        copy_em = v.findViewById(R.id.copyEmail);
        copy_em.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setClipboard(getActivity(),"maradeveloper9@gmail.com");
                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        //Copy Address 2
        copy_ad2 = v.findViewById(R.id.addressTwoCopy);
        copy_ad2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setClipboard(getActivity(), ad2.getText().toString());
                Toast.makeText(getActivity(), "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return v;
    }

    //Copy Phones Code
    @SuppressLint("ObsoleteSdkInt")
    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                clipboard.setText(text);
            }
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
            }
        }
    }
}