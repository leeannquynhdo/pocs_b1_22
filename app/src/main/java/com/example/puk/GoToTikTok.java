package com.example.puk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class GoToTikTok extends AppCompatActivity {

    private ProgressBar ttProgress;
    private int ttProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_to_tik_tok);

        // create back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        long time = extras.getLong("tt_time");
        int ttTimeMinute = (int) Math.round((time/1000)/60);
        int ttAvg = 31;
        ttProgress = (ProgressBar) findViewById(R.id.tt_progress);
        // multiplying by three to account maxval = 100
        ttProgress.setMax(31);
        ttProgress.setProgress(ttTimeMinute);

        int[] images = {R.drawable.img01,
                R.drawable.img02,
                R.drawable.img03,
                R.drawable.img04,
                R.drawable.img05,
                R.drawable.img06,
                R.drawable.img07,
                R.drawable.img08,
                R.drawable.img09,
                R.drawable.img10,
                R.drawable.img11,
                R.drawable.img12,
                R.drawable.img13,
                R.drawable.img14,
                R.drawable.img15,
                R.drawable.img16,
                R.drawable.img17,
                R.drawable.img18,
                R.drawable.img19,
                R.drawable.img20};
        Random rand = new Random();

        TextView lTextView = (TextView) findViewById(R.id.tt_motivator);
        StringBuilder lStringBuilder = new StringBuilder();

        lStringBuilder.append("You have spent ");
        lStringBuilder.append(ttTimeMinute);
        lStringBuilder.append(" minutes out of the ideal ");
        lStringBuilder.append(ttAvg);
        lStringBuilder.append(" minutes.");
        lStringBuilder.append(System.getProperty("line.separator"));
        lStringBuilder.append(System.getProperty("line.separator"));

        if (ttTimeMinute >= ttAvg) {
            lStringBuilder.append("You did it! Well done!");
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append("You have unlocked the hidden image");
            ImageView lImageView = (ImageView) findViewById(R.id.tt_image);
            lImageView.setImageResource(images[rand.nextInt(images.length)]);
        } else {
            lStringBuilder.append("You can do it!");
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append("Only ");
            lStringBuilder.append(ttAvg-ttTimeMinute);
            lStringBuilder.append(" minutes to go");
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append(System.getProperty("line.separator"));
            lStringBuilder.append("Reach the daily goal to unveil the hidden image!");
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