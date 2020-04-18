package com.example.mentalhealth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mentalhealth.databinding.ActivityPatientInfoBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class PatientInfoActivity extends AppCompatActivity {

    info.hoang8f.widget.FButton fButton_setDp, fButton_submit;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    Spinner marital_status;
    Spinner profession;
    EditText describe_patient;
    private StorageReference storageReference;
    private static int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private StorageTask uploadTask;
    private ImageView displayPicture_patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        storageReference = FirebaseStorage.getInstance().getReference("DP_Patients");

        ActivityPatientInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_info);
        final PatientInfoViewModel patientInfoViewModel = ViewModelProviders.of(this).get(PatientInfoViewModel.class);
        binding.setPatientInfoViewModel(patientInfoViewModel);
        binding.setLifecycleOwner(this);

        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawableResource(R.color.fbutton_color_transparent);

        displayPicture_patient = findViewById(R.id.displayPicture_patient);
        fButton_setDp =findViewById(R.id.setDp_patient);
        fButton_submit =findViewById(R.id.submit_patient);
        fButton_setDp.setButtonColor(getResources().getColor(R.color.colorWhite));
        fButton_submit.setButtonColor(getResources().getColor(R.color.colorWhite));

        fButton_setDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        marital_status = (Spinner) findViewById(R.id.marital_status);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(PatientInfoActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.marital_status));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marital_status.setAdapter(myAdapter1);

        profession = (Spinner) findViewById(R.id.profession);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(PatientInfoActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.profession));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        profession.setAdapter(myAdapter2);

        describe_patient =(EditText) findViewById(R.id.describe_patient);

        patientInfoViewModel.getPatientInfo().observe(this, new Observer<PatientInfo>() {
            @Override
            public void onChanged(@Nullable PatientInfo patientInfo) {
                //if (PatientInfo.getDescribe().length() > 0)
                //{
                    savePatientInformation(patientInfo);
                //}
            }
        });

    }

    private void savePatientInformation(final PatientInfo patientInfo) {

        //save info

        final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Some error has occurred", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Marital Status").setValue(marital_status.getSelectedItem().toString());
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Profession").setValue(profession.getSelectedItem().toString());
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Description").setValue(patientInfo.getDescribe());

                    //databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Profile Photo").setValue(patientInfo.getDescribe());

                    uploadFile();

                    //databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("DP").setValue(uri);

                    //Toast.makeText(getApplicationContext(), "Saving Patient Info", Toast.LENGTH_LONG).show();

                    redirect(curr_user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void redirect(FirebaseUser curr_user) {

        Intent i = new Intent(PatientInfoActivity.this, DepressionTest.class);
        startActivity(i);
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(displayPicture_patient);
            displayPicture_patient.setImageURI(uri);
        }
    }


    private void uploadFile() {
        if (uri != null) {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            uploadTask = reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    /*handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //progressBar.setProgress(0);
                        }
                    }, 5000);*/
                    Toast.makeText(getApplicationContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    //progressBar.setProgress((int) progress);
                }
            });
            reference.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        final Uri downloadUri = task.getResult();
                        Log.d(TAG, "onComplete: Url: " + downloadUri.toString());
                        //final com.example.mentalhealth.Model.Upload upload = new com.example.mentalhealth.Model.Upload(editText.getText().toString().trim(), downloadUri.toString());

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                //uploadPhoto(dataSnapshot.child(curr_user.getEmail().replace('.', '&')).child("mName").getValue().toString());

                                //uploadKey = databaseReference.push().getKey();
                                databaseReference.child("User").child(currentUser.getEmail().replace('.', '&')).child("DP").setValue(downloadUri.toString());
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //upload.setUserName(uname);
                        // uploadKey = databaseReference.push().getKey();
                        //databaseReference.child(uploadKey).setValue(upload);

                    }
                }
            });

        } else
            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_SHORT).show();
    }

}
