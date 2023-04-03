package com.example.gma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ErrorActivity extends AppCompatActivity {

    Bundle arges;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        arges=getIntent().getExtras();

        if(arges != null){
            if(arges.containsKey("key")){
                if(arges.getString("key").contains("disable")){
                    setTitle("Message");
                    layout=findViewById(R.id.disabledScreen);
                    layout.setVisibility(View.VISIBLE);
                }else if(arges.getString("key").contains("deactivated")){
                    setTitle("Message");
                    layout=findViewById(R.id.deactivatedScreen);
                    layout.setVisibility(View.VISIBLE);
                }

            }

        }
    }
}