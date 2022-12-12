package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarActivity extends AppCompatActivity {

  private String[] savedCalendar = new String[35]; // 저장된 날짜
  GregorianCalendar calendar; // 현재 날짜

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_calendar);

    calendar = new GregorianCalendar();
    CalendarSetting(calendar); // 현재 날짜로 캘린더 세팅

    ImageButton previousButton, nextButton;

    previousButton = findViewById(R.id.previousButton);
    nextButton = findViewById(R.id.nextButton);

    // 이벤트 리스너 설정
    previousButton.setOnClickListener(preBtnClickEvent);
    nextButton.setOnClickListener(nextBtnClickEvent);

    ImageButton searchBtn = (ImageButton) findViewById(R.id.floatingActionButton);
    searchBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
        Intent intent = new Intent(getApplicationContext(), TodoSearchActivity.class);
        startActivity(intent);
      }
    });
  }

  // 이전 버튼 눌렀을 때 이벤트
  View.OnClickListener preBtnClickEvent = new View.OnClickListener(){
    @Override
    public void onClick(View v){
      calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)-1,1);
      CalendarSetting(calendar);

      TextView textView = findViewById(R.id.textView);
      textView.setText(calendar.get(Calendar.YEAR)+"년 "+(calendar.get(Calendar.MONTH)+1)+"월");
    }
  };

  // 다음 버튼 눌렀을 때 이벤트
  View.OnClickListener nextBtnClickEvent = new View.OnClickListener(){
    @Override
    public void onClick(View v){
      calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) +1,1);
      CalendarSetting(calendar);

      TextView textView = findViewById(R.id.textView);
      textView.setText(calendar.get(Calendar.YEAR)+"년 "+(calendar.get(Calendar.MONTH)+1)+"월");
    }
  };

  // 캘린더 세팅 함수
  protected void CalendarSetting(GregorianCalendar calendar){
    // 현재 달의 1일
    GregorianCalendar cal = new GregorianCalendar(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),1,0,0,0);
    // 저번 달의 1일
    GregorianCalendar prev_cal = new GregorianCalendar(calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH)-1,1,0,0,0);

    int week = cal.get(Calendar.DAY_OF_WEEK) -1;
    int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH) -1;

    for(int i =0; i<savedCalendar.length;i++){
      if(i<week){
        savedCalendar[i]=Integer.toString(prev_cal.getActualMaximum(Calendar.DAY_OF_MONTH)-week+i+1);
      }
      else if (i>(max+week)){
        savedCalendar[i]=Integer.toString(i-(max+week));
      }
      else{
        savedCalendar[i] = Integer.toString(i-week+1);
      }
    }
    RecyclerViewCreate(week, max, cal);

    TextView textView = findViewById(R.id.textView);
    textView.setText(calendar.get(Calendar.YEAR)+"년 "+(calendar.get(Calendar.MONTH)+1)+"월");
  }

  // 리사이클러뷰 생성 함수
  protected void RecyclerViewCreate(int week, int max, GregorianCalendar cal){
    RecyclerView calendarView = findViewById(R.id.recyclerView);
    CalendarRecyclerAdapter calendarAdapter = new CalendarRecyclerAdapter(getApplicationContext(), savedCalendar, week, max, cal);

    // 그리드 뷰 이용
    GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),7);
    calendarView.setLayoutManager(layoutManager);
    calendarView.setAdapter(calendarAdapter);
    calendarView.setFocusable(false);
  }

}