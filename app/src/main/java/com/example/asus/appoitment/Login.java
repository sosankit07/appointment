package com.example.asus.appoitment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Login extends AppCompatActivity {
    Button login;
    TextView deviceinfo,datetime;
    Calendar calendar;
    SimpleDateFormat simpledateformat;
    String Date;
    EditText user,pass;
    String USER, PASS;
    private String Epass;
    String pass_retrieve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        login = findViewById(R.id.login_button);
        user = findViewById(R.id.login_username);
        pass = findViewById(R.id.login_pass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Menu.class);
                startActivity(intent);
            }
        });
    }
}
