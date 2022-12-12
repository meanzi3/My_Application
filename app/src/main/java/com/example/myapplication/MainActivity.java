package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // 주소록 관리로 이동
    ViewGroup layout = (ViewGroup) findViewById(R.id.addressLayout);
    layout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), PhoneBookShowActivity.class);
        startActivity(intent);
      }
    });

    // 일정 관리로 이동
    ViewGroup layout2 = (ViewGroup) findViewById(R.id.scheduleLayout);
    layout2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
      }
    });
  }

}