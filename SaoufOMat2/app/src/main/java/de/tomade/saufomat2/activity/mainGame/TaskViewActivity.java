package de.tomade.saufomat2.activity.mainGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.task.Task;

public class TaskViewActivity extends Activity {
    private static final String TAG = TaskViewActivity.class.getSimpleName();
    private ArrayList<Player> players;
    private Task currentTask;
    private Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.players = extras.getParcelableArrayList("player");
            this.currentTask = (Task) extras.getSerializable("task");
            this.currentPlayer = Player.getPlayerById(this.players, extras.getInt("currentPlayer"));
        }

        for (Player p : players) {
            Log.d(TAG, p.getName() + " id: " + p.getId() + " next: " + p.getNextPlayerId());
        }

        TextView taskText = (TextView) this.findViewById(R.id.taskText);
        taskText.setText(this.currentTask.getText());
        int cost = this.currentTask.getCost();
        TextView costText = (TextView) this.findViewById(R.id.costText);
        if (cost <= 0) {
            costText.setVisibility(View.GONE);
            this.findViewById(R.id.noButton).setVisibility(View.GONE);
        } else {
            costText.setText("Trink " + this.currentTask.getCost());
        }

        LinearLayout playerLayout = (LinearLayout) this.findViewById(R.id.playerLayout);
        for (Player p : this.players) {
            TextView textView = new TextView(this);
            textView.setText(p.getName() + " " + p.getDrinks());
            playerLayout.addView(textView);
        }

        ((ImageButton) findViewById(R.id.yesButton)).setOnTouchListener(new View.OnTouchListener() {

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

        ((ImageButton) findViewById(R.id.noButton)).setOnTouchListener(new View.OnTouchListener() {

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

                        noButtonPressed();

                    case MotionEvent.ACTION_CANCEL: {
                        ImageButton view = (ImageButton) v;
                        view.setImageResource(R.drawable.no_button);
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void chanceToMainView() {
        Log.d(TAG, "curr: " + this.currentPlayer.getId() + " next: " + this.currentPlayer.getNextPlayerId());
        this.currentPlayer = Player.getPlayerById(this.players, this.currentPlayer.getNextPlayerId());
        Intent intent = new Intent(getApplicationContext(), MainGameActivity.class);
        intent.putExtra("currentPlayer", currentPlayer.getId());
        intent.putExtra("player", players);
        for (Player p : players) {
            Log.d(TAG, p.getName() + " drinks: " + p.getDrinks());
        }
        startActivity(intent);
    }

    private void yesButtonPressed() {
        switch (this.currentTask.getTarget()) {
            case SELF:
                this.currentPlayer.increaseDrinks(this.currentTask.getDrinkCount());
                break;
            case NEIGHBOUR:
                Player.getPlayerById(this.players, currentPlayer.getNextPlayerId()).increaseDrinks(this.currentTask.getDrinkCount());
                Player.getPlayerById(this.players, currentPlayer.getLastPlayerId()).increaseDrinks(this.currentTask.getDrinkCount());
                break;
            case CHOOSE_ONE:
                break;
            case CHOOSE_TWO:
                break;
            case CHOOSE_THREE:
                break;
            case ALL:
                for (Player p : players) {
                    p.increaseDrinks(this.currentTask.getDrinkCount());
                }
                break;
            case ALL_BUT_SELF:
                for (Player p : players) {
                    if (p.getId() != this.currentPlayer.getId()) {
                        p.increaseDrinks(this.currentTask.getDrinkCount());
                    }
                }
                break;
        }
        chanceToMainView();
    }

    private void noButtonPressed() {
        this.currentPlayer.setDrinks(this.currentPlayer.getDrinks() + this.currentTask.getCost());
        Log.d(TAG, "player: " + currentPlayer.getDrinks() + " cost: " + currentTask.getCost());
        chanceToMainView();
    }
}
