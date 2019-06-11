package com.example.asus.appoitment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    Button appointment,schedule;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTitle("Menu");
        appointment = findViewById(R.id.appointment);
        schedule = findViewById(R.id.schedule);
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = "192.168.43.5";
                Intent intent = new Intent(Menu.this,Appointment.class);
                intent.putExtra("ip",ip);
                startActivity(intent);
            }
        });
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ip = "192.168.43.5";
                Intent intent = new Intent(Menu.this,Schedule.class);
                intent.putExtra("ip",ip);
                startActivity(intent);
            }
        });
    }
}
