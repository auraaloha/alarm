package com.aloha.alaram2.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.aloha.alaram2.MainActivity;
import com.aloha.alaram2.R;
import com.aloha.alaram2.adapter.Alarm;

/**
 * Created by seoseongho on 15. 9. 24..
 */
public class Notifier {

    final int[] dayString = {R.string.sunday, R.string.monday, R.string.tuesday, R.string.wednesday, R.string.thursday, R.string.friday, R.string.saturday};
    Context context;

    public Notifier(Context context) {
        this.context = context;
    }

    public void notifyMessage(Alarm alarm) {

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (alarm == null) {
            nm.cancel(998812);
            return;
        }

        String notiTitle = context.getString(R.string.app_name);
        String content = "S";

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(R.drawable.alpha_large_on);
        builder.setTicker(content);
        builder.setWhen(System.currentTimeMillis());

        // 알림 제목.
        builder.setContentTitle(context.getString(R.string.app_name));

        // 알림 내용.
        builder.setContentText(makeAlarmMessage(alarm));

        // 알림시 사운드, 진동, 불빛을 설정 가능.
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS);

        // 알림 터치시 반응.
        builder.setContentIntent(pendingIntent);

        // 알림 터치시 반응 후 알림 삭제 여부.
        builder.setAutoCancel(false);

        builder.setOngoing(true);

        // 우선순위.
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        // 고유ID로 알림을 생성.
        nm.notify(998812, builder.build());

    }

    String makeAlarmMessage(Alarm alarm) {

        String message = "다음알람은 " + context.getString(dayString[alarm.getFastest()]);
        message += alarm.getTime_s();
        message += " 에 울립니다.";

        return message;
    }
}
