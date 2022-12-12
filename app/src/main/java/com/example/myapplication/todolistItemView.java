package com.example.myapplication;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class todolistItemView extends LinearLayout{
    LinearLayout layout;
    TextView textView, textView2;
    Button deletebutton;


    public todolistItemView(Context context) {
        super(context);
        init(context);
    }

    public todolistItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calender_todolistitem, this, true);

        layout = (LinearLayout) findViewById(R.id.layout);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        deletebutton=(Button)findViewById(R.id.checkBox2);

        deletebutton.setFocusable(false);

    }

    public void settodolist(String todolist) {
        textView.setText(todolist);
    }
    public void setday(int year, int month, int day){textView2.setText(year+"/"+month+"/"+day);}
    public void deleteDay(){layout.removeView(textView2);}

}
