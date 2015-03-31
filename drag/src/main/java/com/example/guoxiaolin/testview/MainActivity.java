package com.example.guoxiaolin.testview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity {
    WidgetFrameView mWidgetFrameView;

    LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.activity_main);
        mWidgetFrameView= (WidgetFrameView) this.findViewById(R.id.widgetFrameLayout);
     //   setContentView(R.layout.calendar_widget);

        initView();
        initView2();
        initView3();
        initView4();
      //  initView5();

    }

    private void initView2() {
        // TODO Auto-generated method stub
        RelativeLayout localRelativeLayout = (RelativeLayout) findViewById(R.id.calendarLayout);
        DragView myScrollDragViewGroup;
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View main2 = layoutInflater.inflate(R.layout.activity_main2, mWidgetFrameView,false);
        myScrollDragViewGroup = (DragView) main2.findViewById(R.id.myScrollDragView);
        CalendarWiget mCalendarWiget=new CalendarWiget(this);

       // myScrollDragViewGroup.setScreenHeight(260);
       myScrollDragViewGroup.setHeader(mCalendarWiget.createHeaderView()); // 添加滑动菜单的view
        myScrollDragViewGroup.setContent(mCalendarWiget.createContentView()); // 添加主页面的view
        myScrollDragViewGroup.setOnPageChangeListener(new DragView.OnPageChangeListener() {

            @Override
            public void onPageChange() {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "啓動", Toast.LENGTH_SHORT).show();
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        localRelativeLayout.addView(main2,params);
    }

    private void initView() {
        RelativeLayout galleryLayout = (RelativeLayout) findViewById(R.id.galleryLayout);

        DragView myScrollDragViewGroup;
        // TODO Auto-generated method stub

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View main2 = layoutInflater.inflate(R.layout.activity_main2, null);
        myScrollDragViewGroup = (DragView) main2.findViewById(R.id.myScrollDragView);
        ContactWidget mContactWidget = new ContactWidget(this);
      //  myScrollDragViewGroup.setScreenHeight(200);
        myScrollDragViewGroup.setHeader(mContactWidget.createHeaderView());//滑动菜单的view
        myScrollDragViewGroup.setContent(mContactWidget.createContentView());// 添加主页面的view
        myScrollDragViewGroup.setOnPageChangeListener(new DragView.OnPageChangeListener() {

            @Override
            public void onPageChange() {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "啓動", Toast.LENGTH_SHORT).show();
            }
        });


        galleryLayout.addView(main2);
    }

    private void initView3() {
        RelativeLayout galleryLayout = (RelativeLayout) findViewById(R.id.batteryLayout);

        DragView myScrollDragViewGroup;
        // TODO Auto-generated method stub

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View main2 = layoutInflater.inflate(R.layout.activity_main2, null);
        myScrollDragViewGroup = (DragView) main2.findViewById(R.id.myScrollDragView);
        ContactWidget mContactWidget = new ContactWidget(this);
        //  myScrollDragViewGroup.setScreenHeight(200);
        myScrollDragViewGroup.setHeader(mContactWidget.createHeaderView());//滑动菜单的view
        myScrollDragViewGroup.setContent(mContactWidget.createContentView());// 添加主页面的view
        myScrollDragViewGroup.setOnPageChangeListener(new DragView.OnPageChangeListener() {

            @Override
            public void onPageChange() {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "啓動", Toast.LENGTH_SHORT).show();
            }
        });


        galleryLayout.addView(main2);
    }
    private void initView4() {
        RelativeLayout galleryLayout = (RelativeLayout) findViewById(R.id.weatherLayout);

        DragView myScrollDragViewGroup;
        // TODO Auto-generated method stub

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View main2 = layoutInflater.inflate(R.layout.activity_main2, null);
        myScrollDragViewGroup = (DragView) main2.findViewById(R.id.myScrollDragView);
        ContactWidget mContactWidget = new ContactWidget(this);
        //  myScrollDragViewGroup.setScreenHeight(200);
        myScrollDragViewGroup.setHeader(mContactWidget.createHeaderView());//滑动菜单的view
        myScrollDragViewGroup.setContent(mContactWidget.createContentView());// 添加主页面的view
        myScrollDragViewGroup.setOnPageChangeListener(new DragView.OnPageChangeListener() {

            @Override
            public void onPageChange() {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "啓動", Toast.LENGTH_SHORT).show();
            }
        });


        galleryLayout.addView(main2);
    }
    private void initView5() {
        RelativeLayout galleryLayout = (RelativeLayout) findViewById(R.id.musicLayout);

        DragView myScrollDragViewGroup;
        // TODO Auto-generated method stub

        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View main2 = layoutInflater.inflate(R.layout.activity_main2, null);
        myScrollDragViewGroup = (DragView) main2.findViewById(R.id.myScrollDragView);
        ContactWidget mContactWidget = new ContactWidget(this);
        //  myScrollDragViewGroup.setScreenHeight(200);
        myScrollDragViewGroup.setHeader(mContactWidget.createHeaderView());//滑动菜单的view
        myScrollDragViewGroup.setContent(mContactWidget.createContentView());// 添加主页面的view
        myScrollDragViewGroup.setOnPageChangeListener(new DragView.OnPageChangeListener() {

            @Override
            public void onPageChange() {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.this, "啓動", Toast.LENGTH_SHORT).show();
            }
        });


        galleryLayout.addView(main2);
    }
}
