package com.example.mentalhealth;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentalhealth.Adapter.PostAdapter;

public class Questionnaire extends AppCompatActivity {

    Questions[] obj = new Questions[20];

    private QuestionnaireViewModel questionnaireViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);


        questionnaireViewModel = ViewModelProviders.of(this).get(QuestionnaireViewModel.class);

        recyclerView = findViewById(R.id.question_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        final QuestionnaireAdapter questionnaireAdapter = new QuestionnaireAdapter();
        recyclerView.setAdapter(questionnaireAdapter);


    }


}
