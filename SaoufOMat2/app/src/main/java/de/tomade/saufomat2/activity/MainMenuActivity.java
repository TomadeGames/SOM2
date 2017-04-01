package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import de.tomade.saufomat2.R;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main_menu);
        this.findViewById(R.id.startButton).setOnTouchListener(new View.OnTouchListener() {
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

                        Intent intent = new Intent(MainMenuActivity.this.getApplicationContext(),
                                CreatePlayerActivity.class);
                        MainMenuActivity.this.startActivity(intent);

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

        this.findViewById(R.id.gamesButton).setOnTouchListener(new View.OnTouchListener() {
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

                        Intent intent = new Intent(MainMenuActivity.this.getApplicationContext(),
                                ChooseMiniGameActivity.class);
                        MainMenuActivity.this.startActivity(intent);

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

    private void loadGame() {
        Context context = this.getApplicationContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences(this.getString(R.string
                .preference_file_key), Context.MODE_PRIVATE);
    }

    @Override
    public void onBackPressed() {
    }
}
