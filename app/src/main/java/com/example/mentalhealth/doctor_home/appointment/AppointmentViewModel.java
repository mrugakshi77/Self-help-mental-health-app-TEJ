package com.example.mentalhealth.doctor_home.appointment;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.mentalhealth.FirebaseQueryLiveData;
import com.example.mentalhealth.Model.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class AppointmentViewModel extends ViewModel {

    private List<Appointment> appointments = new ArrayList<>();
    private static final DatabaseReference APPT_REF = FirebaseDatabase.getInstance().getReference("Appointments");
    private static final Query QUERY = APPT_REF.orderByChild("doctorEmail").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(QUERY);


    public LiveData<List<Appointment>> getAppointmentsLiveData() {
        LiveData<List<Appointment>> appointmentsLiveData = Transformations.map(liveData, new Deserializer());
        return appointmentsLiveData;
    }

    private class Deserializer implements Function<DataSnapshot, List<Appointment>> {

        @Override
        public List<Appointment> apply(DataSnapshot dataSnapshot) {
            appointments.clear();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                Appointment appointment = ds.getValue(Appointment.class);
                appointments.add(appointment);
            }
            return appointments;
        }
    }
}
