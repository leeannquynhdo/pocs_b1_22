package com.example.puk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GoToInstagram extends AppCompatActivity {

    private ProgressBar igProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_instagram);

        // create back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        long time = extras.getLong("ig_time");
        int igTimeMinute = (int) Math.round((time/1000)/60);
        int igAvg = 28;
        igProgress = (ProgressBar) findViewById(R.id.ig_progress);
        igProgress.setMax(igAvg);
        igProgress.setProgress(igTimeMinute);

        TextView lTextView = (TextView) findViewById(R.id.ig_motivator);
        StringBuilder lStringBuilder = new StringBuilder();

        lStringBuilder.append("You have spent ");
        lStringBuilder.append(igTimeMinute);
        lStringBuilder.append(" minutes out of the ideal ");
        lStringBuilder.append(igAvg);
        lStringBuilder.append(" minutes.");
        lStringBuilder.append(System.getProperty("line.separator"));
        lStringBuilder.append(System.getProperty("line.separator"));

        if (igTimeMinute >= igAvg) {
            lStringBuilder.append("You did it! Well done!");
        } else {
            lStringBuilder.append("You can do it!");
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append("Only ");
            lStringBuilder.append(igAvg-igTimeMinute);
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