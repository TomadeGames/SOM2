package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import de.tomade.saufomat2.R;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        findViewById(R.id.startButton).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.start_button_pressed);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        Intent intent = new Intent(getApplicationContext(), CreatePlayerActivity.class);
                        startActivity(intent);

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.start_button);
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });

        findViewById(R.id.gamesButton).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.minigames_button_pressed);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        Intent intent = new Intent(getApplicationContext(), ChooseMiniGameActivity.class);
                        startActivity(intent);

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.minigames_button);
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }
}
