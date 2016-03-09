package de.tomade.saoufomat2.activity.mainGame;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


public class MainGameActivity extends Activity {

    private static final String TAG  = MainGameActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new MainGamePanel(this));
    }

    @Override
    protected void onDestroy(){
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        Log.d(TAG, "Stopping...");
        super.onStop();
    }
}
