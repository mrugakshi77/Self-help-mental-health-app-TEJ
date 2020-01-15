package com.example.mentalhealth.patient_navbar.patient_doclist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mentalhealth.Adapter.DocListAdapter;
import com.example.mentalhealth.Model.Doctor;
import com.example.mentalhealth.R;
import com.example.mentalhealth.patient_navbar.patient_home.PatientHomeViewModel;

import java.util.List;
import java.util.Observable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PatientDocListFragment extends Fragment {

    private PatientDocListViewModel patientDocListViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        patientDocListViewModel =
                ViewModelProviders.of(this).get(PatientDocListViewModel.class);
        View root = inflater.inflate(R.layout.fragment_patientappointment, container, false);

        recyclerView = root.findViewById(R.id.rv_doclist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final DocListAdapter docListAdapter = new DocListAdapter();
        docListAdapter.setFragmentManager(getFragmentManager());
        recyclerView.setAdapter(docListAdapter);

        patientDocListViewModel = ViewModelProviders.of(this).get(PatientDocListViewModel.class);
        LiveData<List<Doctor>> mylivedata = patientDocListViewModel.getDocsLiveData();

        mylivedata.observe(this, new Observer<List<Doctor>>() {
            @Override
            public void onChanged(List<Doctor> doctors) {
                if(!doctors.isEmpty()){
                    Log.e("testDL2",""+doctors.get(0).getEmail());
                    docListAdapter.setDoctorList(doctors);
                }
            }
        });

        return root;
    }

}
