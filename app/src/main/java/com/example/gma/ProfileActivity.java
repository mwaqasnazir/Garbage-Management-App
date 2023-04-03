package com.example.gma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gma.models.Vehicle;
import com.example.gma.utils.CommonUtils;
import com.example.gma.utils.Keywords;
import com.example.gma.utils.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    EditText name, phone_number, email, gender, dateBirth, age;
    ImageView truckbtn, editbtn;
    Spinner type;

    String vehicleId ;

    Button btn;
    private Bundle args;
    ImageView imageView;
    FirebaseUser user;
    FirebaseDatabase mDatabase;
    DatabaseReference mDbRef;

    AlertDialog wDialog;
Session session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("PROFILE");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        session = new Session(ProfileActivity.this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Please wait a while");
        dialog.setCancelable(true);
        wDialog = dialog.create();


        user = FirebaseAuth.getInstance().getCurrentUser();
        mDbRef = FirebaseDatabase.getInstance().getReference().getDatabase().getReference();




        btn = findViewById(R.id.btn_save_profile);
        truckbtn = findViewById(R.id.viewTruck);
        editbtn = findViewById(R.id.edit);
        args = getIntent().getExtras();
        name = findViewById(R.id.Name);
        type = findViewById(R.id.type);
        gender = findViewById(R.id.gender);
        email = findViewById(R.id.email);
        imageView = findViewById(R.id.circleImageView);
        age = findViewById(R.id.Age);
        phone_number = findViewById(R.id.phone_number);
        dateBirth = findViewById(R.id.dateBirth);
        if(new Session(ProfileActivity.this).getUserRole().equals(Keywords.USER_TYPE.admin)){
            ArrayAdapter<String> AdopterRols = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"Roles",Keywords.USER_TYPE.publc, Keywords.USER_TYPE.driver,Keywords.USER_TYPE.admin});
            type.setAdapter(AdopterRols);
        }else {
            ArrayAdapter<String> AdopterRols = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"Roles",Keywords.USER_TYPE.publc, Keywords.USER_TYPE.driver});
            type.setAdapter(AdopterRols);
        }




        btn.setText("Update");
        btn.setVisibility(View.VISIBLE);
        //For Fields enabling when the user click the edit
        if (args != null) {
            name.setEnabled(true);
            type.setEnabled(true);
            gender.setEnabled(true);
            email.setEnabled(true);
            dateBirth.setOnClickListener(v -> {
                PopupTimeDialog(dateBirth, age);
            });
            if (args.containsKey("id")) {
                setFeildsValues(args.getString("id"));
                //To display update profile button for all updating
                if(args.getString("id").equals(user.getUid())){
                    btn.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(v -> {
                        checkValidation(args.getString("id"));
                    });

                }
            }
            else{
                ArrayAdapter<String> AdopterRols = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, new String[]{"Roles",Keywords.USER_TYPE.publc, Keywords.USER_TYPE.driver});
                type.setAdapter(AdopterRols);
                btn.setVisibility(View.GONE);
            }
        }
        else {
            wDialog.show();
            name.setEnabled(false);
            type.setEnabled(false);
            gender.setEnabled(false);
            email.setEnabled(false);
            phone_number.setEnabled(false);
            setFeildsValues(user.getUid());
            btn.setVisibility(View.GONE);
            setFeildsValues(user.getUid());


        }



    }

    private void setFeildsValues(String id){
        // Here i am getting data from database
        FirebaseDatabase database = FirebaseDatabase.getInstance().getReference().getDatabase();
        DatabaseReference ref = database.getReference();
        ref.child(Keywords.FIREBASE_DOC.users).child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                    HashMap<String, Object> dataMap = (HashMap<String, Object>) task.getResult().getValue();
                    if (dataMap != null) {
                        if(dataMap.get("username") != null){
                            name.setText((String) dataMap.get("username"));
                        }
                        if (dataMap.get("phoneNo") != null) {
                                phone_number.setText((String) dataMap.get("phoneNo"));
                            }
                        if (dataMap.get("imgUrl") != null) {
                            Picasso.get().load((String) dataMap.get("imgUrl")).into(imageView);
                        }
                        if (dataMap.get("email") != null) {
                            email.setText((String) dataMap.get("email"));
                        }
                        if (dataMap.get("datebirth") != null) {
                            String time = (String) dataMap.get("datebirth").toString();
                            dateBirth.setText(CommonUtils.longintoTime(time,"dd MMM yyyy"));
                            Calendar today = Calendar.getInstance();
                            Calendar birthDate = Calendar.getInstance();
                            birthDate.setTime(new Date(Long.parseLong(time)));
                            age.setText(String.valueOf(  today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR) ));
                        }
                        if (dataMap.get("gender") != null) {
                            gender.setText((String) dataMap.get("gender"));
                        }
                        if (dataMap.get("role") != null) {
                            if (((String) dataMap.get("role")).equals(Keywords.USER_TYPE.publc)) {
                                type.setSelection(1);
                            } else if(((String) dataMap.get("role")).equals(Keywords.USER_TYPE.driver)) {
                                if (dataMap.get("vehicleId") != null) {
                                    vehicleId = (String) dataMap.get("role");
                                }
                                type.setSelection(2);
                            } else if (((String) dataMap.get("role")).equals(Keywords.USER_TYPE.admin)) {
                                type.setSelection(3);

                            }
                        }
                    }

                }
                wDialog.dismiss();
            }
        });

    }

    private void checkValidation(String id) {
        if (TextUtils.isEmpty(name.getText().toString())) {

        } else if (TextUtils.isEmpty(phone_number.getText().toString())) {
            email.setError("Enter Mobile Number");

        } else if (TextUtils.isEmpty(type.getSelectedItem().toString()) && type.getSelectedItemPosition() != 0) {
            Toast.makeText(this, "Select Driver / Public", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Valid Mail");

        } else if (TextUtils.isEmpty(gender.getText().toString())) {
            gender.setError("Enter Gender");

        } else if (!gender.getText().toString().equals("Male") && !gender.getText().toString().equals("Female")) {
            gender.setError("Hint: Male / Female");

        } else if (TextUtils.isEmpty(dateBirth.getText().toString())) {
            dateBirth.setError("Select a Date");

        } else if (type.getSelectedItem().toString().equals("Driver")) {
            PopupVehicleDialog(id);

        } else {
            updateUserData(id,null);
        }
    }

    private void updateUserData(String id,String vehicleId) {
        // To update user profile
        wDialog.show();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance().getReference().getDatabase();
        DatabaseReference ref = database.getReference().child(Keywords.FIREBASE_DOC.users).child(id);

        Map<String, Object> updates = new HashMap<>();
        updates.put("datebirth", CommonUtils.milliseconds(dateBirth.getText().toString().trim()));
        updates.put("gender", gender.getText().toString());
        if(args != null){
            if(args.getString("id").equals(session.getUserid()) && session.getUserRole().equals(Keywords.USER_TYPE.admin)){
                updates.put("role", Keywords.USER_TYPE.admin);
                type.setSelection(3);
            }else{
                updates.put("role", type.getSelectedItem().toString());
            }
        }
        session.setUserRole(type.getSelectedItem().toString());
        updates.put("phoneNo", phone_number.getText().toString());
        updates.put("email", email.getText().toString());
        updates.put("username", name.getText().toString());
        if(user.getPhotoUrl()!=null){
            updates.put("imgUrl", user.getPhotoUrl().toString());
        }
        updates.put("status",Keywords.USER_STATUS.enable);
        if(type.getSelectedItem().toString().equals("Driver") && vehicleId != null){
            updates.put("vehicleId", vehicleId);
        }

        //Toast.makeText(this, updates.toString(), Toast.LENGTH_SHORT).show();

        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(ProfileActivity.this, "Try again other time ", Toast.LENGTH_SHORT).show();
                }
                wDialog.dismiss();
            }
        });


    }

    private void PopupTimeDialog(EditText date, EditText age) {
        // To display Calender to set Date of birth
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog datePD = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                String formattedDate = sdf.format(myCalendar.getTime());
                date.setText(formattedDate);
                Calendar calendar = Calendar.getInstance();
                int currentMonth, currentYear;

                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    currentMonth = calendar.get(Calendar.MONTH) + 1; // month is zero-based
                    currentYear = calendar.get(Calendar.YEAR);
                } else {
                    currentMonth = calendar.get(Calendar.MONTH);
                    currentYear = calendar.get(Calendar.YEAR);
                }
                age.setText(String.valueOf(currentYear - myCalendar.get(Calendar.YEAR)));


            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        //datePD.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePD.show();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem edit = menu.findItem(R.id.edit);
        MenuItem remove = menu.findItem(R.id.remove);
        if(args != null ){
            if(args.containsKey("id") ){
                edit.setVisible(false);
            }
        }
        if(args == null && session.getUserRole().equals(Keywords.USER_TYPE.admin)){
            remove.setVisible(false);

        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.edit:
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                intent.putExtra("id",FirebaseAuth.getInstance().getCurrentUser().getUid());
                startActivity(intent);

                break;
            case R.id.remove:

            if(args == null){
                deletingPopup(" your profile",user.getUid());
            }else if((!args.getString("id").equals(session.getUserid())) && session.getUserRole().equals(Keywords.USER_TYPE.admin)){
                   deletingPopup(name.getText().toString(),args.getString("id"));
               }else{
                   deletingPopup(" your profile",user.getUid());
               }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletingPopup(String name ,String id) {
        // To delete its profile is not available but still i am working on it
        wDialog.show();
        FirebaseUser user ;

        AlertDialog.Builder mProductDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = mProductDialog.create().getLayoutInflater();

        View view = inflater.inflate(R.layout.card_dialog, null);
        mProductDialog.setView(view);
        LinearLayout vehiclelayout =(LinearLayout) view.findViewById(R.id.vehicleDetail);
        LinearLayout removelayout =(LinearLayout) view.findViewById(R.id.removeLayout);
        TextView title = view.findViewById(R.id.dilogMessage);
        vehiclelayout.setVisibility(View.GONE);
        removelayout.setVisibility(View.VISIBLE);

        title.setText(title.getText().toString()+name+" ?");

        mProductDialog.setCancelable(true);
        mProductDialog.setPositiveButton("OK", null);


        mProductDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



            }
        });
        AlertDialog dialog = mProductDialog.create();
        dialog.show();

        Button btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(session.getUserRole().equals(Keywords.USER_TYPE.admin)){
                    //UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
                    Toast.makeText(ProfileActivity.this, "Not Avaliabe Yet", Toast.LENGTH_SHORT).show();
                }else {
                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            removeData(mDbRef,id);
                        }
                    });

                }


            }

        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }
    private void removeData(DatabaseReference mDbRef, String id) {
        mDbRef.child(Keywords.FIREBASE_DOC.users).child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if(type.getSelectedItemPosition()==2){
                    mDbRef.child(Keywords.FIREBASE_DOC.vehicles).child(vehicleId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            FirebaseAuth.getInstance().signOut();
                            wDialog.dismiss();
                            session.destroy();
                            Intent i = new Intent(ProfileActivity.this,ErrorActivity.class);
                            i.putExtra("key","deactivated");
                            finishAffinity();
                        }
                    });
                }else{
                    FirebaseAuth.getInstance().signOut();
                    wDialog.dismiss();
                    session.destroy();
                    Intent i = new Intent(ProfileActivity.this,ErrorActivity.class);
                    i.putExtra("key","deactivated");
                    finishAffinity();

                }


            }
        });
    }

    private void PopupVehicleDialog(String id) {
        AlertDialog.Builder mProductDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = mProductDialog.create().getLayoutInflater();
        View view = inflater.inflate(R.layout.card_dialog, null);
        mProductDialog.setView(view);

        AutoCompleteTextView model = (AutoCompleteTextView) view.findViewById(R.id.vehiclModel);
        EditText number = (EditText) view.findViewById(R.id.vehicleNo);
        EditText type = (EditText) view.findViewById(R.id.vehicleType);
        LinearLayout vehiclelayout =(LinearLayout) view.findViewById(R.id.vehicleDetail);
        LinearLayout removelayout =(LinearLayout) view.findViewById(R.id.removeLayout);
        vehiclelayout.setVisibility(View.VISIBLE);
        removelayout.setVisibility(View.GONE);
        mProductDialog.setCancelable(true);
        mProductDialog.setPositiveButton("OK", null);


        mProductDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = mProductDialog.create();
        dialog.show();

        Button theButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        theButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(model.getText().toString())) {
                    model.setText("Enter the Model of Vehicle");

                } else if (TextUtils.isEmpty(number.getText().toString())) {
                    number.setText("Enter the Number of Vehicle");

                } else if (TextUtils.isEmpty(type.getText().toString())) {
                    type.setText("Enter the Type of Vehicle");

                } else {
                    addVechelicDetail(id);
                }
            }

            private void addVechelicDetail(String userId) {
                Vehicle gaddi = new Vehicle();
                gaddi.type = type.getText().toString().trim();
                gaddi.model = model.getText().toString().trim();
                gaddi.vehicleNo = number.getText().toString().trim();
                FirebaseDatabase database = FirebaseDatabase.getInstance().getReference().getDatabase();
                gaddi.uId =FirebaseDatabase.getInstance().getReference().getDatabase().getReference().child("vehicles").push().getKey();
                DatabaseReference ref = database.getReference().child(Keywords.FIREBASE_DOC.vehicles).child(gaddi.uId);
                ref.setValue(gaddi).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateUserData(userId,gaddi.getuId());
                            dialog.dismiss();

                        }

                    }
                });

            }
        });
    }
}