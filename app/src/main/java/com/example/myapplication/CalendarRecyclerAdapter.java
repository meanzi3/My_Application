package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarRecyclerAdapter extends RecyclerView.Adapter<CalendarRecyclerAdapter.viewHolder> {
  private Context context;
  private String[] data;

  private int week;
  private int max;
  private int savedPosition;
  private viewHolder[] savedViewHolder = new viewHolder[36];
  private GregorianCalendar cal;

  public CalendarRecyclerAdapter(Context context, String[] data, int week, int max, GregorianCalendar cal){
    super();
    this.context = context;
    this.data = data;
    this.week = week;
    this.max = max;
    this.cal = cal;
  }

  public class viewHolder extends RecyclerView.ViewHolder{
    TextView dayText, checkText;
    LinearLayout linearLayout;
    public viewHolder(View itemView){
      super(itemView);
      dayText = itemView.findViewById(R.id.calender_recycler_day);
      checkText = itemView.findViewById(R.id.calender_recycler_check);
      linearLayout = itemView.findViewById(R.id.day_layout);

      itemView.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
          int position = getAdapterPosition();
          if(position != RecyclerView.NO_POSITION){
            Selected(savedPosition, position);
            savedPosition = position;
            Intent intent = new Intent(context.getApplicationContext(), TodoShowActivity.class);
            intent.putExtra("Year", cal.get(Calendar.YEAR));
            intent.putExtra("Day", savedViewHolder[position].dayText.getText().toString());
            if(position < week){
              intent.putExtra("Month", cal.get(Calendar.MONTH));
            }else if (position>(max+week)){
              intent.putExtra("Month", cal.get(Calendar.MONTH)+2);
            }else{
              intent.putExtra("Month", cal.get(Calendar.MONTH)+1);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
          }
        }
      });
    }
  }
  @NonNull
  @Override
  public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(context).inflate(R.layout.calendar_item, parent, false);

    return new viewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CalendarRecyclerAdapter.viewHolder holder, int position) {
    savedViewHolder[position] = holder;

    // 일요일은 빨간색으로
    if(position % 7 == 0){
      holder.dayText.setTextColor(Color.argb(100,201,0,0));
    }else if(position % 7 == 6){ // 토요일은 파란색으로
      holder.dayText.setTextColor(Color.argb(100,0,30,201));
    }

    // 현재 달이 아닌 날은 회색으로
    if(position < week || position>(max+week)){
      holder.dayText.setTextColor(Color.argb(100,140,140,140));
    }
    holder.dayText.setText(data[position]);

  }
  @Override
  public int getItemCount() {
    return data.length;
  }

  protected void Selected(int savedPosition, int position){
    savedViewHolder[savedPosition].linearLayout.setBackgroundColor(Color.argb(100,255,255,255));
    savedViewHolder[position].linearLayout.setBackgroundColor(Color.argb(100,148,153,183));
    //
  }

}
