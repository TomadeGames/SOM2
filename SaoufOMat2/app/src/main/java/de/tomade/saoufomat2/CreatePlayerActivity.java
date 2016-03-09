package de.tomade.saoufomat2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
                showNameDialog(newPlayer);
        }
    }

    public void showNameDialog(final Player newPlayer) {
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
                .setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
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
        this.players.add(player);
        displayPlayer(player);
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
            }
        });

        ImageButton edit = (ImageButton) playerView.findViewById(R.id.ibEdit);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        this.linearLayout.addView(playerView);
    }
}
