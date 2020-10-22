package com.example.wattodo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class createTask extends AppCompatActivity implements View.OnClickListener {

    EditText title,editTextTime,editTextDate;
    Button createTask;
    String titleTxt;
    Calendar cal;   /////// To get  Current Date
    public Calendar TaskDateAndTime;

    final public String TAG = createTask.class.getSimpleName();

    private NotificationManager mNotificationManager;
    AlarmManager alarmManager;
    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        getSupportActionBar();

         title =(EditText) findViewById(R.id.editTextTitle);
         editTextDate = (EditText) findViewById(R.id.editTextDate);
         editTextTime = (EditText) findViewById(R.id.editTextTime);
         createTask  = findViewById(R.id.buttonTask);
         TaskDateAndTime = Calendar.getInstance();
         editTextTime.setOnClickListener(this);
         editTextDate.setOnClickListener(this);
         createTask.setOnClickListener(this);

        createNotificationChannel();
    }

    private void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String name = "Task Notification";
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
              notificationChannel.enableLights(true);
              notificationChannel.setLightColor(Color.RED);
              notificationChannel.enableVibration(true);
            notificationChannel.setDescription("To Notify Task before or on time");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        Log.d(TAG, "createNotificationChannel: Channel Created");
    }

    @Override
    public void onClick(View view) {
         switch (view.getId())
         {
             case R.id.editTextTime :
                 setTime();
                 break;
             case R.id.editTextDate :
                 setDate();
                 break;
             case R.id.buttonTask :
                 addTask();

                 break;
    }
}

    private void setAlarmManager() {
         Intent intent = new Intent(createTask.this,AlarmReceiver.class);
         intent.putExtra("title",titleTxt);
        Log.d(TAG, "Inside setAlarmManager: "+titleTxt);
         PendingIntent pendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "setAlarmManager: "+TaskDateAndTime.get(Calendar.YEAR)+"/"+TaskDateAndTime.get(Calendar.MONTH)+"/"+TaskDateAndTime.get(Calendar.DAY_OF_MONTH)+" - "+TaskDateAndTime.get(Calendar.HOUR_OF_DAY)+":"+TaskDateAndTime.get(Calendar.MINUTE));
         alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
          if(Build.VERSION.SDK_INT >= 23)
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,TaskDateAndTime.getTimeInMillis(),pendingIntent);
           else if(Build.VERSION.SDK_INT >= 19)
              alarmManager.setExact(AlarmManager.RTC_WAKEUP,TaskDateAndTime.getTimeInMillis(),pendingIntent);
            else
              alarmManager.set(AlarmManager.RTC_WAKEUP,TaskDateAndTime.getTimeInMillis(),pendingIntent);
    }

    private void moveToHome() {
        startActivity(new Intent(createTask.this, ListMainPage.class));
    }

    private void addTask() {

        titleTxt = title.getText().toString();
        String timeTxt = editTextTime.getText().toString();
        String dateTxt = editTextDate.getText().toString();

        if(titleTxt.isEmpty()) {
            title.setError("Fill title");
            title.requestFocus(); return;
        }
        if(timeTxt.isEmpty()) {
            editTextTime.setError("Fill Time");
            editTextTime.requestFocus(); return;
        }
        if(dateTxt.isEmpty()) {
            editTextDate.setError("Fill Date");
            editTextDate.requestFocus(); return;
        }
        TaskClass map = new TaskClass(titleTxt,dateTxt,timeTxt);

        String name = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("Task").child(name).child(String.valueOf(map.hashCode()))
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        setAlarmManager();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(createTask.this, "Failed to insert.. Create Task again", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(createTask.this, "Task Added Successfully "+TaskDateAndTime.get(Calendar.SHORT), Toast.LENGTH_SHORT).show();
            }
        });
        moveToHome();
    }

    private void setDate() {
         cal = Calendar.getInstance();
         int year = cal.get(Calendar.YEAR);
         int month = cal.get(Calendar.MONTH);
         int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                TaskDateAndTime.set(Calendar.YEAR,year);
                TaskDateAndTime.set(Calendar.MONTH,month);
                TaskDateAndTime.set(Calendar.DAY_OF_MONTH,day);
                month = month+1;
                String date = day+"/"+month+"/"+year;
                editTextDate.setText(date);
            }
        },year,month,day);
        dpd.show();
    }

    private void setTime() {
        cal = Calendar.getInstance();
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMin = cal.get(Calendar.MINUTE);
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                TaskDateAndTime.set(Calendar.HOUR_OF_DAY,hour);
                TaskDateAndTime.set(Calendar.MINUTE,min);
                TaskDateAndTime.set(Calendar.SECOND,0);
                editTextTime.setText(hour+":"+min);
            }
        },currentHour,currentMin, DateFormat.is24HourFormat(this));
        tpd.show();
    }
}