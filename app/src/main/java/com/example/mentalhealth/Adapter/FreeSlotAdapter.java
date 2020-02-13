package com.example.mentalhealth.Adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mentalhealth.Model.Appointment;
import com.example.mentalhealth.Model.FreeSlot;
import com.example.mentalhealth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class FreeSlotAdapter extends RecyclerView.Adapter<FreeSlotAdapter.FreeSlotHolder> {


    private List<FreeSlot> freeSlotList = new ArrayList<FreeSlot>();


    @NonNull
    @Override
    public FreeSlotHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.freeslot, parent, false);
        return new FreeSlotHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeSlotHolder holder, int position) {
        final FreeSlot currentFreeSlot = freeSlotList.get(position);
        Log.e("testDL3", "" + currentFreeSlot.getDate());
        // if (!currentFreeSlot.getStatusBooked()) {

            holder.tv_date.setText(currentFreeSlot.getDate());
            holder.tv_time.setText(currentFreeSlot.getTime());
            holder.tv_location.setText(currentFreeSlot.getLocation());
            holder.tv_bookappt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //change status
                    DatabaseReference dbref_slots = FirebaseDatabase.getInstance().getReference("freeslots");
                    dbref_slots.orderByChild("slotId").equalTo(currentFreeSlot.getSlotId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ds.getRef().child("statusBooked").setValue(Boolean.TRUE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //add to appointments
                    Appointment appointment = new Appointment(currentFreeSlot, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString(), String.valueOf(1));
                    DatabaseReference dbref_appt = FirebaseDatabase.getInstance().getReference("Appointments").push();
                    dbref_appt.setValue(appointment).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //booked status
                        }
                    });


                }
            });
        }
    //}

    @Override
    public int getItemCount() {
        return freeSlotList.size();
    }

    public void setFreeSlots(List<FreeSlot> allFreeSlots) {
        this.freeSlotList = allFreeSlots;
        notifyDataSetChanged();
    }

    class FreeSlotHolder extends RecyclerView.ViewHolder {

        private TextView tv_date;
        private TextView tv_time;
        private TextView tv_location;
        private Button tv_bookappt;

        public FreeSlotHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.fs_date);
            tv_time = itemView.findViewById(R.id.fs_time);
            tv_location = itemView.findViewById(R.id.fs_location);
            tv_bookappt = itemView.findViewById(R.id.bookAppt);
        }
    }
}
