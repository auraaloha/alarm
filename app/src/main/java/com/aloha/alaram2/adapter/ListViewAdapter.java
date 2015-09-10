package com.aloha.alaram2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aloha.alaram2.R;
import com.aloha.alaram2.database.DBhelper;
import com.aloha.alaram2.database.Database;

import java.util.ArrayList;

/**
 * Created by seoseongho on 15. 8. 6..
 */
public class ListViewAdapter extends ArrayAdapter<Alarm> {

    private final static int ALARM_NORMAL = 0x01;
    private final static int ALARM_ONETIME = 0x02;
    private final static int ACTIVE = 0x01;
    private final static int INACTIVE = 0x02;
    Typeface roboto_regular;
    Typeface roboto_medium;
    private LayoutInflater inflater;
    private ArrayList<Alarm> alarmList;
    private Context context;
    private int mWidth;
    private int mHeight;
    private int dayTextSize = 17;

    public ListViewAdapter(Context cnxt, int resource, int phoneWidth, int phoneHeight) {
        super(cnxt, resource);
        context = cnxt;
        mWidth = phoneWidth;
        mHeight = phoneHeight;
        alarmList = new ArrayList<>();
        inflater = LayoutInflater.from(context);

        roboto_medium = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf");
        roboto_regular = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Light.ttf");
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Alarm getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alarm_listview_row, null);
        }

        Alarm alarm = alarmList.get(position);
        final boolean[] dayInfo = alarm.getDay();

        View.OnClickListener dayTvClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dayindex = (int) v.getTag();
                if (dayindex < 0 || dayindex > 6) return;

                if (dayInfo[dayindex]) {
                    v.setAlpha((float) 0.2);
                    dayInfo[dayindex] = false;
                } else {
                    v.setAlpha(1);
                    dayInfo[dayindex] = true;
                }

            }
        };

        ImageButton alarmlight = new ImageButton(context);
        alarmlight.setBackground(null);
        alarmlight.setScaleType(ImageView.ScaleType.FIT_CENTER);
        alarmlight.setAdjustViewBounds(true);
        switch (alarm.getKind()) {
            case ALARM_NORMAL:
                if (alarm.getActive() == ACTIVE)
                    alarmlight.setImageResource(R.drawable.alram_small_on);
                else alarmlight.setImageResource(R.drawable.alram_small_off);
                break;

            case ALARM_ONETIME:
                alarmlight.setImageResource(R.drawable.alram_onetime);
                break;

            default:
                break;
        }

        //Light circle
        RelativeLayout toplayout = (RelativeLayout) convertView.findViewById(R.id.row_top_layout);
        toplayout.setLayoutParams(new AbsListView.LayoutParams(mWidth, (int) (mHeight * 0.1448)));
        RelativeLayout.LayoutParams alarmlightParams = new RelativeLayout.LayoutParams((int) (mHeight * 0.0926), (int) (mHeight * 0.0926));
        alarmlightParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        alarmlightParams.addRule(RelativeLayout.CENTER_VERTICAL);
        alarmlightParams.setMarginStart((int) (mWidth * 0.1037));
        alarmlight.setLayoutParams(alarmlightParams);
        toplayout.addView(alarmlight);

        //info layout
        LinearLayout alarmInfoLayout = new LinearLayout(context);
        alarmInfoLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams alarmInfoLayoutParams = new RelativeLayout.LayoutParams((int) (mWidth * 0.640), ViewGroup.LayoutParams.WRAP_CONTENT);
        alarmInfoLayoutParams.setMarginStart((int) (mWidth * 0.338));
        alarmInfoLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        alarmInfoLayout.setLayoutParams(alarmInfoLayoutParams);

        //Alarm time
        TextView timeTv = new TextView(context);
        timeTv.setText(alarm.getTime_s());
        timeTv.setTextColor(Color.WHITE);
        timeTv.setTextSize(20);
        timeTv.setTypeface(roboto_regular);

        //Days Layout
        LinearLayout daysLayout = new LinearLayout(context);
        LinearLayout.LayoutParams daysLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //daysLayoutParams.addRule(RelativeLayout.BELOW, timeTv.getId());
        //daysLayoutParams.addRule(RelativeLayout.ALIGN_START,timeTv.getId());
        daysLayout.setLayoutParams(daysLayoutParams);

        Log.v("timeTv ID", "" + timeTv.getId());

        //Days TextView

        //Layoutparams for all daytextview margin
        LinearLayout.LayoutParams dayTvparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dayTvparams.setMarginEnd((int) (mWidth * 0.01));
        //Monday
        TextView mondayTv = new TextView(context);
        mondayTv.setText("Mon");
        mondayTv.setTextColor(Color.WHITE);
        mondayTv.setTextSize(dayTextSize);
        mondayTv.setLayoutParams(dayTvparams);
        mondayTv.setOnClickListener(dayTvClickListener);
        mondayTv.setTag(1);
        //tuesday
        TextView tuesdayTv = new TextView(context);
        tuesdayTv.setText("Tue");
        tuesdayTv.setTextColor(Color.WHITE);
        tuesdayTv.setTextSize(dayTextSize);
        tuesdayTv.setLayoutParams(dayTvparams);
        tuesdayTv.setOnClickListener(dayTvClickListener);
        tuesdayTv.setTag(2);
        //wednesday
        TextView wednesdayTv = new TextView(context);
        wednesdayTv.setText("Wed");
        wednesdayTv.setTextColor(Color.WHITE);
        wednesdayTv.setTextSize(dayTextSize);
        wednesdayTv.setLayoutParams(dayTvparams);
        wednesdayTv.setOnClickListener(dayTvClickListener);
        wednesdayTv.setTag(3);
        //thursday
        TextView thursdayTv = new TextView(context);
        thursdayTv.setText("Thr");
        thursdayTv.setTextColor(Color.WHITE);
        thursdayTv.setTextSize(dayTextSize);
        thursdayTv.setLayoutParams(dayTvparams);
        thursdayTv.setOnClickListener(dayTvClickListener);
        thursdayTv.setTag(4);
        //Friday
        TextView fridayTv = new TextView(context);
        fridayTv.setText("Fri");
        fridayTv.setTextColor(Color.WHITE);
        fridayTv.setTextSize(dayTextSize);
        fridayTv.setLayoutParams(dayTvparams);
        fridayTv.setOnClickListener(dayTvClickListener);
        fridayTv.setTag(5);
        //Saturday
        TextView saturdayTv = new TextView(context);
        saturdayTv.setText("Sat");
        saturdayTv.setTextColor(Color.WHITE);
        saturdayTv.setTextSize(dayTextSize);
        saturdayTv.setLayoutParams(dayTvparams);
        saturdayTv.setOnClickListener(dayTvClickListener);
        saturdayTv.setTag(6);
        //Sunday
        TextView sundayTv = new TextView(context);
        sundayTv.setText("Sun");
        sundayTv.setTextColor(Color.WHITE);
        sundayTv.setTextSize(dayTextSize);
        sundayTv.setLayoutParams(dayTvparams);
        sundayTv.setOnClickListener(dayTvClickListener);
        sundayTv.setTag(0);

        TextView[] dayTvArray = {sundayTv, mondayTv, tuesdayTv, wednesdayTv, thursdayTv, fridayTv, saturdayTv};
        for (int i = 0; i < dayTvArray.length; i++) {
            if (!dayInfo[i]) dayTvArray[i].setAlpha((float) 0.2);
            //setFonts
            dayTvArray[i].setTypeface(roboto_medium);
        }

        //add textview
        daysLayout.addView(mondayTv);
        daysLayout.addView(tuesdayTv);
        daysLayout.addView(wednesdayTv);
        daysLayout.addView(thursdayTv);
        daysLayout.addView(fridayTv);
        daysLayout.addView(saturdayTv);
        daysLayout.addView(sundayTv);
        alarmInfoLayout.addView(timeTv);
        alarmInfoLayout.addView(daysLayout);

        //add alarm info layout to top layout
        toplayout.addView(alarmInfoLayout);


        return convertView;
    }

    @Override
    public void add(Alarm object) {
        alarmList.add(object);
    }

    public void makeAlarmList(SQLiteDatabase mDB) {
        Cursor mCursor = mDB.rawQuery("SELECT * FROM "+ Database.TABLENAME, null);

        while(mCursor.moveToNext()) {
            Alarm alarm = new Alarm();
            alarm.setId(mCursor.getInt(0));
            alarm.setKind(mCursor.getInt(1));
            alarm.setActive(mCursor.getInt(2));
            alarm.setDay(mCursor.getInt(3));
            alarm.setTime(mCursor.getInt(4));
            alarm.setRepeat(mCursor.getInt(5));
            alarm.setVib(mCursor.getInt(6));
            alarm.setSound(mCursor.getInt(7));
            alarm.setSource(mCursor.getString(8));

            add(alarm);
        }

        mCursor.close();
    }

}
