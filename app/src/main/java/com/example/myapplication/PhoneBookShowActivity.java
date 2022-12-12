package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PhoneBookShowActivity extends AppCompatActivity {
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

    }

    @Override
    public void onResume() {
        super.onResume();
        // 데이터베이스 오픈
        database = openOrCreateDatabase("diary_database", MODE_PRIVATE, null);

        // 테이블 오픈
        String sql = "create table if not exists " + "phonebook" + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
        database.execSQL(sql);

        listView = (ListView) findViewById(R.id.listView);

        adapter = new ShowAdapter();
        listView.setAdapter(adapter);
        listView.setFocusable(false);

        // 모든 레코드를 select 하여 listview에 연결하기
        if (database != null) {
            String sql2 = "select _id, name, age, mobile from phonebook order by name";
            Cursor cursor = database.rawQuery(sql2, null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int age = cursor.getInt(2);
                String mobile = cursor.getString(3);

                adapter.addItem(new PhoneBookShowItem(id, name, mobile,age));

                adapter.notifyDataSetChanged();
            }
            cursor.close();
        }

        // 상세페이지 이동
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
                Cursor cursor = database.rawQuery(sql3, null);

                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int age = cursor.getInt(2);
                    String mobile = cursor.getString(3);

                    adapter.addItem(new PhoneBookShowItem(id, name, mobile, age));

                    adapter.notifyDataSetChanged();
                }
                cursor.close();

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

        @Override
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