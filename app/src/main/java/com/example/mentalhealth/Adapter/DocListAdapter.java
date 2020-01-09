package com.example.mentalhealth.Adapter;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mentalhealth.Model.Doctor;
import com.example.mentalhealth.R;
import com.example.mentalhealth.patient_navbar.patient_freeslots.PatientFreeSlotsFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class DocListAdapter extends RecyclerView.Adapter<DocListAdapter.DocHolder> {

    private List<Doctor> doctorList = new ArrayList<Doctor>();
    FragmentManager fragmentManager;

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public DocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doctorcard, parent, false);
        return new DocHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DocHolder holder, int position) {
        final Doctor currentDoc = doctorList.get(position);
        Log.e("testDL3",""+currentDoc.getmName());
        holder.tv_docName.setText(currentDoc.getmName());
        holder.tv_docSpec.setText(currentDoc.getSpecialization());
        holder.tv_docQuali.setText(currentDoc.getQualification());
        holder.tv_viewSlots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String docEmail = currentDoc.getEmail();
                Bundle bundle = new Bundle();
                Fragment fragment = new PatientFreeSlotsFragment();
                bundle.putString("docEmail",currentDoc.getEmail());
                Log.e("testfs1",""+docEmail);
                fragment.setArguments(bundle);
                if(fragment!=null) {
                    fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_patient, fragment).commit();
                }
                //open freeslots fragment here with
            }
        });
    }



    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public void setDoctorList(List<Doctor> allDocs){
        this.doctorList = allDocs;
        notifyDataSetChanged();
    }

    class DocHolder extends RecyclerView.ViewHolder{

        private TextView tv_docName;
        private TextView tv_docSpec;
        private TextView tv_docQuali;
        private Button tv_viewSlots;


        public DocHolder(@NonNull View itemView) {
            super(itemView);
            tv_docName = itemView.findViewById(R.id.docName);
            tv_docQuali = itemView.findViewById(R.id.docQuali);
            tv_docSpec = itemView.findViewById(R.id.docSpec);
            tv_viewSlots = itemView.findViewById(R.id.viewSlots);
        }
    }
}
