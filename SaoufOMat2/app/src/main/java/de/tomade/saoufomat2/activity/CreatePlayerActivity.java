package de.tomade.saoufomat2.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.app.AlertDialog;
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

import de.tomade.saoufomat2.model.Player;
import de.tomade.saoufomat2.R;

public class CreatePlayerActivity extends Activity implements View.OnClickListener {
    Button btnNewPlayer = null;
    LinearLayout linearLayout = null;
    ArrayList<Player> players = new ArrayList<Player>();
    ArrayList<View> playerelements = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player);

        this.linearLayout = (LinearLayout) findViewById(R.id.llCreatePlayer);
        this.btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        this.btnNewPlayer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNewPlayer:
                //Intent intent = new Intent(this, NewPlayerDialog.class);
                //startActivity(intent);
                Player newPlayer = new Player();
                showDialog(newPlayer);
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
                        newPlayer.setGender(spGender.getSelectedItem().toString());
                        if (!newPlayer.getName().isEmpty() && newPlayer.getWeight() > 0 && !newPlayer.getGender().isEmpty()) {
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
            this.players.add(player);
            displayPlayer(player);
        } else {
            Toast.makeText(CreatePlayerActivity.this, "Name bereits vorhanden!", Toast.LENGTH_LONG).show();
        }
    }

    public void displayPlayer(final Player player) {
        //TODO Editieren
        LayoutInflater inflater = this.getLayoutInflater();
        final View playerView = inflater.inflate(R.layout.player_element, null);
        playerelements.add(playerView);

        TextView playername = (TextView) playerView.findViewById(R.id.txtvPlayerName);
        playername.setText(player.getName());

        ImageButton delete = (ImageButton) playerView.findViewById(R.id.ibDelete);
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                linearLayout.removeView(playerView);
                players.remove(player);
                playerelements.remove(playerView);
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
        spGender.setSelection(getIndex(spGender, player.getGender()));

        final Player finalPlayer = player;
        builder.setMessage("Spieler Bearbeiten")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!etxtName.getText().toString().isEmpty() && Integer.parseInt(etxtWeight.getText().toString()) > 0 && !etxtWeight.getText().toString().isEmpty()) {
                            txtvName.setText(finalPlayer.getName());
                            finalPlayer.setName(etxtName.getText().toString());
                            finalPlayer.setWeight(Integer.parseInt(etxtWeight.getText().toString()));
                            finalPlayer.setGender(spGender.getSelectedItem().toString());
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
        players.remove(player);
    }
}
