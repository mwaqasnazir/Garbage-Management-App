package com.example.gma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.gma.models.User;
import com.example.gma.utils.Keywords;
import com.example.gma.utils.Session;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    int AUTHUI_REQUEST_CODE = 011;
    FirebaseDatabase database = FirebaseDatabase.getInstance().getReference().getDatabase();
    DatabaseReference ref = database.getReference();
    FirebaseUser auth  = FirebaseAuth.getInstance().getCurrentUser() ;
    Session session ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        session = new Session(SplashScreen.this);
        auth = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(auth != null){
                    haveAuthObj(auth,ref);
                }else{
                    handleLoginRegister();
                }
            }
        }, 200);
    }
    public void handleLoginRegister() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build()

        );

        Intent gogleSign = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls("https://example.com", "https://example.com")
                .setLogo(R.drawable.app_logo_s)
                .setTheme(R.style.CustomUI)
                .setAlwaysShowSignInMethodScreen(true)
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(gogleSign, AUTHUI_REQUEST_CODE);



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHUI_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                findViewById(R.id.projectName).setVisibility(View.GONE);
                // We have signed in the user or we have a new user
                auth = FirebaseAuth.getInstance().getCurrentUser();
                //Checking for User (New/Old)
                if (auth.getMetadata().getCreationTimestamp() == auth.getMetadata().getLastSignInTimestamp()) {
                    addUserData(auth,ref);
                    //This is a New User

                } else {
                    //For returning users
                    haveAuthObj(auth,ref);

                }
            }
            else {
                // Signing in failed
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Toast.makeText(this, "Cancelled the sign in request", Toast.LENGTH_SHORT).show();

                    Log.d("TAG", "onActivityResult: the user has cancelled the sign in request");
                } else {
                    Log.e("TAG", "onActivityResult: ", response.getError());
                }

            }
            finish();
        }
    }
    private void addUserData(FirebaseUser auth, DatabaseReference ref) {
        User obj = new User();
        if (auth.getUid()!=null){
            obj.id=auth.getUid();
        }if (auth.getEmail()!=null){
            obj.email=auth.getEmail();
        }if (auth.getPhoneNumber()!=null){
            obj.phoneNo=auth.getPhoneNumber();
        }if (auth.getDisplayName()!=null){
            obj.username=auth.getDisplayName();
        }if (auth.getPhotoUrl()!=null){
            obj.imgUrl=auth.getPhotoUrl().toString();
        }
        obj.status= Keywords.USER_STATUS.enable;
        ref.child(Keywords.FIREBASE_DOC.users).child(auth.getUid()).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SplashScreen.this, ProfileActivity.class);
                    intent.putExtra("id",auth.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void haveAuthObj(FirebaseUser auth, DatabaseReference ref){

        ref.child(Keywords.FIREBASE_DOC.users).child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) task.getResult().getValue();
                    if(dataMap != null){
                        String status = (String) dataMap.get("status").toString();
                        dataMap.containsKey("role");
                        if(status.equalsIgnoreCase(Keywords.USER_STATUS.enable)){
                            if(dataMap.containsKey("role") && dataMap.get("role") != null){
                                session.setUserRole((String)dataMap.get("role"));
                                session.setUserid(auth.getUid());
                                if(dataMap.get("role").equals(Keywords.USER_TYPE.admin)){
                                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else if (dataMap.get("role").equals(Keywords.USER_TYPE.driver)) {
                                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else if (dataMap.get("role").equals(Keywords.USER_TYPE.publc)) {
                                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                            else{
                                if(dataMap.containsKey("email")){
                                    if(dataMap.get("email").equals("wbutt1514@gmail.com")){
                                        session.setUserRole("Admin");
                                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                                Intent intent = new Intent(SplashScreen.this, ProfileActivity.class);
                                intent.putExtra("id",auth.getUid());
                                startActivity(intent);
                                finish();


                            }

                        }
                        else{
                            Intent intent = new Intent(SplashScreen.this, ErrorActivity.class);
                            intent.putExtra("key","disable Profile");
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        addUserData(auth,ref);
                    }

                }
            }
        });

    }
}