package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TodoSearchActivity extends AppCompatActivity {
    EditText editText;
    SQLiteDatabase database;
    ListView listView;
    SingerAdapter adapter; // 내부 클래스
    String tableName = "todo_table";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_search);

        database = openOrCreateDatabase("diary_database", MODE_PRIVATE, null);

        // 테이블 오픈
        String sql = "create table if not exists " + "todo_table" + "(_id integer PRIMARY KEY autoincrement, todolist text, memo text, year int, month int, day int)";
        database.execSQL(sql);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new SingerAdapter();
        listView.setAdapter(adapter);
        listView.setFocusable(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        database = openOrCreateDatabase("diary_database", MODE_PRIVATE, null);

        // 테이블 오픈
        String sql = "create table if not exists " + "todo_table" + "(_id integer PRIMARY KEY autoincrement, todolist text, memo text, year int, month int, day int)";
        database.execSQL(sql);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new SingerAdapter();
        listView.setAdapter(adapter);
        listView.setFocusable(false);

        if(database != null){
            // 다 띄우기
            String sql2 = "select * from "+tableName+" order by year, month, day";
            Cursor cursor = database.rawQuery(sql2, null);
            // getCount=선택된 레코드의 개수
            for( int i = 0; i< cursor.getCount(); i++){
                cursor.moveToNext();//다음 레코드로 넘어간다. 첫번째 레코드의 앞을 가리키고 있으므로 ToNext()해야지 해당 레코드를 가리킴
                // select하면 3개의 속성이 지금 들어오는데, getString의 인수에는 어떤 속성으로 가져올 것인지
                /* 중요할지도.... *******************/
                String todo = cursor.getString(1);
                String memo = cursor.getString(2);
                int id = cursor.getInt(0);
                int year = cursor.getInt(3);
                int month = cursor.getInt(4);
                int day = cursor.getInt(5);
                adapter.addItem(new todolistItem(todo, memo, id, year, month, day));
            }
            cursor.close();
        }

        listView.setAdapter(adapter);
        listView.setFocusable(false);

        editText = (EditText) findViewById(R.id.editText);

        // 생세페이지
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                todolistItem item = (todolistItem) adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), TodoShowDetails.class);
                intent.putExtra("ID : ", item.getId());
                intent.putExtra("year : ",item.getYear());
                intent.putExtra("month : ",item.getMonth());
                intent.putExtra("day : ",item.getDay());
                intent.putExtra("todo : ", item.gettodolist());
                intent.putExtra("memo : ", item.getmemo());
                startActivity(intent);
            }
        });

        // 검색
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setQueryHint("일정 검색");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.clearItem();

                String sql3 = "select * from "+tableName +" where todolist Like "+ "'"+ '%' + s + '%' +  "'"+"order by year, month, day";
                Cursor cursor = database.rawQuery(sql3, null); //레코드셋 전체 집합을 저장

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext(); //다음 레코드로 넘어간다. cursor는 첫 번째 레코드의 그 전 레코드를 항상 가르킨다. 그래서 넘겨주는고임!
                    String todo = cursor.getString(1);
                    String memo = cursor.getString(2);
                    int id = cursor.getInt(0);
                    int year = cursor.getInt(3);
                    int month = cursor.getInt(4);
                    int day = cursor.getInt(5);
                    adapter.addItem(new todolistItem(todo, memo, id, year, month, day));
                    adapter.notifyDataSetChanged();
                }
                cursor.close(); //끝나면 커서 닫기

                return false;
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.HomeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }




    // 아답터
    public class SingerAdapter extends BaseAdapter {
        ArrayList<todolistItem> items = new ArrayList<todolistItem>();

        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(todolistItem item) {
            items.add(item);
        }

        @Override
        // 몇번 째 아이템 리턴
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        // 특정 포지션 리턴
        public long getItemId(int position) {
            return position;
        }

        public void clearItem(){
            items.clear();
        }

        @Override
        /* View를 만들어서 리턴해줌 getView-> 실제 view가 생성이 됨 !!!**/
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            todolistItemView view = new todolistItemView(getApplicationContext());

            todolistItem item = items.get(position);
            view.settodolist(item.gettodolist());
            view.setday(item.getYear(), item.getMonth(), item.getDay());

            // 삭제하기
            Button deleteImageView = (Button) view.findViewById(R.id.checkBox2);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(TodoSearchActivity.this);
                    dialog.setTitle(item.gettodolist());
                    dialog.setMessage("해당 일정을 삭제하시겠습니까?");
                    dialog.setCancelable(false);


                    dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            database.delete(tableName,"_id=?",new String[]{Integer.toString(item.getId())});
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

            return view;
        }

    }

}