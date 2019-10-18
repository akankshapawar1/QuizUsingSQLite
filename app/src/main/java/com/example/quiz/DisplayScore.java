package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.media.MediaPlayer;
import android.widget.Toast;

public class DisplayScore extends AppCompatActivity {

/*  public String strUser;
    public String name;
    public String email;
    public String password;*/
    public int number;
    private long backPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        super.onCreate(savedInstanceState);
        Button button = findViewById(R.id.button);
        setContentView(R.layout.activity_display_score);
        setTitle("Score");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        /*
        //Username & password from main activity:
        strUser = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");
        //email & name from signup activity:
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        */

        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.fakeapplause);
        MediaPlayer mplayer = MediaPlayer.create(this,R.raw.bigclaps);
        MediaPlayer mp = MediaPlayer.create(this,R.raw.sadsad);

        EditText score = (EditText) findViewById(R.id.score2);
        Intent intent = getIntent();
        number = intent.getIntExtra(Questions.QUIZ_SCORE,0);

        if (number >= 24 ){
            mplayer.start();
        }else if(number > 5 && number <= 23){
            mediaPlayer.start();
        }else
        {
            mp.start();
        }

        score.setText("    "+number);

        /*MyApplication app = (MyApplication)getApplicationContext();
        app.setScore(number);
        contact cc = new contact();
        cc.setScore(number);
        cc.setUsername(strUser);
        //String name = app.getUsername();
        databaseHelper.addScore(name,number);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        */

        /*Boolean b;
        contact cc = new contact();
        cc.setScore(number);
        cc.setUsername(strUser);
        cc.setPass(password);
        cc.setEmail(email);
        cc.setName(name);
        b = databaseHelper.addScore(cc);
        if(b==true){
            Toast.makeText(this, "Upadated", Toast.LENGTH_SHORT).show();
        }else{Toast.makeText(this, "not upadated", Toast.LENGTH_SHORT).show();}*/

    }


    public void goBack(View view){
        if(view.getId() == R.id.button){
        Intent intent = new Intent(DisplayScore.this,Login.class);
        startActivity(intent);
        }

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
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent);
                break;
            case R.id.about:
                Intent intent2 = new Intent(this,AboutUs.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent2);
                break;
            case R.id.study:
                Intent intent3 = new Intent(this,StudyMaterial.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(intent3);
                break;
        }

        return true;
    }
    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();

    }
}
