package com.example.mentalhealth.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.Model.Patient;
import com.example.mentalhealth.R;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<Patient> patients = new ArrayList<>();

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.patientdisplay, parent, false);
        return new PatientAdapter.PatientViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        final Patient currentPatient = patients.get(position);
        holder.tv_pName.setText(currentPatient.getmName());
        holder.tv_pEmail.setText(currentPatient.getEmail());
        holder.tv_pAge.setText(currentPatient.getmAge());
        holder.tv_pProfession.setText(currentPatient.getProfession());
        holder.tv_pDescription.setText(currentPatient.getDescription());
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void setPatientList(List<Patient> allPs) {
        this.patients = allPs;
        notifyDataSetChanged();
    }

    class PatientViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_pName;
        private TextView tv_pEmail;
        private TextView tv_pAge;
        private TextView tv_pProfession;
        private TextView tv_pDescription;


        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_pName = itemView.findViewById(R.id.patient_name_textview);
            tv_pEmail = itemView.findViewById(R.id.patient_email_textview);
            tv_pAge = itemView.findViewById(R.id.patient_age_textview);
            tv_pProfession = itemView.findViewById(R.id.patient_profession_textview);
            tv_pDescription = itemView.findViewById(R.id.patient_description_textview);
        }
    }

}
