package com.example.mentalhealth.patient_navbar.patient_home;

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

import com.example.mentalhealth.Adapter.PostAdapter;
import com.example.mentalhealth.R;
import com.example.mentalhealth.Model.Upload;

import java.util.List;

public class PatientHomeFragment extends Fragment {

    private PatientHomeViewModel patientHomeViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        patientHomeViewModel =
                ViewModelProviders.of(this).get(PatientHomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_patienthome, container, false);


        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final PostAdapter postAdapter = new PostAdapter();
        recyclerView.setAdapter(postAdapter);

        PatientHomeViewModel patientHomeViewModel = ViewModelProviders.of(this).get(PatientHomeViewModel.class);
        LiveData<List<Upload>> myLiveData = patientHomeViewModel.getPostsLiveData();

        myLiveData.observe(this, new Observer<List<Upload>>() {
            @Override
            public void onChanged(List<Upload> posts) {
                if (!posts.isEmpty()) {
                    //  Log.e("test3",""+posts.get(0).getUserName());
                    postAdapter.setPosts(posts);
                }
            }
        });


        return root;
    }
}