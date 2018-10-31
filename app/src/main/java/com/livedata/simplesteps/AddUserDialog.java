package com.livedata.simplesteps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.livedata.simplesteps.database.AppDatabase;
import com.livedata.simplesteps.database.User;
import com.livedata.simplesteps.database.UserDao;

import java.util.Random;

class AddUserDialog {

    private Context context;

    AddUserDialog(Context context) {
        this.context = context;
    }

    void addNewMessage() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.dialog_layout, null);

        final EditText etName = subView.findViewById(R.id.etName);
        final EditText etEmail = subView.findViewById(R.id.etEmail);
        etName.setText(String.format("Name %s", generateRandomChars()));
        etEmail.setText(String.format("Email %s", generateRandomChars()));

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add new User");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("ADD User", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String name = etName.getText().toString();
                final String email = etEmail.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email))
                    Toast.makeText(context, "Field(s) are empty...", Toast.LENGTH_LONG).show();
                else {
                    User u = new User(name, email);
                    UserDao userDao = AppDatabase.getInstance(context).userDao();
                    userDao.insertUser(u);
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }

    private String generateRandomChars() {
        String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        int length = 5;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++)
            sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
        return sb.toString();
    }
}
