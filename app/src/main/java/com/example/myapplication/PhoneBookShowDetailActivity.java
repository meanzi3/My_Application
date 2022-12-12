package com.example.myapplication;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PhoneBookShowDetailActivity extends AppCompatActivity {
    EditText nameText, mobileText, ageText;
    int Id;
    String Name;
    String Mobile;
    int Age;

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook_show_detail);

        // 데이터베이스 오픈
        database = openOrCreateDatabase("diary_database", MODE_PRIVATE,null) ;

        // 테이블 오픈
        String sql = "create table if not exists " + "phonebook" + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
        database.execSQL(sql); //sql문 실행

        nameText = findViewById(R.id.editText);
        mobileText = findViewById(R.id.editText2);
        ageText = findViewById(R.id.editText3);

        Id = getIntent().getIntExtra("ID",1);
        Name = getIntent().getStringExtra("Name");
        Mobile = getIntent().getStringExtra("Mobile");
        Age = getIntent().getIntExtra("Age",1);

        nameText.setText(Name);
        mobileText.setText(Mobile);
        ageText.setText(String.valueOf(Age));

        Button msgButton = findViewById(R.id.msgButton);
        msgButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), Name+"에게 메시지 보내기.", Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:"+Mobile));
                startActivity(myIntent);
            }
        });

        ImageButton previousButton = findViewById(R.id.previousButton);
        previousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(), Name+"에게 전화걸기.", Toast.LENGTH_LONG).show();
                Intent myIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+Mobile));
                startActivity(myIntent2);
            }
        });

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // 내용이 수정되면 DB update
                if((nameText.getText().toString() != Name) || (mobileText.getText().toString() != Mobile) || (ageText.getText().toString() != String.valueOf(Age))) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(PhoneBookShowDetailActivity.this);
                    dialog.setTitle("");
                    dialog.setMessage("해당 연락처를 수정하시겠습니까?");
                    dialog.setCancelable(false);

                    dialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            // 입력 안 된 항목 있으면 수정 x
                            if(nameText.getText().toString().equals("")||mobileText.getText().toString().equals("")||ageText.getText().toString().equals("")){
                                Toast.makeText(getApplicationContext(), "입력이 완료되지 않은 항목이 있습니다.",Toast.LENGTH_SHORT).show();
                            }
                            else { // 다 입력 되어있으면 수정 d
                                ContentValues recordValues = new ContentValues();
                                recordValues.put("name", nameText.getText().toString());
                                recordValues.put("mobile", mobileText.getText().toString());
                                recordValues.put("age",Integer.parseInt(ageText.getText().toString()));
                                database.update("phonebook", recordValues,"_id=?", new String[]{Integer.toString(Id)});

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
            }
        });

        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialog = new AlertDialog.Builder(PhoneBookShowDetailActivity.this);
                dialog.setTitle(Name);
                dialog.setMessage("해당 연락처를 삭제하시겠습니까?");
                dialog.setCancelable(false);

                dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.delete("phonebook","_id=?",new String[]{Integer.toString(Id)});
                        dialogInterface.dismiss();
                        finish();

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
    }
}