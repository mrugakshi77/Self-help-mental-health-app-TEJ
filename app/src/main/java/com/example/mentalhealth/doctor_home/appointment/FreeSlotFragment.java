package com.example.mentalhealth.doctor_home.appointment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.Adapter.FreeSlotAdapter;
import com.example.mentalhealth.Adapter.FreeSlotDocAdapter;
import com.example.mentalhealth.Model.FreeSlot;
import com.example.mentalhealth.R;
import com.example.mentalhealth.patient_navbar.patient_freeslots.PatientFreeSlotsViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class FreeSlotFragment extends Fragment {
    private FreeSlotViewModel freeSlotViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //set docemail here


        freeSlotViewModel =
                ViewModelProviders.of(this).get(FreeSlotViewModel.class);
        View root = inflater.inflate(R.layout.fragment_freeslotsdoc, container, false);

        recyclerView = root.findViewById(R.id.recyclerViewFreeSlotsDoc);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final FreeSlotDocAdapter freeSlotAdapter = new FreeSlotDocAdapter();
        freeSlotAdapter.setFragmentManager(getFragmentManager());
        //freeSlotAdapter.setLayout(R.id.nav_host_fragment);
        recyclerView.setAdapter(freeSlotAdapter);


        //patientFreeSlotsViewModel = ViewModelProviders.of(this).get(PatientFreeSlotsViewModel.class);
        //Log.e("testfs2",""+getArguments().getString("docEmail"));
        freeSlotViewModel.setDocEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        freeSlotViewModel.setFreeSlotData();
        LiveData<List<FreeSlot>> mylivedata = freeSlotViewModel.getFreeSlots();

        mylivedata.observe(this, new Observer<List<FreeSlot>>() {
            @Override
            public void onChanged(List<FreeSlot> freeSlots) {
                if (!freeSlots.isEmpty()) {
                    Log.e("testDL2", "" + freeSlots.get(0).getDate());
                    freeSlotAdapter.setFreeSlots(freeSlots);
                }
            }
        });

        return root;
    }
}
