package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    DatabaseHelper helper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("SignUp");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void onSignUpClick(View view){
        EditText name = findViewById(R.id.tvname);
        EditText email = findViewById(R.id.tvemail);
        EditText pass1 = findViewById(R.id.pass1);
        EditText pass2 = findViewById(R.id.pass2);
        EditText username = findViewById(R.id.username);

        String strname = name.getText().toString();
        boolean val1;
        val1 = strname.matches("^[a-zA-Z_ ]*$");
        //Matcher matcher = Pattern.compile("[A-Z][a-z]*").matcher(strname); above is the same
        if(strname.isEmpty()){
            name.setError("Name cannot be empty!");
        }
        if(val1==false){
            name.setError("Enter valid name");
        }

        String stremail = email.getText().toString();
        boolean val3 =Patterns.EMAIL_ADDRESS.matcher(stremail).matches();
        if(stremail.isEmpty()){
            email.setError("Email cannot be empty!");
        }else if(val3 == false){
            email.setError("Enter valid email address!");
        }

        String strpass1 = pass1.getText().toString();
        boolean val4 = PASSWORD_PATTERN.matcher(strpass1).matches();
        if(strpass1.isEmpty()){
            pass1.setError("Password cannot be empty!");
        }else if(val4 == false){
            pass1.setError("Weak password!Should contain at least 6 characters");
        }
        String strpass2 = pass2.getText().toString();

        String strusername = username.getText().toString();
        boolean val2;
        val2 = strusername.matches("^[a-zA-Z0-9]*$");
        if(strusername.isEmpty()){
            username.setError("Username cannot be empty!");}
        if(val2==false){
            username.setError("Username should not contain spaces or special characters!");
        }
        Boolean chkuser;
        chkuser = helper.checkUsername(strusername);
        if (chkuser == false){
        username.setError("Username already exists!");
        }


        if(!strpass1.equals(strpass2)){
            pass2.setError("Password does not match");

            //Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_LONG).show();
        }
        else if(val1 == true && val2 == true && val3 == true && val4 == true && chkuser ==true)
        {
            contact c = new contact();
            c.setName(strname);
            c.setEmail(stremail);
            c.setPass(strpass1);
            c.setUsername(strusername);
            c.setScore(0);
            helper.insertContact(c);
            Toast.makeText(this, "SignUp successful!", Toast.LENGTH_LONG).show();
            //Intent z = new Intent(this,DisplayScore.class);
            //z.putExtra("name",strname);
            //z.putExtra("email",stremail);
            //Intent i = new Intent(signup.this, MainActivity.class);
            //startActivity(i);

        }

    }

    @Override
    public void finish() {
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        super.finish();

    }
}
