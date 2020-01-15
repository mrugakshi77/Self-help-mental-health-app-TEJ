package com.example.mentalhealth.patient_navbar.patient_freeslots;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mentalhealth.Adapter.FreeSlotAdapter;
import com.example.mentalhealth.Model.FreeSlot;
import com.example.mentalhealth.R;
import com.example.mentalhealth.patient_navbar.patient_doclist.PatientDocListViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PatientFreeSlotsFragment extends Fragment {

    private PatientFreeSlotsViewModel patientFreeSlotsViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //set docemail here


        patientFreeSlotsViewModel =
                ViewModelProviders.of(this).get(PatientFreeSlotsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_patientfreeslots, container, false);

        recyclerView = root.findViewById(R.id.rv_freeslots);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final FreeSlotAdapter freeSlotAdapter = new FreeSlotAdapter();
        recyclerView.setAdapter(freeSlotAdapter);

        //patientFreeSlotsViewModel = ViewModelProviders.of(this).get(PatientFreeSlotsViewModel.class);
        Log.e("testfs2",""+getArguments().getString("docEmail"));
        patientFreeSlotsViewModel.setDocEmail(getArguments().getString("docEmail"));
        patientFreeSlotsViewModel.setFreeSlotData();
        LiveData<List<FreeSlot>> mylivedata = patientFreeSlotsViewModel.getFreeSlots();

        mylivedata.observe(this, new Observer<List<FreeSlot>>() {
            @Override
            public void onChanged(List<FreeSlot> freeSlots) {
                if(!freeSlots.isEmpty()){
                    Log.e("testDL2",""+freeSlots.get(0).getDate());
                    freeSlotAdapter.setFreeSlots(freeSlots);
                }
            }
        });

        return root;
    }
}
