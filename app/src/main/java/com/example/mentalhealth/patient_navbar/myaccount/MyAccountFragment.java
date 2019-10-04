package com.example.mentalhealth.patient_navbar.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mentalhealth.LoginActivity;
import com.example.mentalhealth.R;

public class MyAccountFragment extends Fragment {

    private MyAccountViewModel myAccountViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myAccountViewModel =
                ViewModelProviders.of(this).get(MyAccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myaccount, container, false);
        final TextView textView = root.findViewById(R.id.Username);
        Button logOut = root.findViewById(R.id.LogOut);
        myAccountViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAccountViewModel.signOut();
                Intent intent1 = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(intent1);
            }
        });

        return root;
    }
}