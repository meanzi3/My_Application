package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookShowActivity extends AppCompatActivity {
    EditText editText;
    SQLiteDatabase database;
    ListView listView;
    ShowAdapter adapter; // 내부 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook_show);

        database = openOrCreateDatabase("diary_database", MODE_PRIVATE, null);

        // 테이블 오픈
        String sql = "create table if not exists " + "phonebook" + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
        database.execSQL(sql); //sql문 실행

        listView = (ListView) findViewById(R.id.listView);

        adapter = new ShowAdapter();
        listView.setAdapter(adapter);
        listView.setFocusable(false);

        // 초기 데이터셋
        /*String sql1 = "insert into phonebook(name, age, mobile) values(?, ?, ?)";
        Object[] params1 = {"minji", 22, "01086250484"};
        Object[] params2 = {"sumin", 22, "01012345678"};
        database.execSQL(sql1, params1);
        database.execSQL(sql1, params2);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        // 데이터베이스 오픈
        database = openOrCreateDatabase("diary_database", MODE_PRIVATE, null);

        // 테이블 오픈
        String sql = "create table if not exists " + "phonebook" + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
        database.execSQL(sql); //sql문 실행

        listView = (ListView) findViewById(R.id.listView);

        adapter = new ShowAdapter();
        listView.setAdapter(adapter);
        listView.setFocusable(false);

        // 모든 레코드를 select 하여 listview에 연결하기
        if (database != null) {
            String sql2 = "select _id, name, age, mobile from phonebook order by name";
            Cursor cursor = database.rawQuery(sql2, null); //레코드셋 전체 집합을 저장

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext(); //다음 레코드로 넘어간다. cursor는 첫 번째 레코드의 그 전 레코드를 항상 가르킨다. 그래서 넘겨주는고임!
                int id = cursor.getInt(0);
                String name = cursor.getString(1); // 해당 커서의 0번째 속성
                int age = cursor.getInt(2);
                String mobile = cursor.getString(3);

                adapter.addItem(new PhoneBookShowItem(id, name, mobile,age));

                adapter.notifyDataSetChanged();
            }
            cursor.close(); //끝나면 커서 닫기
        }

        // 클릭하면 상세보기 화면으로
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PhoneBookShowItem item = (PhoneBookShowItem) adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), PhoneBookShowDetailActivity.class);
                intent.putExtra("ID", item.getId());
                intent.putExtra("Name", item.getName());
                intent.putExtra("Mobile", item.getMobile());
                intent.putExtra("Age", item.getAge());
                startActivity(intent);
            }
        });

        // 검색
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setQueryHint("이름으로 검색");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.clearItem();

                String sql3 = "select _id, name, age, mobile from phonebook where name Like "+ "'"+ '%' + s + '%' +  "'"+"order by name";
                Cursor cursor = database.rawQuery(sql3, null); //레코드셋 전체 집합을 저장

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext(); //다음 레코드로 넘어간다. cursor는 첫 번째 레코드의 그 전 레코드를 항상 가르킨다. 그래서 넘겨주는고임!
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1); // 해당 커서의 0번째 속성
                    int age = cursor.getInt(2);
                    String mobile = cursor.getString(3);

                    adapter.addItem(new PhoneBookShowItem(id, name, mobile, age));

                    adapter.notifyDataSetChanged();
                }
                cursor.close(); //끝나면 커서 닫기

                return false;
            }
        });
    }

    // 내부 클래스
    class ShowAdapter extends BaseAdapter {
        ArrayList<PhoneBookShowItem> items = new ArrayList<PhoneBookShowItem>();

        public void clearItem() {items.clear();}
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(PhoneBookShowItem item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override // ShowItem view에 있는 메소드에 아이템들 저장. 뷰가 return / 어댑터
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            PhoneBookShowItemView view = new PhoneBookShowItemView(getApplicationContext());

            PhoneBookShowItem item = items.get(position);
            view.setName(item.getName());
            view.setMobile(item.getMobile());

            Button callButton = (Button) view.findViewById(R.id.callButton);
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), item.getName()+"에게 전화걸기.", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+item.getMobile()));
                    startActivity(myIntent);
                }
            });

            return view;
        }
    }


    public void onAddButtonClicked(View v){
        Intent myIntent = new Intent(getApplicationContext(),PhoneBookInputActivity.class);
        startActivity(myIntent);
    }
    public void onHomeButtonClicked(View v){
        finish();
    }

}