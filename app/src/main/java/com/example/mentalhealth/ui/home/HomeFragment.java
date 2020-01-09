package com.example.mentalhealth.ui.home;

import android.os.Bundle;
import android.util.Log;
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
import com.example.mentalhealth.Model.Upload;
import com.example.mentalhealth.R;

import java.util.List;

//Shows old posts posted by a doctor in a recycler view.
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        final PostAdapter postAdapter = new PostAdapter();
        recyclerView.setAdapter(postAdapter);

        LiveData<List<Upload>> myLiveData = homeViewModel.getPostsLiveData();

        myLiveData.observe(this, new Observer<List<Upload>>() {
            @Override
            public void onChanged(List<Upload> posts) {
                if (!posts.isEmpty()) {
                    Log.e("test3", "" + posts.get(0).getUserName());
                    postAdapter.setPosts(posts);
                }
            }
        });


        return root;
    }
}
