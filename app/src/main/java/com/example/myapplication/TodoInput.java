package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class TodoInput extends AppCompatActivity  {

    EditText editText;
    EditText editText1;

    SQLiteDatabase database;
    TextView textView,textView2;

    private int year;
    private int month;
    private int days;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_addtodo);

        editText = findViewById(R.id.editText);
        editText1=findViewById(R.id.memo);

        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);


        openDatabase();
        createTable();

        year=getIntent().getIntExtra("Year",1);
        month=getIntent().getIntExtra("Month",1);
        days=getIntent().getIntExtra("Day",1);

        textView.setText(year+"년 "+month+"월 "+days+"일");

        // 저장 버튼
        Button updatebutton = findViewById(R.id.update);
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String todo = editText.getText().toString().trim();
                String memo= editText1.getText().toString().trim();

                // 일정 입력 안하면 저장 x
                if(todo.equals("")){
                    Toast.makeText(getApplicationContext(), "일정을 입력하세요.",Toast.LENGTH_SHORT).show();
                } // 일정 입력하면 저장 o
                else{
                    insertData(todo,memo,year,month,days);
                    finish();
                }
            }
        });

        // 뒤로 가기 버튼
        ImageButton cancelButton = (ImageButton) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        // 글자 수 세기
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = editText1.getText().toString();
                textView2.setText(input.length()+" / 90");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    // 데이터베이스 여는 함수
    public void openDatabase(){
        database = openOrCreateDatabase("diary_database", MODE_PRIVATE,null) ;
    }

    // 테이블 생성 함수
    public void createTable(){
        if(database!= null) {
            String sql = "create table if not exists " + "todo_table" + "(_id integer PRIMARY KEY autoincrement, todolist text, memo text, year int, month int, day int)";
            database.execSQL(sql);
        }
    }

    // 데이터 insert 함수
    public void insertData(String todolist, String memo, int year, int month, int day){
        if(database != null){
            String sql = "insert into todo_table(todolist, memo, year, month, day, done) values(?, ?, ?, ?, ?, ?)";
            Object[] params = {todolist, memo, year, month, day};
            database.execSQL(sql, params);
        }
    }

}