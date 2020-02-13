package com.example.mentalhealth.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.Model.Appointment;
import com.example.mentalhealth.Model.Doctor;
import com.example.mentalhealth.R;
import com.example.mentalhealth.doctor_home.patientprofile.PatientProfileFragment;
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

import static android.content.ContentValues.TAG;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder> {
    private List<Appointment> appointments = new ArrayList<>();
    private FragmentManager fragmentManager;


    public AppointmentAdapter() {
    }


    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.appointment, parent, false);
        return new AppointmentHolder(itemView, new AppointmentViewHolderClickListener() {
            @Override
            public void onItemClick(String email) {
                Bundle bundle = new Bundle();
                Fragment fragment = new PatientProfileFragment();
                bundle.putString("email", email);
                fragment.setArguments(bundle);
                if (fragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                }
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull AppointmentHolder holder, int position) {
        final Appointment appointment = appointments.get(position);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/M/yyyy");

            holder.appointment_patientName.setText("Patient : " + appointment.getPatientEmail());
            holder.appointment_time.setText("Appointment Time: " + appointment.getTime());
            holder.appointment__visitNumber.setText("Visit Number: " + String.valueOf(appointment.getVisitNumber()));
            holder.appointment_location.setText("Location: " + appointment.getLocation());
            holder.emailId = appointment.getPatientEmail();

       /*AppointmentDataHolder dataHolder = new AppointmentDataHolder(new FreeSlot((FreeSlot) appointment),
               appointment.getPatientEmail(),appointment.getVisitNumber());
        bundle = new Bundle();
        bundle.putSerializable("selected_patient",dataHolder);*/

            holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments");
                    Query query = reference.orderByChild("slotId").equalTo(appointment.getSlotId());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot freeSlotSnapShot : dataSnapshot.getChildren()) {
                                freeSlotSnapShot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e(Constraints.TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }
            });
        }


    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        Log.e(TAG, "" + appointments.get(0).getDoctorEmail());
        notifyDataSetChanged();
    }


    class AppointmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView appointment_patientName;
        private TextView appointment_time;
        private TextView appointment__visitNumber;
        private TextView appointment_location;
        private ImageButton deleteImageButton;

        public AppointmentViewHolderClickListener clickListener;
        private String emailId;

        public AppointmentHolder(@NonNull View itemView, AppointmentViewHolderClickListener clickListener) {
            super(itemView);
            appointment_patientName = itemView.findViewById(R.id.appt_patientEmail);
            appointment_time = itemView.findViewById(R.id.appt_time);
            appointment__visitNumber = itemView.findViewById(R.id.appt_visitNumber);
            appointment_location = itemView.findViewById(R.id.appt_location);
            deleteImageButton = itemView.findViewById(R.id.deleteAppt_imageButton);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(emailId);
        }
    }
}
