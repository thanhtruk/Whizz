package com.quizchic.whizz;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;


public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    int questionIndex = 0;
    int score = 0,scoreOfAnAnswer = 50;
    int correctAnswers = 0, incorrectAnswers = 0, outOfTimeAnswers = 0 ;
    int totalQuestions;

    public static String[][] answersShuffled = new String[10][4];
    TextView questionTextView,questionScore, questionTimer;
    Button ansA_Btn, ansB_Btn, ansC_Btn, ansD_Btn, submitBtn;
    String selectedAnswer, rightAnswer;
    String ans;

    public static ArrayList questions = new ArrayList<Question>();
    ArrayList<String> selectedAns = new ArrayList<String>();

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        questions.clear();
        String json = getJson("question.json");
        convertJsonToQuestions(json);
        totalQuestions = questions.size();


        init();
        loadQuestions(questionIndex);
    }

    public String getJson(String fileName){
        String json = "";
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex){
            Log.e("TAG", "Load json error: " + ex.getMessage());
        }
        return json;
    }
    public void convertJsonToQuestions(String json){

        try {
            JSONArray jsonArr = new JSONArray(json);
            int size = jsonArr.length();
            for (int i = 0; i < size; i++){
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                Question question = new Question();
                question.setQuestion(jsonObject.getString("question"));
                question.setAnswer(jsonObject.getString("answer"));
                question.setOption1(jsonObject.getString("option1"));
                question.setOption2(jsonObject.getString("option2"));
                question.setOption3(jsonObject.getString("option3"));
                questions.add(question);
            }
        } catch (Exception ex){
            Log.e("TAG", "Load json error: " + ex.getMessage());
        }
    }

    public void init(){
        questionScore = findViewById(R.id.question_score);
        questionTextView= findViewById(R.id.question_displayPlace);
        questionTimer = findViewById(R.id.question_timer);
        ansA_Btn= findViewById(R.id.question_answer1);
        ansB_Btn= findViewById(R.id.question_answer2);
        ansC_Btn= findViewById(R.id.question_answer3);
        ansD_Btn= findViewById(R.id.question_answer4);
        submitBtn= findViewById(R.id.question_submit);

        ansA_Btn.setOnClickListener(this);
        ansB_Btn.setOnClickListener(this);
        ansC_Btn.setOnClickListener(this);
        ansD_Btn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

     public void shuffleAns(int index, Question question){
        ArrayList<String> answers = new ArrayList<>();
        answers.add(question.getAnswer());
        answers.add(question.getOption1());
        answers.add(question.getOption2());
        answers.add(question.getOption3());

        Collections.shuffle(answers);
        for(int i = 0; i < 4; i++){
            answersShuffled[index][i] = answers.get(i);
        }
     }

    public void loadQuestions(int index){

        countTimer();
        questionScore.setText("Score:"+score );
        Question question = (Question) questions.get(index);
        rightAnswer = question.getAnswer();
        questionTextView.setText(question.getQuestion());
        shuffleAns(index,question);
        ansA_Btn.setText(answersShuffled[index][0]);
        ansB_Btn.setText(answersShuffled[index][1]);
        ansC_Btn.setText(answersShuffled[index][2]);
        ansD_Btn.setText(answersShuffled[index][3]);
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        ansA_Btn.setBackgroundColor(733757);
        ansB_Btn.setBackgroundColor(733757);
        ansC_Btn.setBackgroundColor(733757);
        ansD_Btn.setBackgroundColor(733757);
        if (clickedButton.getId() == R.id.question_submit){
            selectedAns.add(ans);
            if (selectedAnswer == rightAnswer){
                score+=scoreOfAnAnswer;
                questionScore.setText("Score:"+score );
                correctAnswers++;
            } else{
                if(selectedAnswer != null)
                incorrectAnswers++;
            }
            selectedAnswer = null;
            questionIndex++;
            if (questionIndex >= totalQuestions){
                Intent toFinishQuizActivity = new Intent(QuestionActivity.this,FinishQuizActivity.class);
                toFinishQuizActivity.putStringArrayListExtra("selectedAns",selectedAns);
                toFinishQuizActivity.putExtra("Score",questionScore.getText().toString().trim());
                toFinishQuizActivity.putExtra("incorrectAns",incorrectAnswers);
                toFinishQuizActivity.putExtra("correctAns",correctAnswers);
                toFinishQuizActivity.putExtra("totalQues",totalQuestions);
                toFinishQuizActivity.putExtra("outOfTimeAns",outOfTimeAnswers);
                startActivity(toFinishQuizActivity);
            } else {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                loadQuestions(questionIndex);
            }
        } else{
            clickedButton.setBackgroundColor(Color.WHITE);
            selectedAnswer = clickedButton.getText().toString().trim();
            ans = selectedAnswer;
        }
    }

    public void countTimer(){
        countDownTimer = new CountDownTimer(10000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                questionTimer.setText(String.format("Time: "+"%02d:%02d", minutes,seconds));
            }

            @Override
            public void onFinish() {
                selectedAns.add(ans);
                if (selectedAnswer == rightAnswer){
                    score+=scoreOfAnAnswer;
                    questionScore.setText("Score:"+score );
                    correctAnswers++;
                } else if(selectedAnswer != null){
                        incorrectAnswers++;
                }
                else{
                    outOfTimeAnswers ++;
                }
                selectedAnswer = null;
                questionIndex++;
                if (questionIndex >= totalQuestions){
                    Intent toFinishQuizActivity = new Intent(QuestionActivity.this,FinishQuizActivity.class);
                    toFinishQuizActivity.putStringArrayListExtra("selectedAns",selectedAns);
                    toFinishQuizActivity.putExtra("Score",questionScore.getText().toString().trim());
                    toFinishQuizActivity.putExtra("incorrectAns",incorrectAnswers);
                    toFinishQuizActivity.putExtra("correctAns",correctAnswers);
                    toFinishQuizActivity.putExtra("totalQues",totalQuestions);
                    toFinishQuizActivity.putExtra("outOfTimeAns",outOfTimeAnswers);
                    startActivity(toFinishQuizActivity);
                }
                else{
                    loadQuestions(questionIndex);
                    ansA_Btn.setBackgroundColor(733757);
                    ansB_Btn.setBackgroundColor(733757);
                    ansC_Btn.setBackgroundColor(733757);
                    ansD_Btn.setBackgroundColor(733757);
                }
            }
        };
        countDownTimer.start();
    }


}