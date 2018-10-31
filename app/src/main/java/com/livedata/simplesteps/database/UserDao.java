package com.livedata.simplesteps.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Query("Select * from User")
    LiveData<List<User>> getUsersLiveData();

    @Delete
    void deleteUser(User user);

    @Insert(onConflict = REPLACE)
    void insertUser(User user);

    @Query("DELETE FROM User ")
    void deleteAll();
}
