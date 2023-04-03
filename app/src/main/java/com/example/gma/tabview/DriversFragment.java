package com.example.gma.tabview;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.gma.R;
import com.example.gma.adopters.UserAdopters;
import com.example.gma.utils.Keywords;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DriversFragment extends Fragment {
    private UserAdopters adapter;
    private ArrayList<HashMap<String, Object>> mDataSet;
    private RecyclerView userList;
    private RelativeLayout emptyLayout;

    private ArrayList<HashMap<String, Object>> s;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_drivers, container, false);
        mDataSet = new ArrayList<>();
        userList = view.findViewById(R.id.publicRecycler);
        adapter = new UserAdopters(getContext(), mDataSet);
        userList.setLayoutManager(new LinearLayoutManager(getActivity()));
        userList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        userList.setAdapter(adapter);
        getDataFromFireBase();
        // Inflate the layout for this fragment
        return view;
    }
    private void getDataFromFireBase() {
        FirebaseDatabase mDb = FirebaseDatabase.getInstance().getReference().getDatabase();
        DatabaseReference ref = mDb.getReference();
        ref.child(Keywords.FIREBASE_DOC.users).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isComplete() && task.getResult().getValue() != null){
                    HashMap<String, Object> data = (HashMap<String, Object>) task.getResult().getValue();
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if(((HashMap) value).containsKey("role")){
                            if(((HashMap) value).get("role").equals(Keywords.USER_TYPE.driver)){
                                mDataSet.add((HashMap<String, Object>) value);
                                adapter.notifyDataSetChanged();
                            }
                        }


                    }




                }

            }
        });
    }
}