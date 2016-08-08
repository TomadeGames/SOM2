package de.tomade.saufomat2.activity.miniGames.memory;

import android.graphics.Canvas;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.threading.ThreadedView;

public class MemoryActivity extends Activity {
    private static final String TAG  = MemoryActivity.class.getSimpleName();

    private String avgFps;
    private long elapsedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            ArrayList<Player> players = extras.getParcelableArrayList("player");
            int currentPlayer = extras.getInt("currentPlayer");
        }
        setContentView(new MemoryPanel(this));
    }
}
