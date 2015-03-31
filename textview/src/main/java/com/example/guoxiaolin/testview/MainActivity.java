package com.example.guoxiaolin.testview;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;


public class MainActivity extends Activity {
    CalendarWiget mCalendarWiget;
    BatteryWidget  mBatteryWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.lock_screen_two);
        initCalendarView();


    }
    private void initCalendarView() {
        mCalendarWiget=new CalendarWiget(this);
        mCalendarWiget.initView(this);
        mCalendarWiget.updateView();
        DragView mDragView;
        mDragView = (DragView) this.findViewById(R.id.calendarDragView);
     //   mDragView.setHeaderHeight(100);
     //   mDragView.setContentHeight(300);
        mDragView.setOnPageChangeListener(new DragView.OnPageChangeListener() {

            @Override
            public void onPageChange() {
                // TODO Auto-generated method stub
                startActivity(mCalendarWiget.createComponentName());
            }
        });
    }
    private void initContactView() {
        mBatteryWidget=new BatteryWidget(this);
        DragView mDragView;
        mDragView = (DragView) this.findViewById(R.id.batteryDragView);
   //     mDragView.setHeaderHeight(100);
    //    mDragView.setContentHeight(300);
        mDragView.setOnPageChangeListener(new DragView.OnPageChangeListener() {

            @Override
            public void onPageChange() {
                // TODO Auto-generated method stub
                startActivity(mCalendarWiget.createComponentName());
            }
        });
    }
    public  void startActivity(ComponentName mComponentName){
        Toast.makeText(MainActivity.this, "啓動", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(mComponentName);
        startActivity(intent);
    };

}
