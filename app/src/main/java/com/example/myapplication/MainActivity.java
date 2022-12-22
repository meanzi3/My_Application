package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
  SQLiteDatabase database;
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

    database = openOrCreateDatabase("diary_database", MODE_PRIVATE,null) ;

    // 테이블 만들기
    String sql = "create table if not exists " + "phonebook" + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
    database.execSQL(sql); //sql문 실행
    String sql2 = "create table if not exists " + "todo_table" + "(_id integer PRIMARY KEY autoincrement, todolist text, memo text, year int, month int, day int)";
    database.execSQL(sql2);

    // 초기데이터셋 주소록 2개
    database.execSQL("INSERT OR IGNORE INTO phonebook (_id, name, age, mobile) VALUES (1, 'minji', 22,'01012345678')");
    database.execSQL("INSERT OR IGNORE INTO phonebook (_id, name, age, mobile) VALUES (2, 'sumin', 22,'01087654321')");
    // 초기데이터셋 일정 2개
    database.execSQL("INSERT OR IGNORE INTO todo_table (_id, todolist, memo, year, month, day) VALUES (1,'Christmas Party♥','Christsmas party with friends ~!~!', 2022, 12, 25)");
    database.execSQL("INSERT OR IGNORE INTO todo_table (_id, todolist, memo, year, month, day) VALUES (2,'Final Project deadline','submit [mobile programming] final project!', 2022, 12, 22)");
  }

}