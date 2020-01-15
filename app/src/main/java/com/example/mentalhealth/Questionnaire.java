package com.example.mentalhealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Questionnaire extends AppCompatActivity {

    Questions[] obj = new Questions[20];

    info.hoang8f.widget.FButton fButton;
    private QuestionnaireViewModel questionnaireViewModel;
    private RecyclerView recyclerView;
    private QuestionnaireAdapter questionnaireAdapter;
    RadioGroup rg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

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

                String str1 = "Selected values for Ques 2 is - " +selected_values[1];
                String str2 = "Selected values for Ques 3 is - " +selected_values[2];
                String str3 = "Selected values for Ques 4 is - " +selected_values[3];
                String str4 = "Selected values for Ques 5 is - " +selected_values[4];

                Log.d("VALUES:", str1);
                Log.d("VALUES:", str2);
                Log.d("VALUES:", str3);
                Log.d("VALUES:", str4);

                //Toast.makeText(getApplicationContext(), selected_values[1], Toast.LENGTH_LONG).show();

                Intent i = new Intent(Questionnaire.this, Patient_feed.class);
                startActivity(i);
            }
        });

    }


}
