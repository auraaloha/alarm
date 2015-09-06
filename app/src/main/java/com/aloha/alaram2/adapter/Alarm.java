package com.aloha.alaram2.adapter;

import android.util.Log;

/**
 * Created by seoseongho on 15. 8. 3..
 */
public class Alarm {

    public final int SUN = 0;
    public final int MON = 1;
    public final int TUE = 2;
    public final int WED = 3;
    public final int THR = 4;
    public final int FRI = 5;
    public final int SAT = 6;
    private int kind;
    private int active;
    private boolean[] day;
    private int time;
    private int repeat;
    private int vib;
    private int sound;
    private String source;
    private String time_s;

    public Alarm(int kind, int active, int day, int time, int repeat, int vibration, int sound, String source) {
        this.kind = kind;
        this.active = active;
        this.day = new boolean[7];
        this.time = time;

        time_s = changeIntToTime(time);
        changeIntToDay(day);
        Log.v("TIME ADDED", time_s);
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getVib() {
        return vib;
    }

    public void setVib(int vib) {
        this.vib = vib;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean[] getDay() {
        return day;
    }

    public void setDay(boolean[] day) {
        this.day = day;
    }

    public String getTime_s() {
        return time_s;
    }

    public void setTime_s(String time_s) {
        this.time_s = time_s;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String changeIntToTime(int time) {

        String time_s = "";
        int min = time % 100;
        String min_s = "";
        if (min < 10) min_s += "0" + min;
        else min_s += "" + min;

        if (time < 2359 && time > 0 && time % 100 < 60) {
            if (time <= 1159) {
                if (time < 100) time_s += "AM 0";
                else time_s += "AM ";

                time_s += time / 100 + "";
                time_s += " : ";
                time_s += min_s;
            } else {
                int pmtime = time - 1200;
                if (pmtime < 100) {
                    time_s += "PM 12 : " + time % 100;
                } else {
                    time_s += "PM ";
                    time_s += pmtime / 100 + "";
                    time_s += " : ";
                    time_s += min_s;
                }

            }
        } else {
            return "ERROR";
        }

        return time_s;
    }

    public void changeIntToDay(int day) {

        if (day < 0 || day > 127) return;

        int temp = day;
        int dayIndex = 0;
        boolean loop = true;
        while (loop) {
            switch (temp) {
                case 1:
                    this.day[dayIndex] = true;
                    loop = false;
                    break;
                case 0:
                    loop = false;
                    break;
                default:
                    if (dayIndex > 6) {
                        loop = false;
                        break;
                    } else {
                        if ((temp & 0b1) == 1) {
                            this.day[dayIndex] = true;
                        }
                    }
                    break;
            }
            temp = temp >> 1;
            dayIndex++;
        }

        Log.v("CHANGEDAY", "FINISHED");
    }
}
