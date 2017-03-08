package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;

import de.tomade.saufomat2.R;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_menu);
        this.findViewById(R.id.startButton).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    ImageButton view = (ImageButton) v;
                    view.setImageResource(R.drawable.start_button_pressed);
                    v.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:

                    Intent intent = new Intent(this.getApplicationContext(), CreatePlayerActivity.class);
                    this.startActivity(intent);

                case MotionEvent.ACTION_CANCEL: {
                    ImageButton view = (ImageButton) v;
                    view.setImageResource(R.drawable.start_button);
                    view.invalidate();
                    break;
                }
            }
            return true;
        });

        this.findViewById(R.id.gamesButton).setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    ImageButton view = (ImageButton) v;
                    view.setImageResource(R.drawable.minigames_button_pressed);
                    v.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:

                    Intent intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
                    this.startActivity(intent);

                case MotionEvent.ACTION_CANCEL: {
                    ImageButton view = (ImageButton) v;
                    view.setImageResource(R.drawable.minigames_button);
                    view.invalidate();
                    break;
                }
            }
            return true;
        });
    }
}
