package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class Login extends AppCompatActivity {
    public static final String SHARED_PREFERENCES = "sharedScore";
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";

    public static final String KEY_HIGHSCORE = "keyHighscore";
    private TextView textViewHighscore;
    public int highScore;
    private Spinner spinnerDifficulty;
    private Spinner spinnerCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        String str = getIntent().getStringExtra("username");
        TextView tv = findViewById(R.id.textView2);
        tv.setText(str);

        textViewHighscore = findViewById(R.id.highscore);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        loadHighscore();
        loadDifficultyLevels();
        loadCategories();

        Button buttonStartQuiz = findViewById(R.id.start);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
    });
    }

    private void startQuiz(){
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();
        /*Intent z = new Intent(Login.this,DisplayScore.class);
        z.putExtra("categoryName",categoryName);*/
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        Intent intent = new Intent(Login.this,Questions.class);
        intent.putExtra(EXTRA_CATEGORY_ID,categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME,categoryName);
        intent.putExtra(EXTRA_DIFFICULTY,difficulty);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_QUIZ){
            if (resultCode == RESULT_OK){
                int score = data.getIntExtra(Questions.RETURN_SCORE, 0);
                if (score > highScore)
                {
                    updateHighscore(score);
                }
            }
        }
    }




    private void loadHighscore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES,MODE_PRIVATE);
        highScore = prefs.getInt(KEY_HIGHSCORE, 0);
        textViewHighscore.setText("Highscore: "+highScore);
    }

    private void updateHighscore(int highscoreNew){
        highScore = highscoreNew;
        textViewHighscore.setText("Highscore: "+highScore);
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_HIGHSCORE,highScore);
        editor.apply();
    }

    private void loadCategories(){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);
        List<Category> categories = databaseHelper.getAllCategories();
        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,
                categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }
    private void loadDifficultyLevels(){
        String[] difficultyLevels = QuestionAnswers.getAllDifficultyLevel();
        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }



    @Override
    public void finish() {
        super.finish();

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.about:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Intent intent2 = new Intent(this,AboutUs.class);
                startActivity(intent2);
                break;
            case R.id.study:
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                Intent intent3 = new Intent(this,StudyMaterial.class);
                startActivity(intent3);
                break;
        }

        return true;
    }
}
