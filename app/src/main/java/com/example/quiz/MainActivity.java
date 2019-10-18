package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper helper = new DatabaseHelper(this);
    private SharedPreferences shared ;
    private String prefName = "userLoginInformation";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shared = getSharedPreferences(prefName,MODE_PRIVATE);
        getPreferencesData();
    }

    public void getPreferencesData(){
        EditText a = (EditText) findViewById(R.id.tvuser);
        EditText b = (EditText) findViewById(R.id.tvpass);
        CheckBox check = findViewById(R.id.checkBox);
        SharedPreferences sp = getSharedPreferences(prefName,MODE_PRIVATE);
        if(sp.contains("username")){
            String u = sp.getString("username","not found");
            a.setText(u.toString());
        }if(sp.contains("password")){
            String p = sp.getString("password","not found");
            b.setText(p.toString());
        }if(sp.contains("isChecked")){
            Boolean i = sp.getBoolean("isChecked",false);
            check.setChecked(i);
        }
    }


    public void onButtonClick(View view){
        if(view.getId()==R.id.signup)
        {
            Intent i = new Intent(MainActivity.this,signup.class);
            startActivity(i);
        }
        if (view.getId() == R.id.login) {
            EditText a = (EditText) findViewById(R.id.tvuser);
            String str = a.getText().toString();
            EditText b = (EditText) findViewById(R.id.tvpass);
            String pass = b.getText().toString();
            CheckBox check = findViewById(R.id.checkBox);
            if (str.isEmpty() || pass.isEmpty()) {
                a.setError("Enter username");
                b.setError("Enter  password");
            } else {
                String password = helper.searchPass(str);
                if (pass.equals(password)) {
                   /*MyApplication app =(MyApplication)getApplicationContext();
                   app.setUsername(str);*/
                    Intent j = new Intent(MainActivity.this, Login.class);
                    //Intent z = new Intent(MainActivity.this, DisplayScore.class);
                    j.putExtra("username", str);
                    //z.putExtra("userName", str);
                    //z.putExtra("password",pass);
                    if(check.isChecked()){
                        Boolean isChecked = check.isChecked();
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putString("username",a.getText().toString());
                        editor.putString("password",b.getText().toString());
                        editor.putBoolean("isChecked",isChecked);
                        editor.apply();
                    }else {
                        shared.edit().clear().apply();
                    }
                    a.getText().clear();
                    b.getText().clear();
                    startActivity(j);
                } else {
                    Toast.makeText(this, "username and password do not match!", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
