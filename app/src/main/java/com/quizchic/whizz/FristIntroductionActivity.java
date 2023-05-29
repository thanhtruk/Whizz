package com.quizchic.whizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FristIntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frist_introduction);

        Button question = (Button) findViewById(R.id.startButton);
        Button back = (Button) findViewById(R.id.previous);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toQuestionActivity = new Intent(FristIntroductionActivity.this, QuestionActivity.class);
                startActivity(toQuestionActivity);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toHomeActivity = new Intent(FristIntroductionActivity.this, HomeActivity.class);
                startActivity(toHomeActivity);
            }
        });
    }
}