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

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.model.Player;


//TODO:
//Wenn kein Spieler erstellt wurde und man auf Start drückt, stürzt das Spiel ab
public class CreatePlayerActivity extends Activity implements View.OnClickListener {
    Button btnNewPlayer = null;
    Button btnStartGame = null;
    LinearLayout linearLayout = null;
    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<View> playerelements = new ArrayList<View>();

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
                Intent intent = new Intent(this, MainGameActivity.class);
                intent.putExtra("player", players);
                intent.putExtra("currentPlayer", players.get(0).getId());
                this.finish();
                this.startActivity(intent);
        }
    }

    public void showDialog(final Player newPlayer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
        );
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_new_player_dialog, null);

        final EditText etxtName = (EditText) view.findViewById(R.id.etxtName);
        final EditText etxtWeight = (EditText) view.findViewById(R.id.etxtWeight);
        final Spinner spGender = (Spinner) view.findViewById(R.id.spGender);

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

    public void addPlayer(Player player) {
        boolean duplicate = false;
        for (Player tmp : players) {
            if (tmp.getName().equals(player.getName())) {
                duplicate = true;
            }
        }
        if (!duplicate) {
            if (players != null && !players.isEmpty()) {
                players.get(players.size()-1).setNextPlayerId(player.getId());
            }
            this.players.add(player);
            if(players.size() > 1){
                players.get(players.size()-1).setLastPlayerId(players.get(players.size()-2).getId());
            }
            for(Player tmp : players){
                System.out.println(tmp.toString());
            }
            displayPlayer(player);
        } else {
            Toast.makeText(CreatePlayerActivity.this, "Name bereits vorhanden!", Toast.LENGTH_LONG).show();
        }
    }

    public void displayPlayer(final Player player) {
        LayoutInflater inflater = this.getLayoutInflater();
        final View playerView = inflater.inflate(R.layout.player_element, null);
        playerelements.add(playerView);

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
                editPlayer(playerView);
            }
        });

        this.linearLayout.addView(playerView);
    }

    public void editPlayer(View playerelemnt) {

        //TODO überarbeiten
        AlertDialog.Builder builder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog)
        );
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activity_new_player_dialog, null);

        final TextView txtvName = (TextView) playerelemnt.findViewById(R.id.txtvPlayerName);

        final EditText etxtName = (EditText) view.findViewById(R.id.etxtName);
        final EditText etxtWeight = (EditText) view.findViewById(R.id.etxtWeight);
        final Spinner spGender = (Spinner) view.findViewById(R.id.spGender);

        Player player = null;
        for (Player tmp : players) {
            if (tmp.getName().equals(txtvName.getText().toString())) ;
            {
                player = tmp;
            }
        }

        etxtName.setText(player.getName());
        etxtWeight.setText(String.valueOf(player.getWeight()));
        if (player.getIsMan()) {
            spGender.setSelection(getIndex(spGender, "Mann"));
        } else {
            spGender.setSelection(getIndex(spGender, "Frau"));
        }


        final Player finalPlayer = player;
        builder.setMessage("Spieler Bearbeiten")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!etxtName.getText().toString().isEmpty() && Integer.parseInt(etxtWeight.getText().toString()) > 0 && !etxtWeight.getText().toString().isEmpty()) {
                            txtvName.setText(finalPlayer.getName());
                            finalPlayer.setName(etxtName.getText().toString());
                            finalPlayer.setWeight(Integer.parseInt(etxtWeight.getText().toString()));
                            finalPlayer.setIsMan(spGender.getSelectedItem().toString().equals("Mann"));
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

    public void removePlayer(Player player) {
        for (View tmp : playerelements) {
            TextView textView = (TextView) tmp.findViewById(R.id.txtvPlayerName);
            if (textView.getText().toString().equals(player.getName())) {
                playerelements.remove(tmp);
            }
        }
        int lastPlayerId = player.getLastPlayerId();
        int nextPLayerId = player.getNextPlayerId();
        System.out.println("LastplayerId: " + lastPlayerId + " NextPlayerID: " + nextPLayerId);
        if(player.getHasNextPlayer() && player.getHastLastPlayer()){
            Player.getPlayerById(players, lastPlayerId).setNextPlayerId(nextPLayerId);
            Player.getPlayerById(players, nextPLayerId).setLastPlayerId(lastPlayerId);
        } else if(player.getHasNextPlayer() && !player.getHastLastPlayer()){
            Player.getPlayerById(players, nextPLayerId).setLastPlayerId(-1);
            Player.getPlayerById(players, nextPLayerId).setHasLastPlayer(false);
        } else {
            Player.getPlayerById(players, lastPlayerId).setNextPlayerId(-1);
            Player.getPlayerById(players, lastPlayerId).setHasNextPlayer(false);
        }
        players.remove(player);
    }
}
