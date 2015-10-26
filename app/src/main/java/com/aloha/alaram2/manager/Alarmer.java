package com.aloha.alaram2.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.aloha.alaram2.AlarmActivity;
import com.aloha.alaram2.adapter.Alarm;

/**
 * Created by seoseongho on 15. 9. 10..
 */
public class Alarmer {

    Context context;
    AlarmManager alarmManager;

//    private static final String INTENT_ACTION = "com.aloha.alaram2.alarmmanager";

    public Alarmer(Context cnxt) {
        this.context = cnxt;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(Alarm alarm) {

        if (alarm == null) {
            return;
        }

        int timeToAdd = alarm.getTimeToAdd();

        Intent Intent = new Intent(context, AlarmActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + timeToAdd, pIntent);
    }
}
