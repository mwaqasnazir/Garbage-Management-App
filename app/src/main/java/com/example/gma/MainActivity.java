package com.example.gma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.gma.utils.ContainersActivity;
import com.example.gma.tabview.UsersActivity;
import com.example.gma.utils.Keywords;
import com.example.gma.utils.Session;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // Variable Declaration
    private CardView track_truck_card, users_card, containers_card, tasks_card, complaints_card, feedback_card;
    FirebaseAuth auth;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth= FirebaseAuth.getInstance();
        session=new Session(MainActivity.this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("HOME");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.home);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Defining Cards / Creating Hooks
        track_truck_card = (CardView) findViewById(R.id.track_trcuk_card);
        users_card = (CardView) findViewById(R.id.users_card);
        containers_card = (CardView) findViewById(R.id.containers_card);
        tasks_card = (CardView) findViewById(R.id.tasks_card);
        complaints_card = (CardView) findViewById(R.id.complaints_card);
        feedback_card = (CardView) findViewById(R.id.feedback_card);

        // Adding Onclicklisteners
        track_truck_card.setOnClickListener(this);
        users_card.setOnClickListener(this);
        containers_card.setOnClickListener(this);
        tasks_card.setOnClickListener(this);
        complaints_card.setOnClickListener(this);
        feedback_card.setOnClickListener(this);

        // Here i am displaying cards for different users
        if(session.getUserRole().equals(Keywords.USER_TYPE.admin)){ // For Admin
            track_truck_card.setVisibility(View.VISIBLE);
            users_card.setVisibility(View.VISIBLE);
            containers_card.setVisibility(View.VISIBLE);
            tasks_card.setVisibility(View.VISIBLE);
            complaints_card.setVisibility(View.VISIBLE);
            feedback_card.setVisibility(View.VISIBLE);

        } else if (session.getUserRole().equals(Keywords.USER_TYPE.driver)) {// For Driver
            tasks_card.setVisibility(View.VISIBLE);
            containers_card.setVisibility(View.VISIBLE);

        } else if (session.getUserRole().equals(Keywords.USER_TYPE.publc)) {// For Public
            feedback_card.setVisibility(View.VISIBLE);
            complaints_card.setVisibility(View.VISIBLE);

        }else{



        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_navbar_menu, menu);
        return true;
    }
    public void logout(MenuItem menu) {
        if(auth != null){
            session.destroy();
            auth.signOut();
            startActivity(new Intent(MainActivity.this,SplashScreen.class));
            finishAffinity();
            finish();
        }

    }
    public void gotoProfile(MenuItem menu) {
        if(auth != null){
            startActivity(new Intent(MainActivity.this,ProfileActivity.class));

        }}

    // For CardView Onclick to go from card to activity
    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.track_trcuk_card:
                i = new Intent(this, TrackTruckActivity.class);
                startActivity(i);
                break;
            case R.id.users_card:
                i = new Intent(this, UsersActivity.class);startActivity(i);
                break;
            case R.id.containers_card:
                i = new Intent(this, ContainersActivity.class);startActivity(i);
                break;
            case R.id.tasks_card:

                //More Short :)
                startActivity(new Intent(this, TasksActivity.class));
                break;
            case R.id.complaints_card:
                i = new Intent(this, ComplaintsActivity.class);startActivity(i);
                break;
            case R.id.feedback_card:
                i = new Intent(this, FeedbackActivity.class);startActivity(i);
                Default:
                break;
        }
    }
}