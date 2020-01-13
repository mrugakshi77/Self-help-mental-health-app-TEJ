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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.mentalhealth.Adapter.FreeSlotDocAdapter;
import com.example.mentalhealth.Model.FreeSlot;
import com.example.mentalhealth.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import static androidx.constraintlayout.widget.Constraints.TAG;

//Convert this to a recycler view if possible
public class UpdateFreeSlotFragment extends Fragment {
    private UpdateFreeSlotViewModel updateFreeSlotViewModel;

    private Button UpdateSlotButton;
    //private TextInputEditText doctorNameText;
    private TextView dateTextView;
    private TextView timeTextView;
    private EditText locationEditText;

    private String appointmentDate;
    private String appointmentTime;

    int day, month, year;
    Calendar calender;
    int hour, minutes;

    private DatabaseReference databaseReference, dbRef;
    private String addKey;
    private String location;
    private FreeSlot freeSlot = new FreeSlot();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        updateFreeSlotViewModel =
                ViewModelProviders.of(this).get(UpdateFreeSlotViewModel.class);
        View root = inflater.inflate(R.layout.fragment_updatefreeslot, container, false);

        dateTextView = root.findViewById(R.id.update_appointment_date);
        timeTextView = root.findViewById(R.id.update_appointment_time);
        locationEditText = root.findViewById(R.id.update_appointment_location);
        UpdateSlotButton = root.findViewById(R.id.update_to_db_button);

        calender = Calendar.getInstance();
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH);
        year = calender.get(Calendar.YEAR);
        month = month + 1;


        Bundle bundle = getArguments();
        if (bundle != null) {
            freeSlot = (FreeSlot) bundle.get("selected_freeSlot");
        }

        dateTextView.setText(freeSlot.getDate());
        timeTextView.setText(freeSlot.getTime());
        locationEditText.setText(freeSlot.getLocation());

        appointmentDate = freeSlot.getDate();
        appointmentTime = freeSlot.getTime();
        location = freeSlot.getLocation();


        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        dateTextView.setText(dayOfMonth + "/" + month + "/" + year);
                        StringBuilder builder = new StringBuilder();
                        builder.append(dayOfMonth);
                        builder.append("/");
                        builder.append(month);
                        builder.append("/");
                        builder.append(year);
                        appointmentDate = String.valueOf(builder);
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
                        timeTextView.setText(hourOfDay + ":" + minute);
                        StringBuilder builder = new StringBuilder();
                        builder.append(hourOfDay);
                        builder.append(":");
                        builder.append(minute);
                        appointmentTime = String.valueOf(builder);
                    }
                }, hour, minutes, android.text.format.DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("freeslots");
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        UpdateSlotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String locationUp = locationEditText.getText().toString().trim();
                Query query = databaseReference.orderByChild("slotId").equalTo(freeSlot.getSlotId());

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Map<String,FreeSlot> updateMap = new HashMap<>();
                        //updateMap.put("slot",freeSlot);
                        /*updateMap.put("date",appointmentDate);
                        updateMap.put("time",appointmentTime);
                        updateMap.put("location",locationUp);
*/
                        for (DataSnapshot freeSlotSnapShot : dataSnapshot.getChildren()) {
                            freeSlotSnapShot.getRef().child("date").setValue(appointmentDate);
                            freeSlotSnapShot.getRef().child("time").setValue(appointmentTime);
                            freeSlotSnapShot.getRef().child("location").setValue(locationUp);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });


            }
        });


        return root;
    }
}
