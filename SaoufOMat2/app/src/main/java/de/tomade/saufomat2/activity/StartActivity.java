package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import de.tomade.saufomat2.R;

public class StartActivity extends Activity {

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
