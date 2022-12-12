package com.example.myapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TodoShowDetails extends AppCompatActivity {
    EditText todoText,memoText;
    TextView textView, textView2;
    int ID;
    String memo;
    String todo;
    int year;
    int month;
    int day;

    SQLiteDatabase database;
    String databaseName = "diary_database";
    String tableName="todo_table";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_todo_detail);

        database =  openOrCreateDatabase(databaseName, MODE_PRIVATE,null) ;
        String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, todolist text, memo text, year int, month int, day int)";
        database.execSQL(sql);

        ID = getIntent().getIntExtra("ID : ",1);
        memo = getIntent().getStringExtra("memo : ");
        todo = getIntent().getStringExtra("todo : ");
        year = getIntent().getIntExtra("year : ",1);
        month = getIntent().getIntExtra("month : ",1);
        day = getIntent().getIntExtra("day : ",1);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(year+"년 "+month+"월 "+day+"일");

        todoText=findViewById(R.id.editText);
        memoText=findViewById(R.id.memo);
        textView2=findViewById(R.id.textView2);

        todoText.setText(todo);
        memoText.setText(memo);

        String input = memoText.getText().toString();
        textView2.setText(input.length()+" / 90");

        // 수정 버튼 클릭했을 때
        Button updateBTN = (Button) findViewById(R.id.update);
        updateBTN.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(TodoShowDetails.this);
                dialog.setTitle("");
                dialog.setMessage("수정하시겠습니까?");
                dialog.setCancelable(false);

                dialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // 일정 입력 안하면 수정 x
                        if(todoText.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "일정을 입력하세요.",Toast.LENGTH_SHORT).show();
                        } // 일정 입력하면 수정 o
                        else{
                            ContentValues recordValues = new ContentValues();
                            //recordValues.put("year", Integer.parseInt(yearText.getText().toString()));
                            //recordValues.put("month", Integer.parseInt(monthText.getText().toString()));
                            //recordValues.put("day", Integer.parseInt(dayText.getText().toString()));
                            recordValues.put("todolist",todoText.getText().toString());
                            recordValues.put("memo",memoText.getText().toString());
                            database.update(tableName, recordValues,"_id=?", new String[]{Integer.toString(ID)});

                            dialogInterface.dismiss();
                            finish();
                        }
                    }
                });

                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

        // 돌아가기 버튼 클릭했을 때
        ImageButton backBTN = (ImageButton) findViewById(R.id.back);
        backBTN.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        memoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String input = memoText.getText().toString();
                textView2.setText(input.length()+" / 90");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}
