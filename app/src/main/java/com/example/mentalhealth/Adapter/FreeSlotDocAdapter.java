package com.example.mentalhealth.Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.Model.FreeSlot;
import com.example.mentalhealth.R;
import com.example.mentalhealth.dataholder.FreeSlotDataHolder;
import com.example.mentalhealth.doctor_home.appointment.UpdateFreeSlotFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FreeSlotDocAdapter extends RecyclerView.Adapter<FreeSlotDocAdapter.FreeSlotDocHolder> {
    private List<FreeSlot> freeSlotList = new ArrayList<FreeSlot>();
    private FrameLayout layout;
    private FragmentManager fragmentManager;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public FrameLayout getLayout() {
        return layout;
    }

    public void setLayout(FrameLayout layout) {
        this.layout = layout;
    }


    @NonNull
    @Override
    public FreeSlotDocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.freeslot_doc, parent, false);
        return new FreeSlotDocHolder(itemView);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull FreeSlotDocHolder holder, int position) {
        final FreeSlot currentFreeSlot = freeSlotList.get(position);
        Log.e("testDL3", "" + currentFreeSlot.getDate());

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");
        //DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if ((LocalDate.now().isBefore(LocalDate.parse(currentFreeSlot.getDate(), formatter)))
                || (LocalDate.now().equals(LocalDate.parse(currentFreeSlot.getDate(), formatter)))) {
            holder.tv_date.setText(currentFreeSlot.getDate());
            holder.tv_time.setText(currentFreeSlot.getTime());
            holder.tv_location.setText(currentFreeSlot.getLocation());

            final FreeSlotDataHolder dataHolder = new FreeSlotDataHolder(currentFreeSlot.getDoctorEmail(),
                    currentFreeSlot.getLocation(), currentFreeSlot.getStatusBooked(), currentFreeSlot.getDate()
                    , currentFreeSlot.getTime(), currentFreeSlot.getSlotId());


            holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("freeslots");
                    Query query = reference.orderByChild("slotId").equalTo(currentFreeSlot.getSlotId());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot freeSlotSnapShot : dataSnapshot.getChildren()) {
                                freeSlotSnapShot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }
            });

            holder.updateImageButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onClick(View v) {
                    //FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    Fragment fragment = new UpdateFreeSlotFragment();
                    bundle.putSerializable("selected_freeSlot", dataHolder);
                    fragment.setArguments(bundle);
                    if (fragment != null) {
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.nav_host_fragment, fragment);
                        transaction.commit();
                    }

                }
            });
        }


    }


    @Override
    public int getItemCount() {
        return freeSlotList.size();
    }

    public void setFreeSlots(List<FreeSlot> allFreeSlots) {
        this.freeSlotList = allFreeSlots;
        notifyDataSetChanged();
    }

    class FreeSlotDocHolder extends RecyclerView.ViewHolder {

        private TextView tv_date;
        private TextView tv_time;
        private TextView tv_location;
        private ImageButton updateImageButton;
        private ImageButton deleteImageButton;

        public FreeSlotDocHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.fsdoc_date);
            tv_time = itemView.findViewById(R.id.fsdoc_time);
            tv_location = itemView.findViewById(R.id.fsdoc_location);
            updateImageButton = itemView.findViewById(R.id.updateFreeSlot_imageButton);
            deleteImageButton = itemView.findViewById(R.id.deleteFreeSlot_imageButton);
        }
    }
}

