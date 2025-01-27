package com.quizchic.whizz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.color.utilities.Score;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {
    int questionIndex = 0;

    ArrayList<Question> questions = QuestionActivity.choosenQuestions;
    int totalQuestions = 5;

    TextView questionTextView,questionScore;
    Button ansA_Btn, ansB_Btn, ansC_Btn, ansD_Btn, next_Btn, previous_Btn;
    String rightAnswer;

    int idSelectedBtn;
    Button selectedBtn;
    ArrayList<String> selectedAns;
    String yourScore;

    String[][] answersShuffled = QuestionActivity.answersShuffled;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Intent intent = getIntent();
        selectedAns = intent.getStringArrayListExtra("selectedAns");
        yourScore = intent.getStringExtra("Score");

        init();
        loadQuestions(questionIndex);
        loadCheck();
    }


    public void init(){
        questionScore = findViewById(R.id.question_score);
        questionTextView= findViewById(R.id.question_displayPlace);
        ansA_Btn= findViewById(R.id.question_answer1);
        ansB_Btn= findViewById(R.id.question_answer2);
        ansC_Btn= findViewById(R.id.question_answer3);
        ansD_Btn= findViewById(R.id.question_answer4);
        previous_Btn = findViewById(R.id.question_previous);
        next_Btn = findViewById(R.id.question_next);
        questionScore.setText(yourScore);

        ansA_Btn.setOnClickListener(this);
        ansB_Btn.setOnClickListener(this);
        ansC_Btn.setOnClickListener(this);
        ansD_Btn.setOnClickListener(this);
        previous_Btn.setOnClickListener(this);
        next_Btn.setOnClickListener(this);
    }

    public void loadQuestions(int index){
        ansA_Btn.setVisibility(View.VISIBLE);
        ansB_Btn.setVisibility(View.VISIBLE);
        ansC_Btn.setVisibility(View.VISIBLE);
        ansD_Btn.setVisibility(View.VISIBLE);
        if (questionIndex == 0) {
            previous_Btn.setEnabled(false);
            previous_Btn.setDrawingCacheBackgroundColor(Color.GRAY);
        } else if (questionIndex == 1) {
            previous_Btn.setEnabled(true);
            previous_Btn.setDrawingCacheBackgroundColor(733757);
        }
        Question question = questions.get(index);
        rightAnswer = question.getAnswer();
        questionTextView.setText((index +1) + "." + question.getQuestion());
        ansA_Btn.setText(answersShuffled[index][0]);
        if(ansA_Btn.getText().toString().equals("TFNULL")){
            ansA_Btn.setVisibility(View.INVISIBLE);
        }

        ansB_Btn.setText(answersShuffled[index][1]);
        if(ansB_Btn.getText().toString().equals("TFNULL")){
            ansB_Btn.setVisibility(View.INVISIBLE);
        }

        ansC_Btn.setText(answersShuffled[index][2]);
        if(ansC_Btn.getText().toString().equals("TFNULL")){
            ansC_Btn.setVisibility(View.INVISIBLE);
        }

        ansD_Btn.setText(answersShuffled[index][3]);
        if(ansD_Btn.getText().toString().equals("TFNULL")){
            ansD_Btn.setVisibility(View.INVISIBLE);
        }
    }

    public void warningReviewLimit(){
        new AlertDialog.Builder(this)
                .setMessage("can't go anymore")
                .setPositiveButton("Restar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent toQuestionActivity = new Intent(ReviewActivity.this,QuestionActivity.class);
//                        QuestionActivity.score = 0;
//                        QuestionActivity.questionIndex = 0;
                        startActivity(toQuestionActivity);
                    }
                })
                .setNegativeButton("Continue",((dialogInterface, i) -> review()))
                .show();
    }

    public void review(){
        loadCheck();
    }
    public void loadCheck(){
        if(checkSelectedAnsver() != 0){
            selectedBtn = findViewById(checkSelectedAnsver());
            if(selectedBtn.getText().equals(rightAnswer)){
                selectedBtn.setBackgroundColor(Color.GREEN);
            } else {
                selectedBtn.setBackgroundColor(Color.RED);
                checkRightAnswer();
            }
        }else{
            checkRightAnswer();
        }
    }

    public int checkSelectedAnsver(){
        idSelectedBtn = 0;
        if (ansA_Btn.getText().equals(selectedAns.get(questionIndex)))
            idSelectedBtn = ansA_Btn.getId();
        if (ansB_Btn.getText().equals(selectedAns.get(questionIndex)))
            idSelectedBtn = ansB_Btn.getId();
        if (ansC_Btn.getText().equals(selectedAns.get(questionIndex)))
            idSelectedBtn = ansC_Btn.getId();
        if (ansD_Btn.getText().equals(selectedAns.get(questionIndex)))
            idSelectedBtn = ansD_Btn.getId();
        return idSelectedBtn;
    }

    public void checkRightAnswer(){
        if (ansA_Btn.getText() == rightAnswer)
            ansA_Btn.setBackgroundColor(Color.GREEN);
        if (ansB_Btn.getText() == rightAnswer)
            ansB_Btn.setBackgroundColor(Color.GREEN);
        if (ansC_Btn.getText() == rightAnswer)
            ansC_Btn.setBackgroundColor(Color.GREEN);
        if (ansD_Btn.getText() == rightAnswer)
            ansD_Btn.setBackgroundColor(Color.GREEN);
    }

    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        ansA_Btn.setBackgroundColor(733757);
        ansB_Btn.setBackgroundColor(733757);
        ansC_Btn.setBackgroundColor(733757);
        ansD_Btn.setBackgroundColor(733757);
        if(clickedButton.getId() == R.id.question_next){
            if(questionIndex < totalQuestions-1){
                questionIndex++;
                loadQuestions(questionIndex);
                loadCheck();
            }else{
                warningReviewLimit();
            }
        } else if (clickedButton.getId() == R.id.question_previous) {
            if(questionIndex>0){
                questionIndex --;
                loadQuestions(questionIndex);
                loadCheck();
                }
            }
        }
    }
