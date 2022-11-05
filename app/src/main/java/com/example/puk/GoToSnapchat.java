package com.example.puk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GoToSnapchat extends AppCompatActivity {

    private ProgressBar scProgress;
    private int scProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_snapchat);

        // create back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        long time = extras.getLong("sc_time");
        int scTimeMinute = (int) Math.round((time/1000)/60);
        int scAvg = 27;
        scProgress = (ProgressBar) findViewById(R.id.sc_progress);
        scProgress.setMax(scAvg);
        scProgress.setProgress(scTimeMinute);

        TextView lTextView = (TextView) findViewById(R.id.sc_motivator);
        StringBuilder lStringBuilder = new StringBuilder();

        lStringBuilder.append("You have spent ");
        lStringBuilder.append(scTimeMinute);
        lStringBuilder.append(" minutes out of the ideal ");
        lStringBuilder.append(scAvg);
        lStringBuilder.append(" minutes.");
        lStringBuilder.append(System.getProperty("line.separator"));
        lStringBuilder.append(System.getProperty("line.separator"));

        if (scTimeMinute >= scAvg) {
            lStringBuilder.append("You did it! Well done!");
        } else {
            lStringBuilder.append("You can do it!");
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append("Only ");
            lStringBuilder.append(scAvg-scTimeMinute);
            lStringBuilder.append(" minutes to go");
        }

        lTextView.setText(lStringBuilder.toString());

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}