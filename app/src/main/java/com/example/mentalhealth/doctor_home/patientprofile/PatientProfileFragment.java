package com.example.mentalhealth.doctor_home.patientprofile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.Adapter.PatientAdapter;
import com.example.mentalhealth.Model.Patient;
import com.example.mentalhealth.R;

import java.util.List;

public class PatientProfileFragment extends Fragment {
    private PatientProfileViewModel viewModel;

    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        viewModel =
                ViewModelProviders.of(this).get(PatientProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_patient_profile_for_doc, container, false);

        recyclerView = root.findViewById(R.id.patient_card_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final PatientAdapter adapter = new PatientAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.setPatientEmail(getArguments().getString("email"));
        viewModel.setPatientData();
        LiveData<List<Patient>> patientLiveList = viewModel.getPatients();

        patientLiveList.observe(this, new Observer<List<Patient>>() {
            @Override
            public void onChanged(List<Patient> patients) {
                if (!patients.isEmpty()) {
                    adapter.setPatientList(patients);
                }
            }
        });


        return root;
    }
}
