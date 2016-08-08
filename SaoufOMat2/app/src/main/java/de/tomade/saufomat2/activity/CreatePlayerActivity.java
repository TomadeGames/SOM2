package de.tomade.saufomat2.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.model.Player;

public class CreatePlayerActivity extends Activity implements View.OnClickListener {
    Button btnNewPlayer = null;
    Button btnStartGame = null;
    LinearLayout linearLayout = null;
    private ArrayList<Player> players = new ArrayList<>();
    Map<Integer, View> playerelements = new HashMap<>();
    static int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);

        this.linearLayout = (LinearLayout) findViewById(R.id.llCreatePlayer);

        this.btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        this.btnStartGame = (Button) findViewById(R.id.btnStartGame);

        this.btnNewPlayer.setOnClickListener(this);
        this.btnStartGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewPlayer:
                Player newPlayer = new Player();
                showDialog(newPlayer);
                break;
            case R.id.btnStartGame:
                if (!getPlayers().isEmpty()) {
                    Intent intent = new Intent(this, MainGameActivity.class);
                    intent.putExtra("player", getPlayers());
                    intent.putExtra("currentPlayer", getPlayers().get(0).getId());
                    this.finish();
                    this.startActivity(intent);
                }else {
                    Toast.makeText(CreatePlayerActivity.this, "Keine Spieler vorhanden", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void showDialog(final Player newPlayer) {
        for(Player tmp: getPlayers()) {
            System.out.println(tmp.toString());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
        );
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_new_player_dialog, null);

        final EditText etxtName = (EditText) view.findViewById(R.id.etxtName);
        final EditText etxtWeight = (EditText) view.findViewById(R.id.etxtWeight);
        final Spinner spGender = (Spinner) view.findViewById(R.id.spGender);

        etxtName.setText("");
        etxtWeight.setText("70");

        builder.setMessage("Neuer Spieler")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newPlayer.setName(etxtName.getText().toString());
                        newPlayer.setWeight(Integer.parseInt(etxtWeight.getText().toString()));
                        boolean genderSet = false;
                        if (spGender.getSelectedItem().equals("Mann")) {
                            newPlayer.setIsMan(true);
                            genderSet = true;
                        } else if (spGender.getSelectedItem().equals("Frau")) {
                            newPlayer.setIsMan(false);
                            genderSet = true;
                        }
                        if (!newPlayer.getName().isEmpty() && newPlayer.getWeight() > 0 && genderSet) {
                            addPlayer(newPlayer);
                        } else {
                            Toast.makeText(CreatePlayerActivity.this, "Daten überprüfen!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.create();
        builder.show();
    }

    private void addPlayer(Player player) {
        boolean duplicate = false;
        for (Player tmp : getPlayers()) {
            if (tmp.getName().equals(player.getName())) {
                duplicate = true;
            }
        }
        if (!duplicate) {
            if (getPlayers() != null && !getPlayers().isEmpty()) {
                getPlayers().get(getPlayers().size() - 1).setNextPlayerId(player.getId());
            }
            this.getPlayers().add(player);
            if (getPlayers().size() > 1) {
                getPlayers().get(getPlayers().size() - 1).setLastPlayerId(getPlayers().get(getPlayers().size() - 2).getId());
            }
            for (Player tmp : getPlayers()) {
                System.out.println(tmp.toString());
            }
            displayPlayer(player);
        } else {
            Toast.makeText(CreatePlayerActivity.this, "Name bereits vorhanden!", Toast.LENGTH_LONG).show();
        }
    }

    private void displayPlayer(final Player player) {
        final int playerViewId = id;
        id++;
        final int playerId = player.getId();
        LayoutInflater inflater = this.getLayoutInflater();
        final View playerView = inflater.inflate(R.layout.player_element, null);
        playerelements.put(playerViewId, playerView);

        TextView playername = (TextView) playerView.findViewById(R.id.txtvPlayerName);
        playername.setText(player.getName());

        ImageButton delete = (ImageButton) playerView.findViewById(R.id.ibDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                linearLayout.removeView(playerView);
                playerelements.remove(playerView);
                removePlayer(player);
            }
        });

        final ImageButton edit = (ImageButton) playerView.findViewById(R.id.ibEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editPlayer(playerViewId, playerId);
            }
        });
        this.linearLayout.addView(playerView);
    }

    private void editPlayer(int playerViewId, int playerId) {
        View playerelemnt = playerelements.get(playerViewId);
        final Player player = Player.getPlayerById(getPlayers(), playerId);

        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
        );
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_new_player_dialog, null);

        final TextView txtvName = (TextView) playerelemnt.findViewById(R.id.txtvPlayerName);

        final EditText etxtName = (EditText) view.findViewById(R.id.etxtName);
        final EditText etxtWeight = (EditText) view.findViewById(R.id.etxtWeight);
        final Spinner spGender = (Spinner) view.findViewById(R.id.spGender);

        etxtName.setText(player.getName());
        etxtWeight.setText(String.valueOf(player.getWeight()));
        if (player.getIsMan()) {
            spGender.setSelection(getIndex(spGender, "Mann"));
        } else {
            spGender.setSelection(getIndex(spGender, "Frau"));
        }

        builder.setMessage("Spieler Bearbeiten")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!etxtName.getText().toString().isEmpty() && Integer.parseInt(etxtWeight.getText().toString()) > 0 && !etxtWeight.getText().toString().isEmpty()) {
                            player.setName(etxtName.getText().toString());
                            player.setWeight(Integer.parseInt(etxtWeight.getText().toString()));
                            player.setIsMan(spGender.getSelectedItem().toString().equals("Mann"));
                            txtvName.setText(player.getName());
                        } else {
                            Toast.makeText(CreatePlayerActivity.this, "Daten überprüfen!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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
        int lastPlayerId = player.getLastPlayerId();
        int nextPLayerId = player.getNextPlayerId();
        System.out.println("LastplayerId: " + lastPlayerId + " NextPlayerID: " + nextPLayerId);
        if (player.getHasNextPlayer() && player.getHastLastPlayer()) {
            Player.getPlayerById(getPlayers(), lastPlayerId).setNextPlayerId(nextPLayerId);
            Player.getPlayerById(getPlayers(), nextPLayerId).setLastPlayerId(lastPlayerId);
        } else if (player.getHasNextPlayer() && !player.getHastLastPlayer()) {
            Player.getPlayerById(getPlayers(), nextPLayerId).setLastPlayerId(-1);
            Player.getPlayerById(getPlayers(), nextPLayerId).setHasLastPlayer(false);
        }
        getPlayers().remove(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
