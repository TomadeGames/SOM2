package de.tomade.saufomat2.activity.mainGame;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
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
        if (extras != null) {
            this.players = extras.getParcelableArrayList("player");
            this.currentTask = (Task) extras.getSerializable("task");
            this.currentPlayer = (Player) extras.getSerializable("currentPlayer");
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

    @Nullable
    private Player getPlayerById(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    private void chanceToMainView() {
        this.currentPlayer = getPlayerById(this.currentPlayer.getNextPlayerId());
        Intent intent = new Intent(getApplicationContext(), MainGameActivity.class);
        intent.putExtra("currentPlayer", currentPlayer);
        intent.putExtra("player", players);
        startActivity(intent);
    }

    private void yesButtonPressed() {
        switch (this.currentTask.getTarget()) {
            case SELF:
                this.currentPlayer.increaseDrinks(this.currentTask.getDrinkCount());
                break;
            case NEIGHBOUR:
                getPlayerById(currentPlayer.getNextPlayerId()).increaseDrinks(this.currentTask.getDrinkCount());
                getPlayerById(currentPlayer.getLastPlayerId()).increaseDrinks(this.currentTask.getDrinkCount());
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
        chanceToMainView();
    }
}
