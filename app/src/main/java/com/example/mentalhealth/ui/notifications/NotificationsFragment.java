package com.example.mentalhealth.ui.notifications;

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
import com.example.mentalhealth.RegisterActivity;

//My Account fragment for doctor. Currently holds a logout button and hardcoded userName.
public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private Button logoutButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        logoutButton = root.findViewById(R.id.logout_button);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationsViewModel.signOut();
                Intent finishIntent = new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(finishIntent);
            }
        });
        return root;
    }
}
