package com.aloha.alaram2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.aloha.alaram2.adapter.Alarm;
import com.aloha.alaram2.adapter.ListViewAdapter;
import com.aloha.alaram2.database.DBhelper;
import com.aloha.alaram2.interfaces.AlarmChangeListener;
import com.aloha.alaram2.manager.Alarmer;
import com.aloha.alaram2.manager.Notifier;

import java.sql.SQLException;


public class MainActivity extends Activity implements View.OnClickListener, AlarmChangeListener {


    //prac
    View.OnClickListener ol;
    View.OnClickListener cl;
    //Database
    private DBhelper mDBhelper;
    //Listview
    private ListView alarmListView;
    private ListViewAdapter adapter;
    //Alarm
    private Alarmer mAlarmer;
    //Notifier
    private Notifier mNotifier;
    //phone width, height
    private int mWidth;
    private int mHeight;
    /* layout and GUI components  */
    //main menubar
    private RelativeLayout menubarLayout;
    private ImageButton deleteBtn;
    private ImageButton addAlarmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlarmer = new Alarmer(this);
        mNotifier = new Notifier(this);

        openDB();
        getPhoneResolution();
        makeMenubar();
        makeListView();
        setAlarmToSystem();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mDBhelper.close();
    }

    private void openDB() {
        mDBhelper = new DBhelper(this, this);
        try {
            mDBhelper.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        addAlarmBtn.setOnClickListener(this);
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
        adapter = new ListViewAdapter(this, mDBhelper, R.layout.alarm_listview_row, mWidth, mHeight);
        alarmListView.setAdapter(adapter);
        alarmListView.setDivider(null);
        adapter.setNotifyOnChange(true);

        adapter.makeAlarmList();


    }

    private void setAlarmToSystem() {
        Alarm nextAlarm = adapter.findNextAlarm();
        mNotifier.notifyMessage(nextAlarm);
        mAlarmer.setAlarm(nextAlarm);
    }

    @Override
    public void onClick(View v) {

        Intent i = new Intent(MainActivity.this, AddAlarmActivity.class);
        startActivity(i);

    }

    @Override
    public void onAlarmDataChanged() {
        setAlarmToSystem();
    }

    @Override
    public void onAlarmDataCreated() {
        if (adapter != null) {
            adapter.closeAdapter();
        }
        makeListView();
        setAlarmToSystem();
    }

}
