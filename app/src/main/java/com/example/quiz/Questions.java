package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Questions extends AppCompatActivity {
    public static final String RETURN_SCORE = "returnScore";
    public static final String QUIZ_SCORE = "quizScore";
    private static final long COUNTDOWN_MILLI = 30000;
    SharedPreferences sharedP;

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewDifficulty;
    private TextView textViewCategory;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    public Button buttonConfirmNext;

    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    public long timeLeft;

    private List<QuestionAnswers> questionList;
    private int questionCounter;
    private long backPressed;
    private int questionCountTotal;
    private QuestionAnswers currentQuestion;
    public int score;
    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setTitle("Quiz");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        textViewQuestion = findViewById(R.id.question);
        textViewScore = findViewById(R.id.score);
        textViewQuestionCount = findViewById(R.id.questionCount);
        textViewCountDown = findViewById(R.id.timer);
        textViewDifficulty = findViewById(R.id.difficultyLevel);
        textViewCategory = findViewById(R.id.category);
        rbGroup = findViewById(R.id.radioGroup);
        rb1 = findViewById(R.id.RadioButton1);
        rb2 = findViewById(R.id.RadioButton2);
        rb3 = findViewById(R.id.RadioButton3);
        rb4 = findViewById(R.id.RadioButton4);
        buttonConfirmNext = findViewById(R.id.confirmNext);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(Login.EXTRA_CATEGORY_ID, 0);
        String categoryName = intent.getStringExtra(Login.EXTRA_CATEGORY_NAME);
        String difficulty = intent.getStringExtra(Login.EXTRA_DIFFICULTY);

        textViewCategory.setText("Category: " + categoryName);
        textViewDifficulty.setText("Difficulty: " + difficulty);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        questionList = dbHelper.getQuestions(categoryID, difficulty);
        questionCountTotal = questionList.size();
        Collections.shuffle(questionList);

        showNextQuestion();

        // Once a radio button is checked, it cannot be marked as unchecked by user.
        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(Questions.this, "Select an answer!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);//currentQuestion is object of QuestionAnswers
            textViewQuestion.setText(currentQuestion.getQuestions());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());
            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("confirm");
            timeLeft = COUNTDOWN_MILLI;
            startCountDown();
        } else {
            sendResult();
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long l) {//l=milliseconds left(byDefault)
                timeLeft = l;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateCountdownText();
                checkAnswer();
            }
        }.start();
    }

    public void updateCountdownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);
        if (timeLeft < 10000) {
            textViewCountDown.setTextColor(Color.RED);
            Toast.makeText(this, "Select an answer or you will be disqualified!", Toast.LENGTH_SHORT).show();
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }

    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        if (answerNr == currentQuestion.getAnswerNr()) {
            //score++;
            //textViewScore.setText("Score: "+score);
            if(timeLeft >20000 && timeLeft <= 30000){
                score = score + 4;
            }else if(timeLeft >=10000 && timeLeft <20000){
                score = score + 2;
            }else{
                score++;
            }
        }
        showSolution();
    }

    private void showSolution() {
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        rbSelected.setTextColor(Color.RED);
        /*
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);*/
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                break;
        }
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");
        } else {
            buttonConfirmNext.setText("Finish");
        }
    }

    public void sendResult(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(RETURN_SCORE, score);
        setResult(RESULT_OK, resultIntent);
    }

    public void finishQuiz() {
        /*Intent resultIntent = new Intent();
        resultIntent.putExtra(RETURN_SCORE, score);
        setResult(RESULT_OK, resultIntent);*/
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        Intent intent = new Intent(Questions.this, DisplayScore.class);
        intent.putExtra(QUIZ_SCORE, score);
        startActivity(intent);

        finish();
    }

    @Override
    public void onBackPressed() {

        if (backPressed + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
    }
}
