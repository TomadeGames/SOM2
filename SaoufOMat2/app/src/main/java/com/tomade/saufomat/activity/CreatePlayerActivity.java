package com.tomade.saufomat.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.tomade.saufomat.activity.mainGame.MainGameActivity;
import com.tomade.saufomat.activity.mainManue.MainMenuActivity;
import com.tomade.saufomat.constant.IntentParameter;
import com.tomade.saufomat.model.player.Player;
import com.tomade.saufomat.persistance.SaveGameHelper;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatePlayerActivity extends Activity implements View.OnClickListener {
    private static final String TAG = CreatePlayerActivity.class.getSimpleName();
    static int id = 0;
    LinearLayout linearLayout = null;
    Map<Integer, View> playerelements = new HashMap<>();
    private ArrayList<Player> players = new ArrayList<>();
    private boolean gameStarting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_create_player);

        this.linearLayout = this.findViewById(R.id.llCreatePlayer);

        ImageButton btnNewPlayer = this.findViewById(R.id.btnNewPlayer);
        ImageButton btnStartGame = this.findViewById(R.id.btnStartGame);
        ImageButton btnBack = this.findViewById(R.id.btnBack);

        btnNewPlayer.setOnClickListener(this);
        btnStartGame.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                Intent intent = new Intent(this.getApplicationContext(), MainMenuActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btnNewPlayer:
                Player newPlayer = new Player();
                this.showDialog(newPlayer);
                break;
            case R.id.etxtWeight:
                EditText textView = (EditText) v;
                textView.setText("");
                break;
            case R.id.btnStartGame:
                if (!this.players.isEmpty() && !this.gameStarting) {
                    this.gameStarting = true;
                    this.setPlayerOrder();
                    this.trimPlayerNames();

                    DatabaseHelper databaseHelper = new DatabaseHelper(this);
                    databaseHelper.startNewGame();

                    for (Player player : this.players) {
                        databaseHelper.insertPlayer(player);
                    }
                    SaveGameHelper saveGameHelper = new SaveGameHelper(this);
                    saveGameHelper.deleteSaveGame();

                    this.changeToMainGame();
                } else {
                    Toast.makeText(CreatePlayerActivity.this, R.string.create_player_no_player, Toast.LENGTH_LONG)
                            .show();
                }
                break;
        }
    }

    private void changeToMainGame() {
        Intent intent = new Intent(this, MainGameActivity.class);
        intent.putExtra(IntentParameter.PLAYER_LIST, this.players);
        intent.putExtra(IntentParameter.CURRENT_PLAYER, this.players.get(0));
        intent.putExtra(IntentParameter.MainGame.NEW_GAME, true);

        this.finish();
        this.startActivity(intent);
    }

    private void setPlayerOrder() {
        for (int i = 0; i < this.players.size(); i++) {
            Player player = this.players.get(i);
            if (this.players.size() > i + 1) {
                player.setNextPlayer(this.players.get(i + 1));
            } else {
                player.setNextPlayer(this.players.get(0));
            }
            if (i == 0) {
                player.setLastPlayer(this.players.get(this.players.size() - 1));
            } else {
                player.setLastPlayer(this.players.get(i - 1));
            }
        }
    }

    private void trimPlayerNames() {
        for (Player player : this.players) {
            player.setName(player.getName().trim());
        }
    }

    private void showDialog(final Player newPlayer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault)
        );
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_new_player_dialog, null);

        final EditText etxtName = view.findViewById(R.id.etxtName);
        final EditText etxtWeight = view.findViewById(R.id.etxtWeight);
        final Spinner spGender = view.findViewById(R.id.spGender);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout
                .simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(arrayAdapter);

        etxtName.setText("");
        etxtWeight.setText("");

        builder.setMessage(R.string.create_player_new_player)
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (PlayerService.fillPlayerData(newPlayer, etxtName.getText().toString(),
                                etxtWeight.getText().toString(), spGender.getSelectedItem().toString())) {
                            addPlayer(newPlayer);
                        } else {
                            Toast.makeText(CreatePlayerActivity.this, R.string.create_player_check_data, Toast
                                    .LENGTH_SHORT).show();
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

    private void addPlayer(Player player) {
        boolean duplicate = false;
        for (Player tmp : this.players) {
            if (tmp.getName().equals(player.getName())) {
                duplicate = true;
            }
        }
        if (!duplicate) {
            this.players.add(player);
            this.displayPlayer(player);
        } else {
            Toast.makeText(CreatePlayerActivity.this, R.string.create_player_name_already_taken, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void displayPlayer(final Player player) {
        final int playerViewId = id;
        id++;
        final int playerId = player.getId();
        LayoutInflater inflater = this.getLayoutInflater();
        final View playerView = inflater.inflate(R.layout.create_player_element, null);
        this.playerelements.put(playerViewId, playerView);

        TextView playername = playerView.findViewById(R.id.txtvPlayerName);
        playername.setText(player.getName());

        ImageButton delete = playerView.findViewById(R.id.ibDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePlayerActivity.this.linearLayout.removeView(playerView);
                CreatePlayerActivity.this.playerelements.remove(playerView);
                CreatePlayerActivity.this.removePlayer(player);
            }
        });

        final ImageButton edit = playerView.findViewById(R.id.ibEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePlayerActivity.this.editPlayer(playerViewId, playerId);
            }
        });
        this.linearLayout.addView(playerView);
    }

    private void editPlayer(int playerViewId, int playerId) {
        View playerElemnt = this.playerelements.get(playerViewId);
        final Player player = Player.getPlayerById(this.players, playerId);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault)
        );
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_new_player_dialog, null);

        final TextView txtvName = playerElemnt.findViewById(R.id.txtvPlayerName);

        final EditText etxtName = view.findViewById(R.id.etxtName);
        final EditText etxtWeight = view.findViewById(R.id.etxtWeight);
        final Spinner spGender = view.findViewById(R.id.spGender);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout
                .simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(arrayAdapter);

        etxtName.setText(player.getName());
        etxtWeight.setText(String.valueOf(player.getWeight()));
        if (player.getIsMan()) {
            spGender.setSelection(this.getIndex(spGender, this.getString(R.string.enum_gender_man)));
        } else {
            spGender.setSelection(this.getIndex(spGender, this.getString(R.string.enum_gender_woman)));
        }
        etxtWeight.setOnClickListener(this);

        builder.setMessage(R.string.create_player_edit_player)
                .setView(view)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id12) {
                        boolean duplicate = false;
                        for (Player tmp : CreatePlayerActivity.this.players) {
                            if (tmp != player) {
                                if (tmp.getName().equals(etxtName.getText().toString())) {
                                    duplicate = true;
                                }
                            }
                        }
                        if (!duplicate) {
                            if (!etxtName.getText().toString().isEmpty() && Integer.parseInt(etxtWeight.getText()
                                    .toString()) > 0 && !etxtWeight.getText().toString().isEmpty()) {
                                player.setName(etxtName.getText().toString());
                                player.setWeight(Integer.parseInt(etxtWeight.getText().toString()));
                                player.setIsMan(spGender.getSelectedItem().toString().equals("Mann"));
                                txtvName.setText(player.getName());
                            } else {
                                Toast.makeText(CreatePlayerActivity.this, R.string.create_player_check_data, Toast
                                        .LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            Toast.makeText(CreatePlayerActivity.this, R.string.create_player_name_already_taken, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(R.string.button_abort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id1) {
                    }
                });
        builder.create();
        builder.show();
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void removePlayer(Player player) {
        this.players.remove(player);
    }
}
