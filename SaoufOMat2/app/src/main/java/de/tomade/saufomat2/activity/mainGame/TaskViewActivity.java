package de.tomade.saufomat2.activity.mainGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.MainMenuActivity;
import de.tomade.saufomat2.activity.mainGame.task.Task;
import de.tomade.saufomat2.activity.mainGame.task.TaskDifficult;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.constant.MiniGame;
import de.tomade.saufomat2.model.Player;

//TODO: Schön machen
//TODO: Optionen-Knopf hat keine Funktion
//TODO: Wenn kein Nein zur Verfügung steht: Anstatt einen ausgegrauten Button einen großen Grünen Button anzeigen
//TODO: Die aktuelle Spielerliste aktuell halten und so...
//TODO: Tasks nach dem Builder-Pattern
public class TaskViewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = TaskViewActivity.class.getSimpleName();

    private TextView statisticsText;

    private ArrayList<Player> playerList;
    private Task currentTask;
    private MiniGame miniGame = null;
    private Player currentPlayer;
    private boolean drinkCountShown = false;
    private boolean isGame;
    private int width;
    private int height;

    private boolean currentPlayerIsAvaible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_task_view);

        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        this.width = size.x;
        this.height = size.y;

        Bundle extras = this.getIntent().getExtras();

        this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);
        this.currentPlayer = (Player) extras.getSerializable(IntentParameter.CURRENT_PLAYER);

        if (!extras.getBoolean(IntentParameter.MainGame.CURRENT_TASK_IS_MINI_GAME)) {
            this.currentTask = (Task) extras.getSerializable(IntentParameter.MainGame.CURRENT_TASK);
            this.isGame = false;
        } else {
            this.miniGame = (MiniGame) extras.getSerializable(IntentParameter.MainGame.CURRENT_MINI_GAME);
            this.isGame = true;
        }

        this.statisticsText = (TextView) this.findViewById(R.id.statisticText);
        TextView currentPlayerNameText = (TextView) this.findViewById(R.id.currentPlayerNameText);
        currentPlayerNameText.setText(this.currentPlayer.getName());
        TextView taskText = (TextView) this.findViewById(R.id.taskText);
        ImageButton noButton = (ImageButton) this.findViewById(R.id.declineButton);
        TextView costText = (TextView) this.findViewById(R.id.costText);
        int cost = 0;

        if (this.isGame) {
            taskText.setText(this.getString(this.miniGame.getNameId()));
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

        this.alcoholButtonPressed();

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

    private void openMainView() {
        Log.d(TAG, "curr: " + this.currentPlayer.getName() + " next: " + this.currentPlayer.getNextPlayer().getName());
        this.currentPlayer = this.currentPlayer.getNextPlayer();
        Intent intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
        intent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
        for (Player p : this.playerList) {
            Log.d(TAG, p.getName() + " drinks: " + p.getDrinks());
        }
        this.startActivity(intent);
    }

    private void yesButtonPressed() {
        if (this.isGame) {
            Intent intent = new Intent(this, this.miniGame.getActivity());
            intent.putExtra(IntentParameter.FROM_MAIN_GAME, true);
            intent.putExtra(IntentParameter.PLAYER_LIST, this.playerList);
            this.currentPlayer = this.currentPlayer.getNextPlayer();
            intent.putExtra(IntentParameter.CURRENT_PLAYER, this.currentPlayer);
            this.startActivity(intent);
        } else {
            if (this.currentPlayerIsAvaible) {
                switch (this.currentTask.getTarget()) {
                    case SELF:
                        this.currentPlayer.increaseDrinks(this.currentTask.getDrinkCount());
                        break;
                    case NEIGHBOUR:
                        this.currentPlayer.getNextPlayer().increaseDrinks(this.currentTask.getDrinkCount());
                        this.currentPlayer.getLastPlayer().increaseDrinks(this.currentTask.getDrinkCount());
                        break;
                    case CHOOSE_ONE:
                        break;
                    case CHOOSE_TWO:
                        break;
                    case CHOOSE_THREE:
                        break;
                    case ALL:
                        for (Player player : this.playerList) {
                            player.increaseDrinks(TaskViewActivity.this.currentTask.getDrinkCount());
                        }
                        break;
                    case ALL_BUT_SELF:
                        for (Player player : this.playerList) {
                            if (player.getId() != this.currentPlayer.getId()) {
                                player.increaseDrinks(this.currentTask.getDrinkCount());
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            this.openMainView();
        }
    }

    private void noButtonPressed() {
        if (this.currentPlayerIsAvaible) {
            this.currentPlayer.setDrinks(this.currentPlayer.getDrinks() + this.currentTask.getCost());
        }
        this.openMainView();
    }


    //TODO
    private void optionsButtonPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault)
        );
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.maingame_options_dialog, null);

        builder.setView(view);
        ImageButton abortButton = (ImageButton) view.findViewById(R.id.abortButton);
        ImageButton acceptButton = (ImageButton) view.findViewById(R.id.acceptButton);
        ImageButton closeButton = (ImageButton) view.findViewById(R.id.closeButton);
        ImageButton addPlayerButton = (ImageButton) view.findViewById(R.id.addPlayerButton);
        ImageButton removePlayerButton = (ImageButton) view.findViewById(R.id.removePlayerButton);


        AlertDialog dialog = builder.create();
        OptionsHandler optionsHandler = new OptionsHandler(this, dialog);
        abortButton.setOnClickListener(optionsHandler);
        acceptButton.setOnClickListener(optionsHandler);
        closeButton.setOnClickListener(optionsHandler);
        addPlayerButton.setOnClickListener(optionsHandler);
        removePlayerButton.setOnClickListener(optionsHandler);
        dialog.show();
        dialog.getWindow().setLayout(this.width, this.height);
    }

    private void alcoholButtonPressed() {
        this.drinkCountShown = !this.drinkCountShown;
        Player player = this.currentPlayer;
        String statisticValue = "";
        do {
            String playerText;
            if (this.drinkCountShown) {
                playerText = player.getName() + ": " + player.getDrinks();
            } else {
                float alc = this.calculateAlcohol(player.getDrinks(), player.getWeight(), player.getIsMan());
                playerText = player.getName() + ": " + new DecimalFormat("#.##").format(alc) + "%";
            }
            statisticValue += playerText + "\n";
            player = player.getNextPlayer();

        } while (player != this.currentPlayer);

        this.statisticsText.setText(statisticValue);
    }

    private float calculateAlcohol(int drinks, int weight, boolean isMan) {
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

    public void closeGame() {
        Intent intent = new Intent(this.getApplicationContext(), MainMenuActivity.class);
        this.startActivity(intent);
    }

    public void setPlayerList(Player player) {
        this.playerList.clear();
        Player firstPlayer = player;
        do {
            this.playerList.add(player);
            player = player.getNextPlayer();
        } while (player != firstPlayer);

        this.drinkCountShown = !this.drinkCountShown;
        this.alcoholButtonPressed();
    }

    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void nextPlayerFromOptions() {
        this.currentPlayer = this.currentPlayer.getNextPlayer();
        this.currentPlayerIsAvaible = false;
    }


    @Override
    public void onBackPressed() {
    }
}
