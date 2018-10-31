package com.livedata.simplesteps;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.livedata.simplesteps.database.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainViewModel extends AndroidViewModel {

    private MutableLiveData<List<User>> mUserListLiveData;
    private MutableLiveData<SharedPreferenceUtils> mPrefLiveData;
    private SharedPreferenceUtils mPreferenceUtils;
    private List<User> users = new ArrayList<>();
    private Handler handler = new Handler();

    public MainViewModel(@NonNull Application application) {
        super(application);
        mPreferenceUtils = new SharedPreferenceUtils(application.getApplicationContext());
        loadUsers();
    }

    LiveData<List<User>> usersLiveData() {
        if (mUserListLiveData == null) mUserListLiveData = new MutableLiveData<>();
        return mUserListLiveData;
    }

    LiveData<SharedPreferenceUtils> prefLiveData() {
        if (mPrefLiveData == null) mPrefLiveData = new MutableLiveData<>();
        return mPrefLiveData;
    }

    private void loadUsers() {
        usersLiveData();
        prefLiveData();

        for (int i = 0; i < 10; i++) users.add(new User("Name" + i, "Email " + i));

        mUserListLiveData.setValue(users);

        Runnable r = new Runnable() {
            public void run() {
                Collections.shuffle(users, new Random(System.nanoTime()));
                mUserListLiveData.setValue(users);
                mPreferenceUtils.setValue("user", objectToJson(users));
                mPrefLiveData.setValue(mPreferenceUtils);
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(r, 5000);
    }

    private String objectToJson(Object o) {
        return new Gson().toJson(o);
    }

    private List<User> jsonToObject(String s) {
        return new Gson().fromJson(s, new TypeToken<List<User>>() {
        }.getType());
    }

    LiveData<List<String>> transformationMap() {
//        return Transformations.map(prefLiveData(),
//                new Function<SharedPreferenceUtils, List<String>>() {
//                    @Override
//                    public List<String> apply(SharedPreferenceUtils input) {
//                        List<String> stringList = new ArrayList<>();
//                        for (User user : jsonToObject(input.getStringValue("user")))
//                            stringList.add(user.getName());
//                        return stringList;
//                    }
//                });
        return Transformations.map(usersLiveData(),
                new Function<List<User>, List<String>>() {
                    @Override
                    public List<String> apply(List<User> user) {
                        List<String> stringList = new ArrayList<>();
                        for (User user1 : user) stringList.add(user1.getName());
                        return stringList;
                    }
                });
    }

    LiveData<List<User>> transformationSwitchMap() {
        return Transformations.switchMap(
                usersLiveData(),
                new Function<List<User>, LiveData<List<User>>>() {
                    @Override
                    public LiveData<List<User>> apply(List<User> input) {
                        //      Can also add from ROOM
                        for (int i = 0; i < 3; i++)
                            input.add(new User("Test " + i, "Test " + i));
                        MutableLiveData<List<User>> data = new MutableLiveData<>();
                        data.setValue(input);
                        return data;
                    }
                });
    }

}