package com.aloha.alaram2.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aloha.alaram2.R;
import com.aloha.alaram2.database.DBhelper;
import com.aloha.alaram2.database.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by seoseongho on 15. 8. 6..
 */
public class ListViewAdapter extends ArrayAdapter<Alarm> implements AdapterView.OnItemSelectedListener {

    //Constants
    private final static int ALARM_NORMAL = 0x01;
    private final static int ALARM_ONETIME = 0x02;
    private final static int ACTIVE = 0x01;
    private final static int INACTIVE = 0x02;
    private final String MEDIA_PATH = Environment.getExternalStorageDirectory().getPath() + "/aloha/";
    public ViewHolder[] holders;
    //media player
    public MediaPlayer mPlayer;
    //Fonts
    Typeface roboto_regular;
    Typeface roboto_medium;
    //DB
    DBhelper mDBhepler;
    //for spinner
    ArrayList<String> songlist;
    String[] songnames = new String[]{"maps.mp3", "song2.mp3", "song3.mp3"};
    private int rowSelected = -1;
    //Components
    private LayoutInflater inflater;
    private ArrayList<Alarm> alarmList;
    private Context context;
    private int mWidth;
    private int mHeight;
    private int dayTextSize = 17;

    public ListViewAdapter(Context cnxt, DBhelper helper, int resource, int phoneWidth, int phoneHeight) {
        super(cnxt, resource);
        context = cnxt;
        mWidth = phoneWidth;
        mHeight = phoneHeight;
        alarmList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
        holders = new ViewHolder[100]; // change it to Link structure.

        roboto_medium = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Medium.ttf");
        roboto_regular = Typeface.createFromAsset(getContext().getAssets(), "font/Roboto-Light.ttf");

        mPlayer = new MediaPlayer();

        mDBhepler = helper;

        //for spinner
        songlist = new ArrayList<String>();
        for (int i = 0; i < songnames.length; i++) {
            songlist.add(songnames[i]);
        }

    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Alarm getItem(int position) {
        return alarmList.get(position);
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

        //view holder
        ViewHolder vHolder = new ViewHolder();
        holders[position] = vHolder;
        final Alarm alarm = alarmList.get(position);
        final boolean[] dayInfo = alarm.getDays();


        /** On Click Listeners  **/
        View.OnClickListener dayTvClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dayindex = (int) v.getTag();
                if (dayindex < 0 || dayindex > 6) return;

                if (dayInfo[dayindex]) {
                    v.setAlpha((float) 0.2);
                    dayInfo[dayindex] = false;
                    alarm.setDays(dayInfo);

                    //DB access
                    mDBhepler.modifyColumn(alarm);


                } else {
                    v.setAlpha(1);
                    dayInfo[dayindex] = true;
                    alarm.setDays(dayInfo);

                    //DB access
                    mDBhepler.modifyColumn(alarm);
                }

            }
        };

        View.OnClickListener circleClickListener = new View.OnClickListener() {

            private int mShortAnimationDuration;

            @Override
            public void onClick(View v) {

                int rowClicked = (int) v.getTag();

                if (rowClicked != rowSelected) {
                    activate(rowClicked);
                    rowSelected = rowClicked;
                    return;
                }

                //++animation semaphore is needed
                Alarm a = getItem(rowClicked);

                if (a.getKind() == ALARM_ONETIME) ; //++onetime alarm off is needed
                else {
                    if (a.getActive() == ACTIVE) {
                        ((ImageButton) v).setImageResource(R.drawable.alram_large_off);
                        a.setActive(INACTIVE);
                        //db access
                        mDBhepler.modifyColumn(a);
                    } else {
                        ((ImageButton) v).setImageResource(R.drawable.alram_large_on);
                        a.setActive(ACTIVE);
                        //db access
                        mDBhepler.modifyColumn(a);
                    }
                }

            }

        };

        View.OnClickListener rowClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int rowClicked = (int) v.getTag();
                Log.v("ROW CLICKED", rowClicked + "");

                if (rowSelected != rowClicked) {
                    activate(rowClicked);
                    rowSelected = rowClicked;
                } else if (rowSelected == rowClicked) {
                    activate(-1);
                }

            }

        };


        vHolder.alarmlight = new ImageButton(context);
        //set Tag for finding position
        vHolder.alarmlight.setTag(position);
        //Image featuring
        vHolder.alarmlight.setBackground(null);
        vHolder.alarmlight.setScaleType(ImageView.ScaleType.FIT_CENTER);
        vHolder.alarmlight.setAdjustViewBounds(true);
        switch (alarm.getKind()) {
            case ALARM_NORMAL:
                if (alarm.getActive() == ACTIVE)
                    vHolder.alarmlight.setImageResource(R.drawable.alram_large_on);
                else vHolder.alarmlight.setImageResource(R.drawable.alram_large_off);
                break;

            case ALARM_ONETIME:
                vHolder.alarmlight.setImageResource(R.drawable.alram_onetime);
                break;

            default:
                break;
        }

        //Light circle
        RelativeLayout toplayout = (RelativeLayout) convertView.findViewById(R.id.row_top_layout);
        //Tag top layout with position for row selecting animation
        toplayout.setTag(position);
        toplayout.setOnClickListener(rowClickListener);
        toplayout.setLayoutParams(new AbsListView.LayoutParams(mWidth, (int) (mHeight * 0.1448)));
        RelativeLayout.LayoutParams alarmlightParams = new RelativeLayout.LayoutParams((int) (mHeight * 0.0856), (int) (mHeight * 0.0856));
        alarmlightParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        alarmlightParams.addRule(RelativeLayout.CENTER_VERTICAL);
        alarmlightParams.setMarginStart((int) (mWidth * 0.0737));
        vHolder.alarmlight.setLayoutParams(alarmlightParams);
        vHolder.alarmlight.setOnClickListener(circleClickListener);
        toplayout.addView(vHolder.alarmlight);

        //info layout
        LinearLayout alarmInfoLayout = new LinearLayout(context);
        alarmInfoLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams alarmInfoLayoutParams = new RelativeLayout.LayoutParams((int) (mWidth * 0.640), ViewGroup.LayoutParams.WRAP_CONTENT);
        alarmInfoLayoutParams.setMarginStart((int) (mWidth * 0.318));
        alarmInfoLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        alarmInfoLayout.setLayoutParams(alarmInfoLayoutParams);

        //Alarm time
        vHolder.timeTv = new TextView(context);
        vHolder.timeTv.setText(alarm.getTime_s());
        vHolder.timeTv.setTextScaleX(0.85f);
        vHolder.timeTv.setTextColor(Color.WHITE);
        vHolder.timeTv.setTextSize(20);
        vHolder.timeTv.setTypeface(roboto_regular);

        //Days Layout
        LinearLayout daysLayout = new LinearLayout(context);
        LinearLayout.LayoutParams daysLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        daysLayout.setLayoutParams(daysLayoutParams);


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
        thursdayTv.setText("Thu");
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
        alarmInfoLayout.addView(vHolder.timeTv);
        alarmInfoLayout.addView(daysLayout);


        //etc option layout
        LinearLayout optionLayout = new LinearLayout(context);
        LinearLayout.LayoutParams optionLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        optionLayout.setOrientation(LinearLayout.HORIZONTAL);
        optionLayout.setLayoutParams(optionLayoutParams);

        vHolder.songTv = new TextView(context);
        vHolder.songTv.setText("Maps - Maroon 5");
        vHolder.songTv.setTypeface(roboto_medium);
        vHolder.songTv.setTextSize(3 * dayTextSize / 4);
        vHolder.songTv.setTextColor(Color.WHITE);
        vHolder.songTv.setVisibility(View.GONE);
        vHolder.songTv.setBackgroundResource(R.drawable.color_selector);
        //Click Listner (maybe spinner or custom dialog)
        optionLayout.addView(vHolder.songTv);
        alarmInfoLayout.addView(optionLayout);


        //add alarm info layout to top layout
        toplayout.addView(alarmInfoLayout);


        return convertView;
    }

    @Override
    public void clear() {
        super.clear();
        alarmList.clear();
        notifyDataSetChanged();
    }

    @Override
    public void add(Alarm object) {
        alarmList.add(object);
    }

    public void makeAlarmList() {

        clear();

        //modify this later
        Cursor mCursor = mDBhepler.mDB.rawQuery("SELECT * FROM " + Database.TABLENAME, null);

        while (mCursor.moveToNext()) {
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

    private void playsong(String source) {
        mPlayer.reset();
        try {
            Log.v("PATH", MEDIA_PATH + "maps.mp3");
            mPlayer.setDataSource(MEDIA_PATH + "maps.mp3");
            mPlayer.prepare();
            mPlayer.start();

            Toast.makeText(context, "재생 : maps.mp3", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "재생 실패", Toast.LENGTH_SHORT).show();
        }
    }

    void activate(int rowClicked) {
        int mShortAnimationDuration = context.getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        //inactivate present row
        if (rowSelected >= 0) {
            //make light circle smaller
            holders[rowSelected].alarmlight.animate().scaleX(1).scaleY(1);
            //make time textview smaller
            holders[rowSelected].timeTv.animate().translationX(1).scaleX(1).scaleY(1).translationY(1);
            //make song option textview disappear
            holders[rowSelected].songTv.animate().alpha(0.0f)
                    .setDuration(mShortAnimationDuration)
                    .setListener(null);
            holders[rowSelected].songTv.setVisibility(View.GONE);
        }

        if (rowClicked == -1) {
            mPlayer.reset();
            rowSelected = -1;
            return;
        }

        //activate new row
        //make light circle larger
        holders[rowClicked].alarmlight.animate().scaleX(1.7f).scaleY(1.7f);
        //make time textview larger
        holders[rowClicked].timeTv.animate().translationX(320).scaleX(1.7f).scaleY(1.7f).translationY(-5);
        //make song option textview appear
        holders[rowClicked].songTv.setAlpha(0f);
        holders[rowClicked].songTv.setVisibility(View.VISIBLE);
        holders[rowClicked].songTv.animate()
                .alpha(1.0f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        //play song
        playsong(null);


    }

    public Alarm findNextAlarm() {

        Alarm fastAlarm = null;

        //AM = 0
        //PM = 1

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = c.get(Calendar.HOUR);
        int am_pm = c.get(Calendar.AM_PM);
        int min = c.get(Calendar.MINUTE);
        int curTime = (am_pm * 12 + hour) * 100 + min;

        Log.v("day", "" + day);

        int[] dayindex = new int[7];
        for (int i = 0; i < 7; i++) {
            if (i < day) {
                dayindex[i] = i - day + 7;
            } else {
                dayindex[i] = (i - day);
            }
        }


        Alarm tempAlarm;
        int temptime;
        int fasttime = 99999;
        int whatday = 0;
        for (int j = 0; j < alarmList.size(); j++) {

            tempAlarm = alarmList.get(j);

            if (tempAlarm.getActive() == ACTIVE) {
                for (int k = 0; k < 7; k++) {
                    if (!tempAlarm.getDays()[k]) ;
                    else {
                        temptime = dayindex[k] * 10000 + tempAlarm.getTime();
                        if (temptime <= curTime) {
                            temptime += 80000;
                        }
                        if (fastAlarm == null) {
                            fastAlarm = tempAlarm;
                            fasttime = temptime;
                            whatday = k;
                        } else {
                            if (fasttime > temptime) {
                                fastAlarm = tempAlarm;
                                fasttime = temptime;
                                whatday = k;
                            }
                        }

                    }
                }
            }
        }

        if (fastAlarm != null) {
            int intervalTime = fastAlarm.getTime() - curTime;
            int hourToAdd = 0;
            int minToAdd = 0;
            if (intervalTime < 0) {
                intervalTime = Math.abs(intervalTime);
                hourToAdd -= intervalTime / 100;
                minToAdd -= intervalTime % 100;
            } else if (intervalTime == 0) {
            } else {
                hourToAdd += intervalTime / 100;
                minToAdd += intervalTime % 100;
            }
            int timeToAdd = (dayindex[whatday] * 24 * 60 + hourToAdd * 60 + minToAdd) * 60000;

            if (timeToAdd < 0) timeToAdd += 7 * 24 * 60 * 60000;
            fastAlarm.setFastest(whatday);
            fastAlarm.setTimeToAdd(timeToAdd);
        }
        return fastAlarm;

    }

    public void closeAdapter() {
        mPlayer.reset();
        mPlayer.release();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class ViewHolder {
        public ImageButton alarmlight;
        public TextView songTv;
        TextView timeTv;
    }

}
