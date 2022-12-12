package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// 커스터마이징 뷰
public class PhoneBookShowItemView extends LinearLayout{
    TextView textView;
    TextView textView2;
    ImageView imageView;
    Button callButton;

    public PhoneBookShowItemView(Context context) {
        super(context);
        init(context);
    }

    public PhoneBookShowItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        // inflator -> xml로 정의된 것을 가져와서
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.phonebook_item, this, true);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        imageView = (ImageView) findViewById(R.id.imageView);
        callButton = (Button) findViewById(R.id.callButton);

        callButton.setFocusable(false);

    }

    public void setName(String name) {
        textView.setText(name);
    }

    public void setMobile(String mobile) {
        textView2.setText(mobile);
    }

//    public void setImage(int resId) {
//        imageView.setImageResource(resId);
//    }
}