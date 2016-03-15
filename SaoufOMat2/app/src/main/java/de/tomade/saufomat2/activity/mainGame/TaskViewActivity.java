package de.tomade.saufomat2.activity.mainGame;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.task.Task;

public class TaskViewActivity extends Activity {
    private ArrayList<Player> players;
    private Task currentTask;
    private Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null){
            this.players = extras.getParcelableArrayList("player");
            this.currentTask = (Task) extras.getSerializable("task");
            this.currentPlayer = (Player) extras.getSerializable("currentPlayer");
        }

        TextView taskText = (TextView)this.findViewById(R.id.taskText);
        taskText.setText(this.currentTask.getText());
        int cost = this.currentTask.getCost();
        TextView costText = (TextView) this.findViewById(R.id.costText);
        if(cost <= 0){
            costText.setVisibility(View.GONE);
            this.findViewById(R.id.noButton).setVisibility(View.GONE);
        }
        else {
            costText.setText("Trink " + this.currentTask.getCost());
        }

        ((ImageButton)findViewById(R.id.yesButton)).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageButton view = (ImageButton) v;
                        //TODO: pressedButtonImage
                        //view.setImageResource(R.drawable.[pressedButton]);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:

                        yesButtonPressed();

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.check_button);
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void yesButtonPressed(){
        Intent intent = new Intent(getApplicationContext(), MainGameActivity.class);
        startActivity(intent);
    }
}
