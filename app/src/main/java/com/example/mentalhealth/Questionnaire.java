package com.example.mentalhealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel;
import com.google.firebase.ml.custom.FirebaseModelDataType;
import com.google.firebase.ml.custom.FirebaseModelInputOutputOptions;
import com.google.firebase.ml.custom.FirebaseModelInputs;
import com.google.firebase.ml.custom.FirebaseModelInterpreter;
import com.google.firebase.ml.custom.FirebaseModelInterpreterOptions;
import com.google.firebase.ml.custom.FirebaseModelOutputs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Questionnaire extends AppCompatActivity {

    Questions[] obj = new Questions[20];

    info.hoang8f.widget.FButton fButton;
    private QuestionnaireViewModel questionnaireViewModel;
    private RecyclerView recyclerView;
    private QuestionnaireAdapter questionnaireAdapter;
    RadioGroup rg;
    FirebaseModelInputOutputOptions inputOutputOptions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        final FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel.Builder("Questionnaire").build();

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Success.
                    }
                });

        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel)
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean isDownloaded) {
                        FirebaseModelInterpreterOptions options;
                        //if (isDownloaded) {
                            options = new FirebaseModelInterpreterOptions.Builder(remoteModel).build();
                        //}
                        try {
                            FirebaseModelInterpreter interpreter = FirebaseModelInterpreter.getInstance(options);
                        } catch (FirebaseMLException e) {
                            e.printStackTrace();
                        }
                        // ...
                    }
                });

        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Download complete. Depending on your app, you could enable
                        // the ML feature, or switch from the local model to the remote
                        // model, etc.
                    }
                });


        try {
            inputOutputOptions = new FirebaseModelInputOutputOptions.Builder()
                    .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1,20})
                    .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 4})
                    .build();
        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }

        questionnaireViewModel = ViewModelProviders.of(this).get(QuestionnaireViewModel.class);

        rg = (RadioGroup)findViewById(R.id.radioGroup);
        fButton = findViewById(R.id.ques_submit);
        recyclerView = findViewById(R.id.question_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        questionnaireAdapter = new QuestionnaireAdapter();
        recyclerView.setAdapter(questionnaireAdapter);


        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] selected_values = questionnaireAdapter.getSelectedOptions();

                int flag=0;

                for(int i=0;i<20;i++)
                {
                    if(selected_values[i]==0)
                    {
                        flag=1;
                    }
                }
                if(flag==1)
                {
                    Toast.makeText(getApplicationContext(), "You haven't answered all questions!!", Toast.LENGTH_LONG).show();
                }

                String[] str = new String[20];

                for(int i=0;i<20;i++)
                {
                    str[i] = "Selected values for Ques" + (i+1) + "  is - " + selected_values[i];
                }

                for(int i=0;i<20;i++)
                {
                    Log.d("Values", str[i]);
                }



                FirebaseModelInputs inputs = null;
                try {
                    inputs = new FirebaseModelInputs.Builder()
                            .add(selected_values)  // add() as many input arrays as your model requires
                            .build();
                } catch (FirebaseMLException e) {
                    e.printStackTrace();
                }
                FirebaseModelInterpreter firebaseInterpreter = null;
                try {
                    firebaseInterpreter = createInterpreter(remoteModel);
                } catch (FirebaseMLException e) {
                    e.printStackTrace();
                }

                firebaseInterpreter.run(inputs, inputOutputOptions)
                        .addOnSuccessListener(
                                new OnSuccessListener<FirebaseModelOutputs>() {
                                    @Override
                                    public void onSuccess(FirebaseModelOutputs result) {
                                        // ...
                                        float[][] output = result.getOutput(0);
                                        float[] probabilities = output[0];

                                        BufferedReader reader = null;
                                        try {
                                            reader = new BufferedReader(
                                                    new InputStreamReader(getAssets().open("retrained_labels.txt")));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        for (int i = 0; i < probabilities.length; i++) {
                                            String label = null;
                                            try {
                                                label = reader.readLine();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            Log.i("MLKit", String.format("%s: %1.4f", label, probabilities[i]));
                                        }

                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        // ...
                                    }
                                });


                //Toast.makeText(getApplicationContext(), selected_values[1], Toast.LENGTH_LONG).show();

                if(flag==0){
                    Intent i = new Intent(Questionnaire.this, Patient_feed.class);
                    startActivity(i);
                }

            }
        });

    }

    private FirebaseModelInterpreter createInterpreter(FirebaseCustomRemoteModel remoteModel) throws FirebaseMLException {
        // [START mlkit_create_interpreter]
        FirebaseModelInterpreter interpreter = null;
        try {
            FirebaseModelInterpreterOptions options =
                    new FirebaseModelInterpreterOptions.Builder(remoteModel).build();
            interpreter = FirebaseModelInterpreter.getInstance(options);
        } catch (FirebaseMLException e) {
            // ...
        }
        // [END mlkit_create_interpreter]

        return interpreter;
    }

}
