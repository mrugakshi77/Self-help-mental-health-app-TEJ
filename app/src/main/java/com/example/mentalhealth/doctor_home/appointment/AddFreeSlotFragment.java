package com.example.mentalhealth.doctor_home.appointment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.mentalhealth.Model.FreeSlot;
import com.example.mentalhealth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddFreeSlotFragment extends Fragment {
    private AddFreeSlotViewModel freeSlotViewModel;

    private Button addSlotButton;
    //private TextInputEditText doctorNameText;
    private TextView dateTextView;
    private TextView timeTextView;
    private EditText locationEditText;

    private String appointmentDate;
    private String appointmentTime;
    // private String appointmentLocation;
    int day, month, year;
    Calendar calender;
    int hour, minutes;

    private DatabaseReference databaseReference, dbRef;
    private String addKey;
    private String docEmail;
    static int slotID = 3;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        freeSlotViewModel =
                ViewModelProviders.of(this).get(AddFreeSlotViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addslot, container, false);

        dateTextView = root.findViewById(R.id.appointment_date);
        timeTextView = root.findViewById(R.id.appointment_time);
        locationEditText = root.findViewById(R.id.appointment_location);
        addSlotButton = root.findViewById(R.id.add_to_db_button);

        calender = Calendar.getInstance();
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH);
        year = calender.get(Calendar.YEAR);
        month = month + 1;

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String dateS = String.format("%02d-%02d-%d", dayOfMonth, month, year);
                        dateTextView.setText(dateS + "");
                       /* StringBuilder builder = new StringBuilder();
                        builder.append(dayOfMonth);
                        builder.append("/");
                        builder.append(month);
                        builder.append("/");
                        builder.append(year);*/
                        //String dateA = String.valueOf(builder)
                        appointmentDate = String.format("%02d-%02d-%d", dayOfMonth, month, year);
                        Log.e("date: ", appointmentDate + " ");

                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String timeS = String.format("%02d:%02d", hourOfDay, minute);
                        timeTextView.setText(timeS + "");
                       /* StringBuilder builder = new StringBuilder();
                        builder.append(hourOfDay);
                        builder.append(":");
                        builder.append(minute);*/
                        appointmentTime = String.format("%02d:%02d", hourOfDay, minute);
                    }
                }, hour, minutes, android.text.format.DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("freeslots");
        //dbRef = FirebaseDatabase.getInstance().getReference("Users");

        addSlotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FreeSlot slot = new FreeSlot();
                slot.setSlotId(String.valueOf(++slotID));
                slot.setDate(appointmentDate);
                slot.setTime(appointmentTime);
                slot.setLocation(locationEditText.getText().toString().trim());
                slot.setStatusBooked(false);
                addSlot(slot);
            }
        });

        return root;
    }

    private void addSlot(FreeSlot slot) {
        //slot.setDoctorEmail("d2@abc.com");
        slot.setDoctorEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        addKey = databaseReference.push().getKey();
        databaseReference.child(addKey).setValue(slot);


    }
}
