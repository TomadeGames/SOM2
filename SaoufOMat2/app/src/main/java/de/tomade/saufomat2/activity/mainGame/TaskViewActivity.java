package de.tomade.saufomat2.activity.mainGame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.model.Player;
import de.tomade.saufomat2.model.task.Task;

public class TaskViewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = TaskViewActivity.class.getSimpleName();
    private ArrayList<Player> players;
    private Map<Integer, TextView> playerTexts = new HashMap<>();
    private Task currentTask;
    private Player currentPlayer;
    private boolean drinkCountShown = false;

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
        TextView currentPlayerNameText = (TextView) this.findViewById(R.id.currentPlayerNameText);
        currentPlayerNameText.setText(this.currentPlayer.getName());
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
            textView.setText(p.getName() + ": " + p.getDrinks());
            playerLayout.addView(textView);
            this.playerTexts.put(p.getId(), textView);
        }

        ImageButton noButton = (ImageButton) findViewById(R.id.noButton);
        ImageButton yesButton = (ImageButton) findViewById(R.id.yesButton);
        ImageButton optionsButton = (ImageButton) findViewById(R.id.optionsButton);
        ImageButton alcoholButton = (ImageButton) findViewById(R.id.alcoholButton);

        noButton.setOnClickListener(this);
        yesButton.setOnClickListener(this);
        optionsButton.setOnClickListener(this);
        alcoholButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yesButton:
                yesButtonPressed();
                break;
            case R.id.noButton:
                noButtonPressed();
                break;
            case R.id.optionsButton:
                optionsButtonPressed();
                break;
            case R.id.alcoholButton:
                alcoholButtonPressed();
                break;
        }
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


    //TODO
    private void optionsButtonPressed() {

    }

    private void alcoholButtonPressed() {
        if (drinkCountShown) {
            for (Map.Entry<Integer, TextView> e : this.playerTexts.entrySet()) {
                Player p = Player.getPlayerById(this.players, e.getKey());
                e.getValue().setText(p.getName() + ": " + p.getDrinks());
            }
        } else {
            for (Map.Entry<Integer, TextView> e : this.playerTexts.entrySet()) {
                Player p = Player.getPlayerById(this.players, e.getKey());
                float alc = calculateAlkohol(p.getDrinks(), p.getWeight(), p.getIsMan());
                String text = p.getName() + ": " + new DecimalFormat("#.##").format(alc);
                e.getValue().setText(text + "%");
            }
        }
        drinkCountShown = !drinkCountShown;
    }

    private float calculateAlkohol(int drinks, int weight, boolean isMan) {
        float alcPercent = 0.18f;
        int amount = 20;
        float alc = amount * alcPercent * drinks * 0.81f;

        float reducedWight;
        if (isMan) {
            reducedWight = weight * 0.7f;
        } else {
            reducedWight = weight * 0.6f;
        }
        float erg = alc / reducedWight;
        erg -= erg * 0.2f;
        return erg;
    }
}
