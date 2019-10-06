package com.example.mentalhealth.ui.dashboard;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mentalhealth.Model.Upload;
import com.example.mentalhealth.R;
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

import java.net.URI;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

//Doctor Add Post fragment. Add text and images from local device to firebase.
public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private StorageReference storageReference;
    private static int PICK_IMAGE_REQUEST = 1;

    private Button chooseFileButton;
    private Button uploadButton;
    private EditText editText;
    private ImageView imageView;
    private ProgressBar progressBar;

    private Uri uri;

    private DatabaseReference databaseReference, dbref;
    private DatabaseReference databaseRef;
    private StorageTask uploadTask;
    private String downloadURL;
    private String uploadKey;
    private String uname;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        editText = root.findViewById(R.id.edit_box);
        chooseFileButton = root.findViewById(R.id.choose_button);
        uploadButton = root.findViewById(R.id.upload_button);
        imageView = root.findViewById(R.id.image_view);
        progressBar = root.findViewById(R.id.progress_bar);

        storageReference = FirebaseStorage.getInstance().getReference("posts");
        dbref = FirebaseDatabase.getInstance().getReference("User");
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseRef = FirebaseDatabase.getInstance().getReference("downloadableURLs");
        chooseFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "Other task in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                    //uploadDownloadableURLs();
                }

            }
        });
        return root;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(imageView);
            imageView.setImageURI(uri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Uploads an image to firebase storage and posts to database
    private void uploadFile() {
        if (uri != null) {
            final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            uploadTask = reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 5000);
                    Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
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
                        final Upload upload = new Upload(editText.getText().toString().trim(), downloadUri.toString());

                        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
                                uploadPost(upload, dataSnapshot.child(curr_user.getEmail().replace('.', '&')).child("mName").getValue().toString());

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
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
    }
    private void uploadPost(Upload u, String s){
        uname = s;
        Log.e("String",""+s+"uname: "+uname);
        u.setUserName(uname);
        Log.e("Striing", ""+u.getUserName()+"imageuri"+u.getImageUrl());
        u.setUserEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        uploadKey = databaseReference.push().getKey();
        databaseReference.child(uploadKey).setValue(u);

    }

   /* private void uploadDownloadableURLs()
    {
        if(!downloadURL.isEmpty())
        {
            DownladableURL downladableURL = new DownladableURL(downloadURL);

        }



    }*/


}
