package com.livedata.simplesteps;

import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.livedata.simplesteps.database.AppDatabase;
import com.livedata.simplesteps.database.User;
import com.livedata.simplesteps.database.UserDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    UserDao mUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mUserDao = AppDatabase.getInstance(getApplicationContext()).userDao();

        mUserDao.getUsersLiveData()
                .observe(this, new Observer<List<User>>() {
                    @Override
                    public void onChanged(@Nullable List<User> users) {
                        String s = "";
                        for (User user : users) s = String.format("%s - %s", s, user.getName());
                        System.out.println("RRR ROOM = " + s);
                    }
                });
    }

    public void SimpleProcessing(View view) {
        mainViewModel.usersLiveData().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                String s = "";
                for (User user : users) s = String.format("%s - %s", s, user.getName());
                System.out.println("RRR simple = " + s);
            }
        });
    }

    public void WithPreference(View view) {
        mainViewModel.prefLiveData().observe(this, new Observer<SharedPreferenceUtils>() {
            @Override
            public void onChanged(@Nullable SharedPreferenceUtils sharedPreferenceUtils) {
                System.out.println("RRR shared pref = " + sharedPreferenceUtils.getStringValue("user"));
            }
        });
    }

    public void TransformationMap(View view) {
        mainViewModel.transformationMap().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                String sa = "";
                for (String s : strings) sa = String.format("%s + %s", sa, s);
                System.out.println("RRR map = " + sa);
            }
        });
    }

    public void TransformationSwitch(View view) {
        mainViewModel.transformationSwitchMap().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                String s = "";
                for (User user : users) s = String.format("%s - %s", s, user.getName());
                System.out.println("RRR Switch = " + s);
            }
        });
    }

    public void AddUser(View view) {
        new AddUserDialog(this).addNewMessage();
    }

    public void ClearUsers(View view) {
        AppDatabase.getInstance(this).userDao().deleteAll();
    }

    public void MediatorLiveData(View view) {

        final MediatorLiveData<List<User>> usersLiveData = new MediatorLiveData<>();

        usersLiveData.addSource(mainViewModel.usersLiveData(), new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> newUserList) {
                usersLiveData.setValue(newUserList);
            }
        });

        usersLiveData.addSource(mUserDao.getUsersLiveData(), new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> newUserList) {
                usersLiveData.setValue(newUserList);
            }
        });

        usersLiveData.observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                String s = "";
                for (User user : users) s = String.format("%s - %s", s, user.getName());
                System.out.println("RRR Mediator = " + s);
            }
        });
    }
}
