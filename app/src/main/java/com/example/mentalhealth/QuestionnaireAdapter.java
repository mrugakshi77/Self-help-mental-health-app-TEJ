package com.example.mentalhealth;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.QuestionnaireHolder>  {

    List<String> obj = new ArrayList<>();
    int[] values = new int[20];

    String questions[] = {
            "1. I feel down-hearted and blue.",
            "2. Morning is when I feel the best.",
            "3. I have crying spells or feel like it.",
            "4. I have trouble sleeping at night.",
            "5. I eat as much as I used to.",
            "6. I still enjoy socializing with opposite gender",
            "7. I notice that I am losing weight.",
            "8. I have trouble with constipation.",
            "9. My heart beats faster than usual.",
            "10. I get tired for no reason.",
            "11. My mind is as clear as it used to be.",
            "12. I find it easy to do the things I used to.",
            "13. I am restless and cant keep still.",
            "14. I feel hopeful about the future.",
            "15. I am more irritable than usual.",
            "16. I find it easy to make decisions.",
            "17. I feel that I am useful and needed.",
            "18. My life is pretty full.",
            "19. I feel that others would be better off if I were dead.",
            "20. I still enjoy the things I used to do."
    };


    @NonNull
    @Override
    public QuestionnaireHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_card, parent, false);


        QuestionnaireAdapter.QuestionnaireHolder vh = new QuestionnaireAdapter.QuestionnaireHolder(itemView);

        return vh;

    }

    public QuestionnaireAdapter() {

        for(int i=0;i<20;i++)
        {
            obj.add(questions[i]);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionnaireHolder holder, int position) {

        holder.question.setText(obj.get(position));
        int radioButtonID = holder.radioGroup.getCheckedRadioButtonId();
        View radioB = holder.radioGroup.findViewById(radioButtonID);
        int rb_num = holder.radioGroup.indexOfChild(radioB);

        /*int pos = position+1;
        1,3,4,7,8,9,10,13,15,19
        if(pos==1 || pos==3 || pos==4 || pos==7 || pos==8 ||pos==9 ||  pos==10 || pos==113 || pos==15 || pos==1)
        {
            values[position] = 10+radioButtonID;
        }
        else
        {
            if(radioButtonID==1)
                values[position] = 10+4;
            else if(radioButtonID==2)
                values[position] = 10+3;
            else if(radioButtonID==3)
                values[position] = 10+2;
            else if(radioButtonID==4)
                values[position] = 10+1;
        }*/

        values[position]=11+rb_num;
    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public int[] getSelectedOptions() {

        return values;
    }

    public class QuestionnaireHolder extends RecyclerView.ViewHolder {

        TextView question;
        RadioGroup radioGroup;

        public QuestionnaireHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            radioGroup = itemView.findViewById(R.id.radioGroup);
        }
    }
}
