package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PhoneBookInputActivity extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook_input);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);

        // 데이터베이스 만들기
        database = openOrCreateDatabase("diary_database", MODE_PRIVATE,null) ;

        // 테이블 만들기
        String sql = "create table if not exists " + "phonebook" + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
        database.execSQL(sql); //sql문 실행

        // 저장 버튼 눌렀을 때
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText1.getText().toString().trim();
                String ageStr = editText2.getText().toString().trim();
                String mobile = editText3.getText().toString().trim();

                int age = -1;
                try{
                    age = Integer.parseInt(ageStr);
                }catch (Exception e){}

                if(name.equals("")||ageStr.equals("")||mobile.equals("")){
                    Toast.makeText(getApplicationContext(), "입력이 완료되지 않은 항목이 있습니다.",Toast.LENGTH_SHORT).show();
                }
                else{ // 모든 항목을 입력해야 insert
                    insertData(name, age, mobile);
                    finish();
                    Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // 데이터 insert 함수
    public void insertData(String name, int age, String mobile){
            String sql = "insert into phonebook(name, age, mobile) values(?, ?, ?)";
            Object[] params = {name, age, mobile};
            database.execSQL(sql, params);
    }

    public void onBackButtonClicked(View v){
        finish();
    }

}