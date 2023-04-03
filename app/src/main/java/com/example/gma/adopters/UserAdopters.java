package com.example.gma.adopters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gma.MainActivity;
import com.example.gma.ProfileActivity;
import com.example.gma.R;
import com.example.gma.utils.CommonUtils;
import com.example.gma.utils.Keywords;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdopters extends RecyclerView.Adapter<UserAdopters.ViewHolder> {
    Context mContext;
    public ArrayList<HashMap<String, Object>> mDataSet;
    public UserAdopters(final Context context, ArrayList<HashMap<String, Object>> dataSet) {
        this.mDataSet = dataSet;
        this.mContext = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.card_user, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,Object> item =mDataSet.get(position);
        if(item.containsKey("imgUrl") &&  item.get("imgUrl") != null ){
            Picasso.get().load((String) item.get("imgUrl")).into(holder.image);

        }
        if(item.containsKey("username") &&  item.get("username") != null){

            holder.name.setText((String) item.get("username"));
        }
        if(item.containsKey("email") &&  item.get("email") != null){

            holder.email.setText((String) item.get("email"));
        }

        if(item.containsKey("status") && item.get("status") != null){
            if(((String) item.get("status")).equals(Keywords.USER_STATUS.enable)){
                holder.statusBtn.setText((String) item.get("status"));
                holder.statusBtn.setBackgroundColor(mContext.getResources().getColor(R.color.green_700, null));

                //Change Btn Color
            }else{
                holder.statusBtn.setBackgroundColor(mContext.getResources().getColor(R.color.vivid_700, null));
                holder.statusBtn.setText((String) item.get("status"));
                //Change Btn Color

            }
        }

        (holder.itemView).setOnClickListener(v->{
            if(item.get("id") != null){
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("id", (String) item.get("id"));
                mContext.startActivity(intent);
            }else{
                Toast.makeText(mContext, "No Avalible Id", Toast.LENGTH_SHORT).show();
            }


        });
        holder.view.setOnClickListener(v->{
            if(item.get("id") != null){
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("id", (String) item.get("id"));
                intent.putExtra("flag",true);
                mContext.startActivity(intent);
            }else{
                Toast.makeText(mContext, "No Avalible Id", Toast.LENGTH_SHORT).show();
            }


        });

        holder.statusBtn.setOnClickListener(v->{
            // Call the firebase and update Status
            updateStatus( (String) item.get("id"),position,holder.statusBtn);


        });


    }

    private void updateStatus(String id, int position,Button btn) {
        FirebaseDatabase database = FirebaseDatabase.getInstance().getReference().getDatabase();
        DatabaseReference ref = database.getReference().child(Keywords.FIREBASE_DOC.users).child(id);
        Map<String, Object> updates = new HashMap<>();

        if(((String) mDataSet.get(position).get("status")).equals(Keywords.USER_STATUS.enable)){
            updates.put("status",Keywords.USER_STATUS.disable);
        }else{
            updates.put("status", Keywords.USER_STATUS.enable);
        }
        ref.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if(((String) mDataSet.get(position).get("status")).equals(Keywords.USER_STATUS.enable)){
                        mDataSet.get(position).put("status",Keywords.USER_STATUS.disable);
                        btn.setBackgroundColor(mContext.getResources().getColor(R.color.green_700, null));
                    }else{
                        mDataSet.get(position).put("status",Keywords.USER_STATUS.enable);
                        btn.setBackgroundColor(mContext.getResources().getColor(R.color.vivid_700, null));
                    }
                   notifyDataSetChanged();
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name,email;
        private Button statusBtn,view;

        public ViewHolder(@NonNull View v) {
            super(v);
            image = v.findViewById(R.id.cardImageView);
            name = v.findViewById(R.id.name);
            statusBtn = v.findViewById(R.id.statusBtn);
            view = v.findViewById(R.id.view);
            email = v.findViewById(R.id.email);
        }
    }
}
