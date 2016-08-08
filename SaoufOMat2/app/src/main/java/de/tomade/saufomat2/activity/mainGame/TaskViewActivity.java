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
import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.model.Player;

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
            this.setCurrentTask((Task) extras.getSerializable("task"));
            this.setCurrentPlayer(Player.getPlayerById(this.getPlayers(), extras.getInt("currentPlayer")));

        }


        TextView currentPlayerNameText = (TextView) this.findViewById(R.id.currentPlayerNameText);
        currentPlayerNameText.setText(this.getCurrentPlayer().getName());
        TextView taskText = (TextView) this.findViewById(R.id.taskText);
        taskText.setText(this.getCurrentTask().getText());
        int cost = this.getCurrentTask().getCost();

        ImageButton noButton = (ImageButton) this.findViewById(R.id.noButton);
        TextView costText = (TextView) this.findViewById(R.id.costText);
        if (cost <= 0) {
            noButton.setImageResource(R.drawable.gray_button);
            costText.setVisibility(View.INVISIBLE);
        } else {
            costText.setText("Trink " + this.getCurrentTask().getCost());
            noButton.setOnClickListener(this);
        }

        LinearLayout playerLayout = (LinearLayout) this.findViewById(R.id.playerLayout);
        for (Player p : this.getPlayers()) {
            TextView textView = new TextView(this);
            textView.setText(p.getName() + ": " + p.getDrinks());
            playerLayout.addView(textView);
            this.getPlayerTexts().put(p.getId(), textView);
        }

        ImageButton yesButton = (ImageButton) findViewById(R.id.yesButton);
        ImageButton optionsButton = (ImageButton) findViewById(R.id.optionsButton);
        ImageButton alcoholButton = (ImageButton) findViewById(R.id.alcoholButton);

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
        Log.d(TAG, "curr: " + this.getCurrentPlayer().getId() + " next: " + this.getCurrentPlayer().getNextPlayerId());
        this.setCurrentPlayer(Player.getPlayerById(this.getPlayers(), this.getCurrentPlayer().getNextPlayerId()));
        Intent intent = new Intent(getApplicationContext(), MainGameActivity.class);
        intent.putExtra("currentPlayer", getCurrentPlayer().getId());
        intent.putExtra("player", getPlayers());
        for (Player p : getPlayers()) {
            Log.d(TAG, p.getName() + " drinks: " + p.getDrinks());
        }
        startActivity(intent);
    }

    private void yesButtonPressed() {
        switch (this.getCurrentTask().getTarget()) {
            case SELF:
                this.getCurrentPlayer().increaseDrinks(this.getCurrentTask().getDrinkCount());
                break;
            case NEIGHBOUR:
                Player.getPlayerById(this.getPlayers(), getCurrentPlayer().getNextPlayerId()).increaseDrinks(this.getCurrentTask().getDrinkCount());
                Player.getPlayerById(this.getPlayers(), getCurrentPlayer().getLastPlayerId()).increaseDrinks(this.getCurrentTask().getDrinkCount());
                break;
            case CHOOSE_ONE:
                break;
            case CHOOSE_TWO:
                break;
            case CHOOSE_THREE:
                break;
            case ALL:
                for (Player p : getPlayers()) {
                    p.increaseDrinks(this.getCurrentTask().getDrinkCount());
                }
                break;
            case ALL_BUT_SELF:
                for (Player p : getPlayers()) {
                    if (p.getId() != this.getCurrentPlayer().getId()) {
                        p.increaseDrinks(this.getCurrentTask().getDrinkCount());
                    }
                }
                break;
        }
        chanceToMainView();
    }

    private void noButtonPressed() {
        this.getCurrentPlayer().setDrinks(this.getCurrentPlayer().getDrinks() + this.getCurrentTask().getCost());
        Log.d(TAG, "player: " + getCurrentPlayer().getDrinks() + " cost: " + getCurrentTask().getCost());
        chanceToMainView();
    }


    //TODO
    private void optionsButtonPressed() {

    }

    private void alcoholButtonPressed() {
        if (isDrinkCountShown()) {
            for (Map.Entry<Integer, TextView> e : this.getPlayerTexts().entrySet()) {
                Player p = Player.getPlayerById(this.getPlayers(), e.getKey());
                e.getValue().setText(p.getName() + ": " + p.getDrinks());
            }
        } else {
            for (Map.Entry<Integer, TextView> e : this.getPlayerTexts().entrySet()) {
                Player p = Player.getPlayerById(this.getPlayers(), e.getKey());
                float alc = calculateAlkohol(p.getDrinks(), p.getWeight(), p.getIsMan());
                String text = p.getName() + ": " + new DecimalFormat("#.##").format(alc);
                e.getValue().setText(text + "%");
            }
        }
        setDrinkCountShown(!isDrinkCountShown());
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public Map<Integer, TextView> getPlayerTexts() {
        return playerTexts;
    }

    public void setPlayerTexts(Map<Integer, TextView> playerTexts) {
        this.playerTexts = playerTexts;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isDrinkCountShown() {
        return drinkCountShown;
    }

    public void setDrinkCountShown(boolean drinkCountShown) {
        this.drinkCountShown = drinkCountShown;
    }
}
