package com.tomade.saufomat.activity.mainGame;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tomade.saufomat.PlayerService;
import com.tomade.saufomat.R;
import com.tomade.saufomat.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasse, die die Events des Optionen-Feldes verwaltet
 * Created by woors on 30.03.2017.
 */

public class OptionsHandler implements View.OnClickListener {

    private AlertDialog optionsDialog;
    private TaskViewActivity taskViewActivity;
    private ArrayList<Player> playerList;

    public OptionsHandler(TaskViewActivity taskViewActivity, AlertDialog optionsDialog) {
        this.optionsDialog = optionsDialog;
        this.taskViewActivity = taskViewActivity;
        this.playerList = new ArrayList<>();
        this.playerList.addAll(taskViewActivity.getPlayerList());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.abortButton:
                this.closeOptions();
                break;
            case R.id.acceptButton:
                this.acceptOptions();
                break;
            case R.id.closeButton:
                this.closeGame();
                break;
            case R.id.addPlayerButton:
                this.addPlayer();
                break;
            case R.id.removePlayerButton:
                this.removePlayerClicked();
                break;
        }
    }

    private void closeOptions() {
        this.optionsDialog.cancel();
    }

    private void closeGame() {
        this.taskViewActivity.closeGame();
    }

    private void acceptOptions() {
        this.closeOptions();
    }

    private void addPlayer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this.taskViewActivity, android.R.style.Theme_DeviceDefault)
        );
        final LayoutInflater inflater = this.taskViewActivity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_new_player_dialog, null);
        final EditText etxtName = view.findViewById(R.id.etxtName);
        final EditText etxtWeight = view.findViewById(R.id.etxtWeight);
        final Spinner spGender = view.findViewById(R.id.spGender);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this.taskViewActivity, R.array.gender, android.R
                .layout
                .simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(arrayAdapter);

        etxtName.setText("");
        etxtWeight.setText(R.string.create_player_activity_weight_default);

        final Player newPlayer = new Player();

        builder.setMessage(R.string.create_player_new_player)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (PlayerService.fillPlayerData(newPlayer, etxtName.getText().toString(), etxtWeight.getText
                                ().toString(), spGender.getSelectedItem().toString())) {
                            addPlayer(newPlayer);
                        } else {
                            Toast.makeText(OptionsHandler.this.taskViewActivity, R.string.create_player_check_data,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.button_abort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create();
        builder.show();
    }

    private void removePlayerClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this.taskViewActivity, android.R.style.Theme_DeviceDefault)
        );
        LayoutInflater inflater = this.taskViewActivity.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_choose_players, null);

        LinearLayout playerContainer = view.findViewById(R.id.playerContainer);
        ImageButton acceptButton = view.findViewById(R.id.acceptButton);

        final List<Player> selectedPlayers = new ArrayList<>();
        this.initRemovePlayerPlayerContainer(playerContainer, selectedPlayers);


        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPlayers.size() == OptionsHandler.this.playerList.size()) {
                    Toast.makeText(OptionsHandler.this.taskViewActivity.getApplicationContext(), R.string
                                    .maingame_options_remove_player_error_to_many_to_remove,
                            Toast.LENGTH_SHORT).show();
                } else {
                    removePlayer(selectedPlayers, dialog);
                }
            }
        });
    }

    private void removePlayer(List<Player> selectedPlayers, AlertDialog dialog) {
        Player firstPlayer = OptionsHandler.this.taskViewActivity.getCurrentPlayer();
        Player player = firstPlayer;
        Player playerFound = null;

        for (int i = 0; i < selectedPlayers.size(); i++) {
            do {
                for (Player playerToRemove : selectedPlayers) {
                    if (playerToRemove.getId() == player.getId()) {
                        playerFound = player;
                    }
                }
                if (playerFound != null) {
                    selectedPlayers.remove(playerFound);
                    Player playerToRemove = null;
                    for (Player playerInList : OptionsHandler.this.playerList) {
                        if (playerInList.getId() == playerFound.getId()) {
                            playerToRemove = playerInList;
                        }
                    }
                    if (player.equals(firstPlayer)) {
                        OptionsHandler.this.taskViewActivity.nextPlayerFromOptions();
                        firstPlayer = player.getNextPlayer();
                    }
                    Player lastPlayer = player.getLastPlayer();
                    Player nextPlayer = player.getNextPlayer();
                    lastPlayer.setNextPlayer(nextPlayer);
                    nextPlayer.setLastPlayer(lastPlayer);
                    OptionsHandler.this.playerList.remove(playerToRemove);
                    playerFound = null;
                }
                player = player.getNextPlayer();
            } while (player != firstPlayer);
        }

        OptionsHandler.this.taskViewActivity.setPlayerList(player);
        dialog.cancel();
    }

    private void initRemovePlayerPlayerContainer(LinearLayout playerContainer, final List<Player> selectedPlayers) {
        for (final Player player : this.playerList) {
            LayoutInflater inflater = this.taskViewActivity.getLayoutInflater();
            final View playerView = inflater.inflate(R.layout.display_player_element, null);

            TextView playerNameText = playerView.findViewById(R.id.playerNameText);
            final ImageButton playerButton = playerView.findViewById(R.id.playerButton);

            playerNameText.setText(player.getName());
            playerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (selectedPlayers.contains(player)) {
                        selectedPlayers.remove(player);
                        playerButton.setImageResource(R.drawable.busfahren_higher_button);
                    } else {
                        playerButton.setImageResource(R.drawable.busfahren_lower_button);
                        selectedPlayers.add(player);
                    }
                }
            });

            playerContainer.addView(playerView);
        }
    }

    private void addPlayer(Player player) {
        boolean duplicate = false;
        for (Player tmp : this.playerList) {
            if (tmp.getName().equals(player.getName())) {
                duplicate = true;
            }
        }
        if (!duplicate) {
            this.playerList.add(player);
            Player currentPlayer = this.taskViewActivity.getCurrentPlayer();
            Player lastPlayer = currentPlayer.getLastPlayer();
            player.setNextPlayer(currentPlayer);
            player.setLastPlayer(lastPlayer);
            currentPlayer.setLastPlayer(player);
            lastPlayer.setNextPlayer(player);
            this.taskViewActivity.setPlayerList(player);
        } else {
            Toast.makeText(this.taskViewActivity, R.string.create_player_name_already_taken, Toast.LENGTH_LONG).show();
        }
    }
}
