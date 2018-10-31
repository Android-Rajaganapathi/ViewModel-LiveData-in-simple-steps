package com.livedata.simplesteps.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;

    public abstract UserDao userDao();

    private Context context;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null)
            appDatabase = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "User-database")
                    .allowMainThreadQueries()
                    .build();
        return appDatabase;
    }

    public static void destroyInstance() {
        appDatabase = null;
    }
}
