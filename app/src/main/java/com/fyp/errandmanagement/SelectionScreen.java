package com.fyp.errandmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.fyp.errandmanagement.CustomerData.CustomerLogin;
import com.fyp.errandmanagement.ProviderData.ProviderLogin;
import com.fyp.errandmanagement.R;


public class SelectionScreen extends AppCompatActivity {

    private RadioButton findid;
    private Button n_btn;
    private RadioGroup selectoption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_selection_screen);

        n_btn = (Button) findViewById(R.id.next_btn);

        n_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValueFromSelection();
            }
        });
    }

    public void getValueFromSelection(){
        selectoption = (RadioGroup) findViewById(R.id.selection);
        int selectedId = selectoption.getCheckedRadioButtonId();
        findid = (RadioButton) findViewById(selectedId);

        if(findid.getText().equals("Make an Errand")){
            Intent intent = new Intent(SelectionScreen.this, ProviderLogin.class);
            startActivity(intent);
        }
        else if(findid.getText().equals("Take an Errand")){
            Intent intent = new Intent(SelectionScreen.this, CustomerLogin.class);
            startActivity(intent);
        }
        else
            Toast.makeText(this, "Please Select Valid Option", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
