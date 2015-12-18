package com.aloha.alaram2;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by seoseongho on 15. 10. 14..
 */
public class AddAlarmActivity extends Activity {

    public final int WAKEUP = 0x1;
    public final int ONETIME = 0x2;
    public final int NORMAL = 0x3;
    public final int TODAY = 0x0;
    public final int WEEK = 0x1;
    public final int WEEKEND = 0x2;
    public final int SUN = 0x0;
    public final int MON = 0x1;
    public final int TUE = 0x2;
    public final int WED = 0x3;
    public final int THU = 0x4;
    public final int FRI = 0x5;
    public final int SAT = 0x6;
    public final int ALPHAICON = 0x0;
    public final int SOUNDICON = 0x1;
    public final int VIBICON = 0x2;
    public final int REPEATICON = 0x3;
    public final int ONETIMEICON = 0x4;
    final String[] DAYSELECT = new String[]{"오늘", "주중", "주말"};
    final String[] WEEKS = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    public boolean[] dayselect_state;
    public boolean[] alarm_days;
    public boolean[] settingselect_state;
    //GUI parts
    LinearLayout topLayout;
    LinearLayout aboveLayout;
    RelativeLayout clockLayout;
    LinearLayout setDayLayout;
    LinearLayout setWeekLayout;
    LinearLayout settingLayout;
    LinearLayout musicLayout;
    //above Layout Btns
    ImageButton aboveWakeupBtn;
    ImageButton aboveOnetimeBtn;
    ImageButton aboveNormalBtn;
    //setWeek Layout Btns;
    TextView[] dayBtns;
    //SetDays Layout Btns;
    TextView[] daysBtns;
    //setting Layout Btns;
    ImageButton[] settingIcons;
    int todayindex;
    //Listeners
    View.OnClickListener alarmKindBtnListener;
    View.OnClickListener alarmDaysBtnClickListener;
    View.OnClickListener alarmWeekBtnClickListener;
    View.OnClickListener alarmSettingBtnClickListner;
    //kind_alarm_selector;
    int alarm_kind;
    //resolution(pixels)
    int mWidth;
    int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addalarm);

        getPhoneResolution();
        addClickListener();
        setlayout();
        setSelector();

    }

    private void addClickListener() {

        alarmKindBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();

                if (tag == alarm_kind) return;
                else {
                    switch (tag) {
                        case WAKEUP:
                            aboveWakeupBtn.setAlpha(0.99f);
                            aboveOnetimeBtn.setAlpha(0.3f);
                            aboveNormalBtn.setAlpha(0.3f);
                            alarm_kind = WAKEUP;
                            break;
                        case ONETIME:
                            aboveWakeupBtn.setAlpha(0.3f);
                            aboveOnetimeBtn.setAlpha(0.99f);
                            aboveNormalBtn.setAlpha(0.3f);
                            alarm_kind = ONETIME;
                            break;
                        case NORMAL:
                            aboveWakeupBtn.setAlpha(0.3f);
                            aboveOnetimeBtn.setAlpha(0.3f);
                            aboveNormalBtn.setAlpha(0.99f);
                            alarm_kind = NORMAL;
                            break;

                        default:
                            break;
                    }
                }
            }
        };

        alarmDaysBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();

                switch (tag) {
                    case TODAY:
                        if (dayselect_state[TODAY]) {
                            dayselect_state[TODAY] = false;
                            alarm_days[todayindex] = false;
                        } else {
                            dayselect_state[TODAY] = true;
                            alarm_days[todayindex] = true;
                        }

                        if (alarm_days[MON] && alarm_days[TUE] && alarm_days[WED] && alarm_days[THU] && alarm_days[FRI])
                            dayselect_state[WEEK] = true;
                        else dayselect_state[WEEK] = false;
                        if (alarm_days[SAT] && alarm_days[SUN]) dayselect_state[WEEKEND] = true;
                        else dayselect_state[WEEKEND] = false;

                        break;
                    case WEEK:
                        if (dayselect_state[WEEK]) {
                            dayselect_state[WEEK] = false;
                            alarm_days[MON] = false;
                            alarm_days[TUE] = false;
                            alarm_days[WED] = false;
                            alarm_days[THU] = false;
                            alarm_days[FRI] = false;
                        } else {
                            dayselect_state[WEEK] = true;
                            alarm_days[MON] = true;
                            alarm_days[TUE] = true;
                            alarm_days[WED] = true;
                            alarm_days[THU] = true;
                            alarm_days[FRI] = true;

                            if (alarm_days[todayindex]) dayselect_state[TODAY] = true;
                        }
                        if (dayselect_state[TODAY]) {
                            alarm_days[todayindex] = true;
                        }
                        break;
                    case WEEKEND:
                        if (dayselect_state[WEEKEND]) {
                            dayselect_state[WEEKEND] = false;
                            alarm_days[SAT] = false;
                            alarm_days[SUN] = false;
                        } else {
                            dayselect_state[WEEKEND] = true;
                            alarm_days[SAT] = true;
                            alarm_days[SUN] = true;

                            if (alarm_days[todayindex]) dayselect_state[TODAY] = true;
                        }
                        if (dayselect_state[TODAY]) {
                            alarm_days[todayindex] = true;
                        }
                        break;
                    default:
                        break;
                }

                changeDaysGUI();

            }
        };

        alarmWeekBtnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();

                switch (tag) {
                    case MON:
                        if (alarm_days[MON]) alarm_days[MON] = false;
                        else alarm_days[MON] = true;
                        break;
                    case TUE:
                        if (alarm_days[TUE]) alarm_days[TUE] = false;
                        else alarm_days[TUE] = true;
                        break;
                    case WED:
                        if (alarm_days[WED]) alarm_days[WED] = false;
                        else alarm_days[WED] = true;
                        break;
                    case THU:
                        if (alarm_days[THU]) alarm_days[THU] = false;
                        else alarm_days[THU] = true;
                        break;
                    case FRI:
                        if (alarm_days[FRI]) alarm_days[FRI] = false;
                        else alarm_days[FRI] = true;
                        break;
                    case SAT:
                        if (alarm_days[SAT]) alarm_days[SAT] = false;
                        else alarm_days[SAT] = true;
                        break;
                    case SUN:
                        if (alarm_days[SUN]) alarm_days[SUN] = false;
                        else alarm_days[SUN] = true;
                        break;
                    default:
                        break;
                }

                if (alarm_days[todayindex]) dayselect_state[TODAY] = true;
                else dayselect_state[TODAY] = false;
                if (alarm_days[MON] && alarm_days[TUE] && alarm_days[WED] && alarm_days[THU] && alarm_days[FRI])
                    dayselect_state[WEEK] = true;
                else dayselect_state[WEEK] = false;
                if (alarm_days[SAT] && alarm_days[SUN]) dayselect_state[WEEKEND] = true;
                else dayselect_state[WEEKEND] = false;

                changeDaysGUI();
            }
        };

        alarmSettingBtnClickListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (int) v.getTag();

                switch (tag) {
                    case ALPHAICON:
                        if (settingselect_state[ALPHAICON]) settingselect_state[ALPHAICON] = false;
                        else settingselect_state[ALPHAICON] = true;
                        break;
                    case SOUNDICON:
                        if (settingselect_state[SOUNDICON]) settingselect_state[SOUNDICON] = false;
                        else settingselect_state[SOUNDICON] = true;
                        break;
                    case VIBICON:
                        if (settingselect_state[VIBICON]) settingselect_state[VIBICON] = false;
                        else settingselect_state[VIBICON] = true;
                        break;
                    case REPEATICON:
                        if (settingselect_state[REPEATICON])
                            settingselect_state[REPEATICON] = false;
                        else settingselect_state[REPEATICON] = true;
                        break;
                    case ONETIMEICON:
                        if (settingselect_state[ONETIMEICON])
                            settingselect_state[ONETIMEICON] = false;
                        else settingselect_state[ONETIMEICON] = true;
                        break;
                    default:
                        break;
                }

                changeSettingsGUI();
            }
        };

    }

    private void setSelector() {
        alarm_kind = 1;

        alarm_days = new boolean[]{false, false, false, false, false, false, false};
        dayselect_state = new boolean[]{true, false, false};
        settingselect_state = new boolean[]{true, true, true, true, false};

        changeSettingsGUI();
        //find today
        todayindex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        alarm_days[todayindex] = true;
        changeDaysGUI();


    }

    private void changeSettingsGUI() {

        for (int i = 0; i < settingselect_state.length; i++) {
            if (settingselect_state[i]) settingIcons[i].setAlpha(1f);
            else settingIcons[i].setAlpha(0.3f);
        }

    }

    private void changeDaysGUI() {

        //켜진게 두개고 합이 11 이면 주말 체크. 이런식으로 하기.

        for (int i = 0; i < alarm_days.length; i++) {
            if (alarm_days[i]) dayBtns[i].setAlpha(0.9f);
            else dayBtns[i].setAlpha(0.3f);
        }

        for (int i = 0; i < dayselect_state.length; i++) {
            if (dayselect_state[i]) daysBtns[i].setAlpha(0.9f);
            else daysBtns[i].setAlpha(0.3f);
        }

    }

    private void setlayout() {

        topLayout = (LinearLayout) findViewById(R.id.toplayout_addalarm);

        aboveLayout = new LinearLayout(this);
        LinearLayout.LayoutParams aboveLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.0915 * mHeight));
        aboveLayoutParams.setMargins(0, 0, 0, 0);
        aboveLayout.setLayoutParams(aboveLayoutParams);
        aboveLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams aboveBtnParams = new LinearLayout.LayoutParams((int) (0.1433 * mWidth), ViewGroup.LayoutParams.MATCH_PARENT);
        aboveBtnParams.setMarginStart((int) (0.0051 * mHeight));

        aboveWakeupBtn = new ImageButton(this);
        aboveWakeupBtn.setBackground(null);
        aboveWakeupBtn.setAdjustViewBounds(true);
        aboveWakeupBtn.setImageResource(R.drawable.wakeup_alarm);
        aboveWakeupBtn.setTag(WAKEUP);
        aboveWakeupBtn.setOnClickListener(alarmKindBtnListener);
        aboveLayout.addView(aboveWakeupBtn, aboveBtnParams);

        aboveOnetimeBtn = new ImageButton(this);
        aboveOnetimeBtn.setImageResource(R.drawable.onetime_alarm);
        aboveOnetimeBtn.setAdjustViewBounds(true);
        aboveOnetimeBtn.setAlpha(0.3f);
        aboveOnetimeBtn.setBackground(null);
        aboveOnetimeBtn.setTag(ONETIME);
        aboveOnetimeBtn.setOnClickListener(alarmKindBtnListener);
        aboveLayout.addView(aboveOnetimeBtn, aboveBtnParams);

        aboveNormalBtn = new ImageButton(this);
        aboveNormalBtn.setImageResource(R.drawable.normal_alarm);
        aboveNormalBtn.setAdjustViewBounds(true);
        aboveNormalBtn.setBackground(null);
        aboveNormalBtn.setAlpha(0.3f);
        aboveNormalBtn.setTag(NORMAL);
        aboveNormalBtn.setOnClickListener(alarmKindBtnListener);
        aboveLayout.addView(aboveNormalBtn, aboveBtnParams);

        clockLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams clockLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) (0.4499 * mHeight));
        clockLayout.setLayoutParams(clockLayoutParams);
        clockLayout.setBackgroundColor(Color.argb(40, 255, 255, 255));

        //temporary clock design
        ImageButton clockCircle = new ImageButton(this);
        clockCircle.setBackground(null);
        clockCircle.setImageResource(R.drawable.minute_circle);
        clockCircle.setAdjustViewBounds(true);
        clockCircle.setScaleType(ImageView.ScaleType.FIT_CENTER);
        RelativeLayout.LayoutParams clockCircleParams = new RelativeLayout.LayoutParams((int) (0.7721 * mWidth), (int) (0.7721 * mWidth));
        clockCircleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        clockLayout.addView(clockCircle, clockCircleParams);

        ImageButton inCircle = new ImageButton(this);
        inCircle.setBackground(null);
        inCircle.setImageResource(R.drawable.hour_circle);
        inCircle.setAdjustViewBounds(true);
        inCircle.setScaleType(ImageView.ScaleType.FIT_CENTER);
        RelativeLayout.LayoutParams inCircleParams = new RelativeLayout.LayoutParams((int) (0.5044 * mWidth), (int) (0.5044 * mWidth));
        inCircleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        clockLayout.addView(inCircle, inCircleParams);


        setDayLayout = new LinearLayout(this);
        LinearLayout.LayoutParams setDayLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.0846 * mHeight)); //0.1146
        setDayLayout.setLayoutParams(setDayLayoutParams);
        setDayLayout.setOrientation(LinearLayout.HORIZONTAL);
        setDayLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        //setDayLayout.setBackgroundColor(Color.WHITE);
        setDayLayout.setPadding((int) (0.536 * mWidth), (int) (0.0106 * mHeight), 0, 0);

        addSetDaysBtns();

        setWeekLayout = new LinearLayout(this);
        LinearLayout.LayoutParams setWeekLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.0639 * mHeight));
        setWeekLayout.setLayoutParams(setWeekLayoutParams);
        setWeekLayout.setOrientation(LinearLayout.HORIZONTAL);
        setWeekLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        setWeekLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        //setWeekLayout.setBackgroundColor(Color.RED);

        addDaysBtns();

        settingLayout = new LinearLayout(this);
        LinearLayout.LayoutParams settingLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.1453 * mHeight));
        settingLayout.setLayoutParams(settingLayoutParams);
        settingLayout.setOrientation(LinearLayout.HORIZONTAL);
        //settingLayout.setBackgroundColor(Color.argb(30,255,255,255));
        settingLayout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        settingLayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        settingLayout.setPadding(0, /* (int) (mWidth*0.0306)*/0, (int) (mWidth * 0.0306), 0);

        addSettingIcons();

        musicLayout = new LinearLayout(this);
        LinearLayout.LayoutParams musicLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.0839 * mHeight));
        musicLayout.setLayoutParams(musicLayoutParams);
        musicLayout.setOrientation(LinearLayout.HORIZONTAL);
        musicLayout.setBackgroundColor(Color.RED);


        topLayout.addView(aboveLayout);
        topLayout.addView(clockLayout);
        topLayout.addView(setDayLayout);
        topLayout.addView(setWeekLayout);
        topLayout.addView(settingLayout);
        topLayout.addView(musicLayout);

    }

    private void addSetDaysBtns() {

        daysBtns = new TextView[3];
        for (int i = 0; i < 3; i++) {
            daysBtns[i] = new TextView(this);
            daysBtns[i].setText(DAYSELECT[i]);
            daysBtns[i].setTextColor(Color.WHITE);
            daysBtns[i].setBackground(null);
            daysBtns[i].setTextSize(25);
            daysBtns[i].setAlpha(0.3f);
            daysBtns[i].setTag(i);
            daysBtns[i].setOnClickListener(alarmDaysBtnClickListener);
            if (i != 0) daysBtns[i].setPadding(50, 0, 0, 0);
            setDayLayout.addView(daysBtns[i], LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    private void addDaysBtns() {

        dayBtns = new TextView[7];
        for (int i = 0; i < 7; i++) {
            dayBtns[i] = new TextView(this);
            dayBtns[i].setText(WEEKS[i]);
            dayBtns[i].setTextColor(Color.WHITE);
            dayBtns[i].setBackground(null);
            dayBtns[i].setTextSize(24);
            dayBtns[i].setAlpha(0.3f);
            dayBtns[i].setTag(i);
            dayBtns[i].setOnClickListener(alarmWeekBtnClickListener);
            dayBtns[i].setPadding(23, 0, 23, 0);

            //ADD SUNDAY later for ordering
            if (i != 0) {
                setWeekLayout.addView(dayBtns[i], LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        }

        //ADD SUNDAY
        setWeekLayout.addView(dayBtns[0], LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    private void addSettingIcons() {

        int[] IconWidths = new int[]{(int) (0.1426 * mWidth), (int) (0.1203 * mWidth), (int) (0.1203 * mWidth), (int) (0.1252 * mWidth), (int) (0.1555 * mWidth)};
        int[] IconDrawables = new int[]{R.drawable.setting_alpha, R.drawable.setting_sound, R.drawable.setting_vib, R.drawable.setting_repeat, R.drawable.setting_onetime};
        settingIcons = new ImageButton[5];


        for (int i = 0; i < settingIcons.length; i++) {
            settingIcons[i] = new ImageButton(this);
            settingIcons[i].setBackground(null);
            settingIcons[i].setImageResource(IconDrawables[i]);
            settingIcons[i].setAdjustViewBounds(true);
            settingIcons[i].setTag(i);
            settingIcons[i].setOnClickListener(alarmSettingBtnClickListner);

            LinearLayout.LayoutParams tempParams = new LinearLayout.LayoutParams(IconWidths[i], LinearLayout.LayoutParams.MATCH_PARENT);
            tempParams.setMargins((int) (mWidth * 0.03), 0, (int) (mWidth * 0.03), 0);
            settingLayout.addView(settingIcons[i], tempParams); //, IconWidths[i] ,LinearLayout.LayoutParams.MATCH_PARENT);
        }

    }

    private void getPhoneResolution() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;

    }

}
