package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class StudyMaterial extends AppCompatActivity {

    private long backPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_material);
        getSupportActionBar().setTitle("Useful Links");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        TextView textView = findViewById(R.id.textView4);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView2 = findViewById(R.id.textView5);
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        TextView textView3 = findViewById(R.id.textView6);
        textView3.setMovementMethod(LinkMovementMethod.getInstance());
    }
    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            Intent intent = new Intent(this,Login.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Press back again to exit!", Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();

    }
}
