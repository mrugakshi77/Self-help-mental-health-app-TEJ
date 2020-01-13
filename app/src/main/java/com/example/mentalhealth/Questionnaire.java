package com.example.mentalhealth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.Adapter.PostAdapter;

public class Questionnaire extends AppCompatActivity {

    Questions[] obj = new Questions[20];

    info.hoang8f.widget.FButton fButton;
    private QuestionnaireViewModel questionnaireViewModel;
    private RecyclerView recyclerView;
    private QuestionnaireAdapter questionnaireAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);


        questionnaireViewModel = ViewModelProviders.of(this).get(QuestionnaireViewModel.class);

        fButton = findViewById(R.id.ques_submit);
        recyclerView = findViewById(R.id.question_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        questionnaireAdapter = new QuestionnaireAdapter();
        recyclerView.setAdapter(questionnaireAdapter);


        fButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Questionnaire.this, Patient_feed.class);
                startActivity(i);
            }
        });

    }


}
