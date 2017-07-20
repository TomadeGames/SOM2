package com.tomade.saufomat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGame.ichHabNochNie.IchHabNochNieTasks;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {
    private void checkIchHabNochNieDuplicates() {
        List<String> lastTasks = new ArrayList<>();
        List<String> duplicates = new ArrayList<>();

        String[] tasks = IchHabNochNieTasks.TASKS;
        for (String task : tasks) {
            if (lastTasks.contains(task)) {
                duplicates.add(task);
            }
            lastTasks.add(task);
        }
        if (!duplicates.isEmpty()) {
            String duplicatesString = "";
            for (String duplicate : duplicates) {
                duplicatesString += duplicate + ";";
            }
            throw new IllegalStateException("duplicates: " + duplicatesString);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_start);

        final ImageView backgroundImage = (ImageView) this.findViewById(R.id.backgroundImage);
        final TextView warningText = (TextView) this.findViewById(R.id.warningText);

        final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (this.counter == 0) {
                    backgroundImage.setVisibility(View.GONE);
                    warningText.setVisibility(View.VISIBLE);
                    this.counter++;
                    mHandler.postDelayed(this, 2000);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }

        }, 2500);
    }
}
