package com.tomade.saufomat.activity.miniGame;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.constant.IntentParameter;

/**
 * Dialog zum Anzeigen einer Anleitung eines Minispiels
 * Created by woors on 08.10.2017.
 */

public class TutorialDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_tutorial);

        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TextView textView = this.findViewById(R.id.tutorialText);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            textView.setText(extras.getInt(IntentParameter.Tutorial.TEXT_ID));
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.finish();
        return super.onTouchEvent(event);
    }
}
