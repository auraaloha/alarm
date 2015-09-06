package com.aloha.alaram2;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aloha.alaram2.adapter.Alarm;
import com.aloha.alaram2.adapter.ListViewAdapter;
import com.aloha.alaram2.database.DBhelper;


public class MainActivity extends Activity {

    //practice
    Typeface font;
    //Database
    private DBhelper mDBhelper;
    //Listview
    private ListView alarmListView;
    private ListViewAdapter adapter;
    //phone width, height
    private int mWidth;
    private int mHeight;
    /**
     * layout and GUI components  *
     */
    //main menubar
    private RelativeLayout menubarLayout;
    private ImageButton deleteBtn;
    private ImageButton addAlarmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //prac
        font = Typeface.createFromAsset(getBaseContext().getAssets(), "font/Roboto-Medium.ttf");
        TextView robo = (TextView) findViewById(R.id.roboto);
        robo.setTypeface(font);

        init();
    }

    private void init() {
        openDB();
        getPhoneResolution();
        makeMenubar();
        makeListView();
    }

    private void openDB() {
        mDBhelper = new DBhelper(this);
    }

    private void getPhoneResolution() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;

        Log.v("RESOLUTION - WIDTH", mWidth + "");
        Log.v("RESOLUTION - HEIGHT", mHeight + "");

    }

    private void makeMenubar() {
        menubarLayout = (RelativeLayout) findViewById(R.id.main_menubar);
        menubarLayout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (mHeight * 0.0662)));
        deleteBtn = new ImageButton(this);
        deleteBtn.setBackground(null);
        deleteBtn.setImageResource(R.drawable.trashcan);
        RelativeLayout.LayoutParams deleteBtnParams = new RelativeLayout.LayoutParams((int) (mHeight * 0.0555), (int) (mHeight * 0.0555));
        deleteBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        deleteBtnParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        deleteBtnParams.setMarginStart((int) (mWidth * 0.0435));
        deleteBtn.setLayoutParams(deleteBtnParams);
        addAlarmBtn = new ImageButton(this);
        addAlarmBtn.setBackground(null);
        addAlarmBtn.setImageResource(R.drawable.addalarm);
        RelativeLayout.LayoutParams addAlarmBtnParams = new RelativeLayout.LayoutParams((int) (mHeight * 0.0555), (int) (mHeight * 0.0555));
        addAlarmBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addAlarmBtnParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        addAlarmBtnParams.setMarginEnd((int) (mWidth * 0.0435));
        addAlarmBtn.setLayoutParams(addAlarmBtnParams);
        menubarLayout.addView(deleteBtn);
        menubarLayout.addView(addAlarmBtn);
    }

    private void makeListView() {
        alarmListView = (ListView) findViewById(R.id.main_alarm_listView);
        //alarmListView.set
        adapter = new ListViewAdapter(this, R.layout.alarm_listview_row, mWidth, mHeight);
        alarmListView.setAdapter(adapter);
        alarmListView.setDivider(null);
        adapter.setNotifyOnChange(true);
        adapter.add(new Alarm(2, 1, 0b11, 700, 1, 1, 1, "MUSIC1"));
        adapter.add(new Alarm(1, 1, 0b111, 830, 1, 1, 1, "MUSIC2"));
        adapter.add(new Alarm(1, 2, 0b1010110, 1330, 1, 1, 1, "MUSIC3"));


    }


}
