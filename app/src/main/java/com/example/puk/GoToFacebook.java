package com.example.puk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GoToFacebook extends AppCompatActivity {

    private ProgressBar fbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_facebook);

        // create back button
        // https://stackoverflow.com/a/27212978
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        long time = extras.getLong("fb_time");
        int fbTimeMinute = (int) Math.round((time/1000)/60);
        int fbAvgTime = 33;
        fbProgress = (ProgressBar) findViewById(R.id.fb_progress);
        fbProgress.setMax(fbAvgTime);
        fbProgress.setProgress(fbTimeMinute);

        TextView lTextView = (TextView) findViewById(R.id.fb_motivator);
        StringBuilder lStringBuilder = new StringBuilder();

        lStringBuilder.append("You have spent ");
        lStringBuilder.append(fbTimeMinute);
        lStringBuilder.append(" minutes out of the ideal ");
        lStringBuilder.append(fbAvgTime);
        lStringBuilder.append(" minutes.");
        lStringBuilder.append(System.getProperty("line.separator"));
        lStringBuilder.append(System.getProperty("line.separator"));

        if (fbTimeMinute >= fbAvgTime) {
            lStringBuilder.append("You did it! Well done!");
        } else {
            lStringBuilder.append("You can do it!");
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append("Only ");
            lStringBuilder.append(fbAvgTime-fbTimeMinute);
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