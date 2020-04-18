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

import com.example.mentalhealth.databinding.ActivityDoctorInfoBinding;
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


public class DoctorInfoActivity extends AppCompatActivity {

    info.hoang8f.widget.FButton fButton_setDp, fButton_submit;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    Spinner specialization;
    Spinner qualification;
    EditText describe_doctor;

    private DatabaseReference databaseRef, dbref;
    private StorageReference storageReference;
    private static int PICK_IMAGE_REQUEST = 1;
    private Uri uri;
    private StorageTask uploadTask;
    private ImageView displayPicture_doctor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_info);

        storageReference = FirebaseStorage.getInstance().getReference("DP_Doctors");
        dbref = FirebaseDatabase.getInstance().getReference("User");
        //databaseReference = FirebaseDatabase.getInstance().getReference("U");
        databaseRef = FirebaseDatabase.getInstance().getReference("downloadableURLs");


        ActivityDoctorInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_info);
        final DoctorInfoViewModel doctorInfoViewModel = ViewModelProviders.of(this).get(DoctorInfoViewModel.class);
        binding.setDoctorInfoViewModel(doctorInfoViewModel);
        binding.setLifecycleOwner(this);

        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawableResource(R.color.fbutton_color_transparent);

        displayPicture_doctor = findViewById(R.id.displayPicture_doctor);
        fButton_setDp =findViewById(R.id.setDp_doctor);
        fButton_submit =findViewById(R.id.submit_doctor);
        fButton_setDp.setButtonColor(getResources().getColor(R.color.colorWhite));
        fButton_submit.setButtonColor(getResources().getColor(R.color.colorWhite));

        fButton_setDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        specialization = (Spinner) findViewById(R.id.specialization);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<String>(DoctorInfoActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.specialization));
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialization.setAdapter(myAdapter1);

        qualification = (Spinner) findViewById(R.id.qualification);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(DoctorInfoActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.qualification));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qualification.setAdapter(myAdapter2);

        describe_doctor =(EditText) findViewById(R.id.describe_doctor);


        doctorInfoViewModel.getDoctorInfo().observe(this, new Observer<DoctorInfo>() {
            @Override
            public void onChanged(@Nullable DoctorInfo doctorInfo) {
                //if (doctorInfo.getDescribe().length() > 0)
                    saveDoctorInformation(doctorInfo);
            }
        });
    }

    private void saveDoctorInformation(final DoctorInfo doctorInfo) {
        //save info

        final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Some error has occurred", Toast.LENGTH_LONG).show();
                } else {
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Specialization").setValue(specialization.getSelectedItem().toString());
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Qualification").setValue(qualification.getSelectedItem().toString());
                    databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("Description").setValue(doctorInfo.getDescribe());

                    uploadFile();
                    //databaseReference.child("User").child(curr_user.getEmail().replace('.','&')).child("DP").setValue(uri);

                    Toast.makeText(getApplicationContext(), "Saving Doc Info", Toast.LENGTH_LONG).show();

                    redirect(curr_user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void redirect(FirebaseUser curr_user)
    {
        Intent i = new Intent(DoctorInfoActivity.this, Doctor_MainActivity.class);
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
            Picasso.get().load(uri).into(displayPicture_doctor);
            displayPicture_doctor.setImageURI(uri);
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
                        Uri downloadUri = task.getResult();
                        Log.d(TAG, "onComplete: Url: " + downloadUri.toString());
                        //final com.example.mentalhealth.Model.Upload upload = new com.example.mentalhealth.Model.Upload(editText.getText().toString().trim(), downloadUri.toString());

                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                //uploadPhoto(dataSnapshot.child(curr_user.getEmail().replace('.', '&')).child("mName").getValue().toString());

                                //uploadKey = databaseReference.push().getKey();
                                databaseReference.child("User").child(currentUser.getEmail().replace('.', '&')).child("DP").setValue(uri);
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
