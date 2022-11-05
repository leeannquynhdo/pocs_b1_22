package com.example.puk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity{
    // how to UsageStats and UsageStatsManager:
    // https://github.com/timoshenkoav/USMSample
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 100;

    private ImageButton fb_button;
    private ImageButton ig_button;
    private ImageButton sc_button;
    private ImageButton tt_button;

    private ProgressBar overallProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Map<String, Long> lAppUsage = fillStats();

        int totalTime = 33+28+27+31;

        long userTotalTime = 0;
        for(long l : lAppUsage.values()){
            userTotalTime += l;
        }
        int totalTimeMinutes = (int) Math.round((userTotalTime/1000)/60);

        overallProgressBar = (ProgressBar) findViewById(R.id.overall_progress);
        overallProgressBar.setMax(totalTime);
        overallProgressBar.setProgress(totalTimeMinutes);

        // add motivational text to front page
        TextView lTextView = (TextView) findViewById(R.id.front_motivator);
        StringBuilder lStringBuilder = new StringBuilder();

        lStringBuilder.append("You have spent ");
        lStringBuilder.append(totalTimeMinutes);
        lStringBuilder.append(" minutes out of the ideal ");
        lStringBuilder.append(totalTime);
        lStringBuilder.append(" minutes.");
        lStringBuilder.append(System.getProperty("line.separator"));
        lStringBuilder.append(System.getProperty("line.separator"));

        if (totalTimeMinutes >= totalTime) {
            lStringBuilder.append("You did it! Well done!");
        } else {
            lStringBuilder.append("You can do it!");
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append("Only ");
            lStringBuilder.append(totalTime-totalTimeMinutes);
            lStringBuilder.append(" minutes to go");
        }

        lTextView.setText(lStringBuilder.toString());

        fb_button = (ImageButton) findViewById(R.id.fb_button); // xml id for button
        fb_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFacebook();
            }

        });
        ig_button = (ImageButton) findViewById(R.id.ig_button);
        ig_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInstagram();
            }
        });
        sc_button = (ImageButton) findViewById(R.id.sc_button);
        sc_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSnapchat();
            }
        });
        tt_button = (ImageButton) findViewById(R.id.tt_button);
        tt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTikTok();
            }
        });

        // https://stackoverflow.com/a/34518115
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            // https://stackoverflow.com/a/71377121
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 18);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
        }
    }


    private Map<String, Long> fillStats() {
        Map<String, Long> onNoPermission = new HashMap<String, Long>();
        if (hasPermission()){
            return getStats();
        }else{
            requestPermission();
        }
        return onNoPermission;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "resultCode " + resultCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS:
                fillStats();
                break;
        }
    }
    // request permission to access user stats
    private void requestPermission() {
        Toast.makeText(this, "Need to request permission", Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
    }
    // check if the app has permission to access user stats
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private Map<String, Long> getStats() {
        UsageStatsManager lUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        // startTime should be the beginning of the day
        long startTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
        // endTime should be the current time
        long endTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);

        // list of all application on emulator
        List<UsageStats> lUsageStatsList = lUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        // hard-coded list of relevant applications
        List<String> lAppNames = new ArrayList<String>();
        lAppNames.add("com.facebook.katana");
        lAppNames.add("com.instagram.android");
        lAppNames.add("com.snapchat.android");
        lAppNames.add("com.zhiliaoapp.musically");

        Map<String, Long> lAppUsage = new HashMap<String, Long>();

        for (UsageStats lUsageStats:lUsageStatsList){
            if (lAppNames.contains(lUsageStats.getPackageName())) {
                lAppUsage.put(lUsageStats.getPackageName(), lUsageStats.getTotalTimeInForeground());
            }
        }
        Log.d("lAppUsage entry ", Arrays.asList(lAppUsage).toString());

        return lAppUsage;
    }


    public void openFacebook() {
        long fb_time = fillStats().get("com.facebook.katana");
        Intent fb_intent = new Intent(this, GoToFacebook.class);
        fb_intent.putExtra("fb_time", fb_time);
        startActivity(fb_intent);
    }

    public void openInstagram() {
        long ig_time = fillStats().get("com.instagram.android");
        Intent ig_intent = new Intent(this, GoToInstagram.class);
        ig_intent.putExtra("ig_time", ig_time);
        startActivity(ig_intent);
    }

    public void openSnapchat() {
        long sc_time = fillStats().get("com.snapchat.android");
        Intent sc_intent = new Intent(this, GoToSnapchat.class);
        sc_intent.putExtra("sc_time", sc_time);
        startActivity(sc_intent);
    }

    public void openTikTok() {
        long tt_time = fillStats().get("com.zhiliaoapp.musically");
        Intent tt_intent = new Intent(this, GoToTikTok.class);
        tt_intent.putExtra("tt_time", tt_time);
        startActivity(tt_intent);
    }

}

