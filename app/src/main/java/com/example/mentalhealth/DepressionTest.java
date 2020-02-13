package com.example.mentalhealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager;
import com.google.firebase.ml.custom.FirebaseCustomLocalModel;
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
import java.util.ArrayList;


public class DepressionTest extends AppCompatActivity {

    Questions[] obj = new Questions[20];

    info.hoang8f.widget.FButton fButton;
    private QuestionnaireViewModel questionnaireViewModel;
    private RecyclerView recyclerView;
    private QuestionnaireAdapter questionnaireAdapter;
    RadioGroup rg;
    float[] probabilities = new float[1];
    FirebaseCustomLocalModel localModel;
    FirebaseCustomRemoteModel remoteModel;
    FirebaseModelInterpreter interpreter;
    FirebaseModelDownloadConditions conditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);


        rg = (RadioGroup) findViewById(R.id.radioGroup);
        fButton = findViewById(R.id.ques_submit);
        recyclerView = findViewById(R.id.question_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        questionnaireAdapter = new QuestionnaireAdapter();
        recyclerView.setAdapter(questionnaireAdapter);
        Log.i("START", "Model Loading");
        try {

            remoteModel = configureHostedModelSource();
            //Toast.makeText(getApplicationContext(), "Remote Model", Toast.LENGTH_SHORT).show();
            conditions = startModelDownloadTask(remoteModel);
            localModel = configureLocalModelSource();
            //Toast.makeText(getApplicationContext(), "Local Model", Toast.LENGTH_SHORT).show()
            //interpreter = createInterpreter(localModel);
            checkModelDownloadStatus(remoteModel, localModel);
            addDownloadListener(remoteModel, conditions);

        } catch (FirebaseMLException e) {
            e.printStackTrace();
        }


        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] selected_values = questionnaireAdapter.getSelectedOptions();

                int flag = 0;

                for (int i = 0; i < 20; i++) {
                    if (selected_values[i] == 0) {
                        flag = 1;
                    }
                }
                if (flag == 1) {
                    Toast.makeText(getApplicationContext(), "You haven't answered all questions!!", Toast.LENGTH_LONG).show();
                }

                String[] str = new String[20];

                for (int i = 0; i < 20; i++) {
                    str[i] = "Selected values for Ques" + (i + 1) + "  is - " + selected_values[i];
                }

                for (int i = 0; i < 20; i++) {
                    Log.d("Values", str[i]);
                }

                try {
                    //FirebaseModelInputOutputOptions inputOutputOptions = createInputOutputOptions();
                    runInference(selected_values);
                    Log.d("MLKit", String.format("%s: %f", "print", probabilities[0]));


                    //useInferenceResult();


                } catch (IOException | FirebaseMLException e) {
                    e.printStackTrace();
                }

                if (flag == 0) {
                    Intent i = new Intent(DepressionTest.this, Patient_feed.class);
                    startActivity(i);
                }

            }
        });
    }

    private FirebaseCustomRemoteModel configureHostedModelSource() throws FirebaseMLException {
        // [START mlkit_cloud_model_source]
        FirebaseCustomRemoteModel remoteModel =
                new FirebaseCustomRemoteModel.Builder("Questionnaire").build();
        return remoteModel;
        // [END mlkit_cloud_model_source]
    }

    private FirebaseModelDownloadConditions startModelDownloadTask(FirebaseCustomRemoteModel remoteModel) throws FirebaseMLException {
        // [START mlkit_model_download_task]
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
        return conditions;
        // [END mlkit_model_download_task]
    }

    private FirebaseCustomLocalModel configureLocalModelSource() throws FirebaseMLException {
        // [START mlkit_local_model_source]
        FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder()
                .setAssetFilePath("model.tflite")
                .build();

        return localModel;
        // [END mlkit_local_model_source]
    }

    private void checkModelDownloadStatus(
            final FirebaseCustomRemoteModel remoteModel,
            final FirebaseCustomLocalModel localModel) throws FirebaseMLException {
        // [START mlkit_check_download_status]
        Log.i("Check", "Function called");
        FirebaseModelManager.getInstance().isModelDownloaded(remoteModel)
                .addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean isDownloaded) {
                        FirebaseModelInterpreterOptions options;
                        if (isDownloaded) {
                            options = new FirebaseModelInterpreterOptions.Builder(remoteModel).build();

                            Log.i("MLKit", String.format("%s", "Remote model downloaded"));

                        } else {
                            options = new FirebaseModelInterpreterOptions.Builder(localModel).build();
                            System.out.println(options.toString());

                            Log.i("MLKit", String.format("%s", "Local model downloaded"));
                        }

                        try {
                            System.out.println(options.toString());
                            interpreter = FirebaseModelInterpreter.getInstance(options);
                        } catch (FirebaseMLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        // [END mlkit_check_download_status]
    }

    private void addDownloadListener(
            FirebaseCustomRemoteModel remoteModel,
            FirebaseModelDownloadConditions conditions) throws FirebaseMLException {
        // [START mlkit_remote_model_download_listener]
        FirebaseModelManager.getInstance().download(remoteModel, conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Download complete. Depending on your app, you could enable
                        // the ML feature, or switch from the local model to the remote
                        // model, etc.
                    }
                });
        // [END mlkit_remote_model_download_listener]
    }

    private FirebaseModelInputOutputOptions createInputOutputOptions() throws FirebaseMLException {
        // [START mlkit_create_io_options]
        FirebaseModelInputOutputOptions inputOutputOptions =
                new FirebaseModelInputOutputOptions.Builder()
                        .setInputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 20})
                        .setOutputFormat(0, FirebaseModelDataType.FLOAT32, new int[]{1, 4})
                        .build();
        // [END mlkit_create_io_options]

        return inputOutputOptions;
    }

    private void runInference(int[] selected_values) throws FirebaseMLException, IOException {
        //FirebaseCustomLocalModel localModel = new FirebaseCustomLocalModel.Builder().build();
        //FirebaseModelInterpreter firebaseInterpreter = createInterpreter(this.localModel);
        float[][] input = new float[1][20];

        for (int i = 0; i < 20; i += 1) {
            input[0][i] = selected_values[i];
        }

        FirebaseModelInputOutputOptions inputOutputOptions = createInputOutputOptions();


        // [START mlkit_run_inference]
        FirebaseModelInputs inputs = new FirebaseModelInputs.Builder()
                .add(input)  // add() as many input arrays as your model requires
                .build();

        //System.out.println(inputOutputOptions);


        interpreter.run(inputs, inputOutputOptions)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseModelOutputs>() {
                            @Override
                            public void onSuccess(FirebaseModelOutputs result) {
                                // [START_EXCLUDE]
                                // [START mlkit_read_result]

                                Toast.makeText(getApplicationContext(), "S", Toast.LENGTH_SHORT).show();
                                Log.i("MLKit", String.format("%s", "interpreter success"));


                                float[][] output = result.getOutput(0);

                                System.out.println(output);

                                probabilities = output[0];
                                System.out.println(probabilities[0]);
                                System.out.println(probabilities[1]);
                                System.out.println(probabilities[2]);
                                System.out.println(probabilities[3]);

                                float max = 0;
                                int index = 0;

                                for (int i = 0; i < 4; i++) {
                                    if (probabilities[i] > max) {
                                        max = probabilities[i];
                                        index = i;
                                    }
                                }

                                String label = "";
                                try {
                                    BufferedReader reader = new BufferedReader(
                                            new InputStreamReader(getAssets().open("labels.txt")));

                                    for (int i = 0; i <= index; i++) {
                                        label = reader.readLine();
                                    }


                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                System.out.println(label);
                                System.out.println(index);
                                // [END mlkit_read_result]
                                // [END_EXCLUDE]
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println(e.getMessage());
                                Toast.makeText(getApplicationContext(), "F", Toast.LENGTH_SHORT).show();
                                Log.i("MLKit", String.format("%s", "interpreter failure"));

                                // Task failed with an exception
                                // ...
                            }
                        });
        // [END mlkit_run_inference]
    }

    private void useInferenceResult() throws IOException {
        // [START mlkit_use_inference_result]
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(getAssets().open("labels.txt")));
        for (int i = 0; i < probabilities.length; i++) {

            //if(i==probabilities[0])
            {

                String label = reader.readLine();
                Log.d("MLKit", String.format("%s: %f", label, probabilities[0]));
                break;
            }
        }
        // [END mlkit_use_inference_result]
    }

}
