package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

public class TodoShowActivity extends Activity {

    EditText editText;
    TextView textView;

    ListView listView;
    SingerAdapter adapter;
    SQLiteDatabase database;
    String databaseName = "diary_database";
    String tableName="todo_table";

    private int year;
    private int month;
    private int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 뒷배경 흐리게 하기
        WindowManager.LayoutParams layoutParams= new WindowManager.LayoutParams();
        layoutParams.flags= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount= 0.7f;
        getWindow().setAttributes(layoutParams);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar_popup);

        // 다이얼로그 화면이 투명해진다
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        // 사이즈 조절
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.85); // Display 사이즈의 90%
        int height = (int) (dm.heightPixels * 0.7); // Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

    }
    public void onResume(){
        super.onResume();
        listView = (ListView) findViewById(R.id.listView);

        adapter = new SingerAdapter();
        database = openOrCreateDatabase(databaseName, MODE_PRIVATE,null) ;
        String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, todolist text, memo text, year int, month int, day int)";
        database.execSQL(sql);
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!year is: "+date.years+"month is :"+date.month+"year is : "+date.day);

        year=getIntent().getIntExtra("Year",1);
        month=getIntent().getIntExtra("Month",1);
        days=Integer.parseInt(getIntent().getStringExtra("Day"));

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(year+"년 "+month+"월 "+days+"일");

        if(database != null){
            // 해당날짜에 해당하는 값만 받아오기
            String sql2 = "select * from "+tableName+ " where year == "+year+" and month == "+month+" and day == "+days;
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
                intent.putExtra("year : ",year);
                intent.putExtra("month : ",month);
                intent.putExtra("day : ",days);
                intent.putExtra("todo : ", item.gettodolist());
                intent.putExtra("memo : ", item.getmemo());
                startActivity(intent);
            }
        });

        ImageButton addbutton = (ImageButton) findViewById(R.id.plus);
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TodoInput.class);
                intent.putExtra("Year", year);
                intent.putExtra("Month", month);
                intent.putExtra("Day", days);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
            view.deleteDay();

            // 삭제하기
            Button deleteImageView = (Button) view.findViewById(R.id.checkBox2);
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(TodoShowActivity.this);
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
