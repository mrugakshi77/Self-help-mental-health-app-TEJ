package com.example.mentalhealth.doctor_home.appointment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.Adapter.AppointmentAdapter;
import com.example.mentalhealth.LoginActivity;
import com.example.mentalhealth.Model.Appointment;
import com.example.mentalhealth.Patient_feed;
import com.example.mentalhealth.R;
import com.example.mentalhealth.doctor_home.notifications.NotificationsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class AppointmentFragment extends Fragment {

    private AppointmentViewModel appointmentViewModel;
    private FloatingActionButton addSlotButton;
    private FloatingActionButton viewSlotsButton;
    private RecyclerView recyclerView;
    private CardView appointmentCardView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        appointmentViewModel =
                ViewModelProviders.of(this).get(AppointmentViewModel.class);
        View root = inflater.inflate(R.layout.fragment_appointmentsd, container, false);


        addSlotButton = root.findViewById(R.id.addFreeSlot_fabButton);


        viewSlotsButton = root.findViewById(R.id.seeFreeSlot_fabButton);
        recyclerView = root.findViewById(R.id.recyclerViewApptsDoc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.hasFixedSize();
        final AppointmentAdapter adapter = new AppointmentAdapter();
        adapter.setFragmentManager(getFragmentManager());
        recyclerView.setAdapter(adapter);


        appointmentViewModel = ViewModelProviders.of(this).get(AppointmentViewModel.class);
        LiveData<List<Appointment>> myLiveData = appointmentViewModel.getAppointmentsLiveData();

        myLiveData.observe(this, new Observer<List<Appointment>>() {
            @Override
            public void onChanged(List<Appointment> appointments) {
                if (!appointments.isEmpty()) {
                    adapter.setAppointments(appointments);
                }
            }
        });



        /*appointmentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                PatientFragment fragment = new PatientFragment();
                fragmentTransaction.add(R.id.nav_host_fragment, fragment);
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();
            }
        });*/

        viewSlotsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                FreeSlotFragment fragment = new FreeSlotFragment();
                fragmentTransaction.add(R.id.nav_host_fragment, fragment);
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();
            }
        });

        addSlotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction fragmentTransaction = manager.beginTransaction();
                AddFreeSlotFragment fragment = new AddFreeSlotFragment();
                fragmentTransaction.add(R.id.nav_host_fragment, fragment);
                fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
                fragmentTransaction.commit();
            }
        });


        return root;
    }

}
