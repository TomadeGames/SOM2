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
import de.tomade.saufomat2.activity.mainGame.task.TaskDifficult;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.constant.MiniGame;
import de.tomade.saufomat2.model.Player;

public class TaskViewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = TaskViewActivity.class.getSimpleName();
    private ArrayList<Player> players;
    private Map<Integer, TextView> playerTexts = new HashMap<>();
    private Task currentTask;
    private MiniGame miniGame = null;
    private Player currentPlayer;
    private boolean drinkCountShown = false;
    private boolean isGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_task_view);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.players = extras.getParcelableArrayList("player");
            if (extras.containsKey("task")) {
                this.currentTask = (Task) extras.getSerializable("task");
                this.isGame = false;
            } else {
                this.miniGame = (MiniGame) extras.getSerializable("miniGame");
                this.isGame = true;
            }
            this.currentPlayer = Player.getPlayerById(this.players, extras.getInt("currentPlayer"));

        }


        TextView currentPlayerNameText = (TextView) this.findViewById(R.id.currentPlayerNameText);
        currentPlayerNameText.setText(this.currentPlayer.getName());
        TextView taskText = (TextView) this.findViewById(R.id.taskText);
        ImageButton noButton = (ImageButton) this.findViewById(R.id.declineButton);
        TextView costText = (TextView) this.findViewById(R.id.costText);
        int cost = 0;

        if (this.isGame) {
            taskText.setText(this.miniGame + "");
        } else {
            TaskDifficult difficult = this.currentTask.getDifficult();
            String taskTextValue = "";
            switch (difficult) {
                case EASY_WIN:
                case MEDIUM_WIN:
                case HARD_WIN:
                    taskTextValue = this.getString(R.string.maingame_task_jackpot);
                    break;
                default:
                    break;
            }
            taskTextValue += this.currentTask.getText();
            taskText.setText(taskTextValue);
            cost = this.currentTask.getCost();
        }

        if (cost <= 0) {
            noButton.setImageResource(R.drawable.gray_button);
            costText.setVisibility(View.INVISIBLE);
        } else {
            costText.setText(this.getString(R.string.maingame_button_decline, this.currentTask.getCost()));
            noButton.setOnClickListener(this);
        }

        LinearLayout playerLayout = (LinearLayout) this.findViewById(R.id.playerLayout);
        for (Player p : this.players) {
            TextView textView = new TextView(this);
            textView.setText(p.getName() + ": " + p.getDrinks());
            playerLayout.addView(textView);
            this.playerTexts.put(p.getId(), textView);
        }

        ImageButton yesButton = (ImageButton) this.findViewById(R.id.acceptButton);
        ImageButton optionsButton = (ImageButton) this.findViewById(R.id.optionsButton);
        ImageButton alcoholButton = (ImageButton) this.findViewById(R.id.alcoholButton);

        yesButton.setOnClickListener(this);
        optionsButton.setOnClickListener(this);
        alcoholButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acceptButton:
                this.yesButtonPressed();
                break;
            case R.id.declineButton:
                this.noButtonPressed();
                break;
            case R.id.optionsButton:
                this.optionsButtonPressed();
                break;
            case R.id.alcoholButton:
                this.alcoholButtonPressed();
                break;
        }
    }

    private void chanceToMainView() {
        Log.d(TAG, "curr: " + this.currentPlayer.getId() + " next: " + this.currentPlayer.getNextPlayerId());
        this.currentPlayer = Player.getPlayerById(this.players, this.currentPlayer.getNextPlayerId());
        Intent intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        intent.putExtra(IntentParameter.CURRENT_PLAYER_ID, this.currentPlayer.getId());
        intent.putExtra(IntentParameter.PLAYER_LIST, this.players);
        for (Player p : this.players) {
            Log.d(TAG, p.getName() + " drinks: " + p.getDrinks());
        }
        this.startActivity(intent);
    }

    private void yesButtonPressed() {
        if (this.isGame) {
            Intent intent = new Intent(this, this.miniGame.getActivity());
            intent.putExtra(IntentParameter.FROM_MAIN_GAME, true);
            intent.putParcelableArrayListExtra(IntentParameter.PLAYER_LIST, this.players);
            this.currentPlayer = Player.getPlayerById(this.players, this.currentPlayer.getNextPlayerId());
            intent.putExtra(IntentParameter.CURRENT_PLAYER_ID, this.currentPlayer.getId());
            this.startActivity(intent);
        } else {
            switch (this.currentTask.getTarget()) {
                case SELF:
                    this.currentPlayer.increaseDrinks(this.currentTask.getDrinkCount());
                    break;
                case NEIGHBOUR:
                    Player.getPlayerById(this.players, this.currentPlayer.getNextPlayerId()).increaseDrinks(this
                            .currentTask.getDrinkCount());
                    Player.getPlayerById(this.players, this.currentPlayer.getLastPlayerId()).increaseDrinks(this
                            .currentTask.getDrinkCount());
                    break;
                case CHOOSE_ONE:
                    break;
                case CHOOSE_TWO:
                    break;
                case CHOOSE_THREE:
                    break;
                case ALL:
                    this.players.forEach(player -> player.increaseDrinks(this.currentTask.getDrinkCount()));
                    break;
                case ALL_BUT_SELF:
                    this.players.stream().filter(p -> p.getId() != this.currentPlayer.getId())
                            .forEach(p -> p.increaseDrinks(this.currentTask.getDrinkCount()));
                    break;
                default:
                    break;
            }
            this.chanceToMainView();
        }
    }

    private void noButtonPressed() {
        this.currentPlayer.setDrinks(this.currentPlayer.getDrinks() + this.currentTask.getCost());
        Log.d(TAG, "player: " + this.currentPlayer.getDrinks() + " cost: " + this.currentTask.getCost());
        this.chanceToMainView();
    }


    //TODO
    private void optionsButtonPressed() {

    }

    private void alcoholButtonPressed() {
        if (this.drinkCountShown) {
            for (Map.Entry<Integer, TextView> e : this.playerTexts.entrySet()) {
                Player p = Player.getPlayerById(this.players, e.getKey());
                e.getValue().setText(p.getName() + ": " + p.getDrinks());
            }
        } else {
            for (Map.Entry<Integer, TextView> e : this.playerTexts.entrySet()) {
                Player p = Player.getPlayerById(this.players, e.getKey());
                float alc = this.calculateAlkohol(p.getDrinks(), p.getWeight(), p.getIsMan());
                String text = p.getName() + ": " + new DecimalFormat("#.##").format(alc);
                e.getValue().setText(text + "%");
            }
        }
        this.drinkCountShown = !this.drinkCountShown;
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
