package com.aloha.alaram2.database;

import android.provider.BaseColumns;

/**
 * Created by seoseongho on 15. 8. 5..
 */
public final class Database {

    public static final String TABLENAME = "alarmaloha";

    public static final class CreateDB implements BaseColumns {

        public static final String KIND = "kind";
        public static final String ACTIVE = "active";
        public static final String DAY = "day";
        public static final String TIME = "time";
        public static final String REPEAT = "repeat";
        public static final String VIB = "vibration";
        public static final String SOUND = "mute";
        public static final String SOURCE = "source";
        public static final String CREATEDB =
                "CREATE TABLE " + TABLENAME + "("
                        + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + KIND + " UNSIGNED TINYINT NOT NULL , "
                        + ACTIVE + " UNSIGNED TINYINT NOT NULL , "
                        + DAY + " UNSIGNED INT NOT NULL , "
                        + TIME + " UNSIGNED MEDIUMINT NOT NULL , "
                        + REPEAT + " UNSIGNED TINYINT NOT NULL , "
                        + VIB + " UNSIGNED TINYINT NOT NULL , "
                        + SOUND + " UNSIGNED TINYINT NOT NULL , "
                        + SOURCE + " VARCHAR(100) NOT NULL);";
    }


}
