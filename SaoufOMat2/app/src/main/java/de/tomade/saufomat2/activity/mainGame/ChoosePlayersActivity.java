package de.tomade.saufomat2.activity.mainGame;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.constant.IntentParameter;
import de.tomade.saufomat2.model.Player;

/**
 * Ein Dialog um Spieler auszuwählen
 */
public class ChoosePlayersActivity extends Activity implements View.OnClickListener {
    private static int playerElementId = 0;

    private int selectionAmoung;
    private ArrayList<Player> playerList;
    private ArrayList<Player> selectedPlayers;
    private LinearLayout playerContainer;
    private Map<Integer, Player> playerElements = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_choose_players);

        Bundle extras = this.getIntent().getExtras();

        this.selectionAmoung = (int) extras.get(IntentParameter.ChoosePlayersActivity.SELECTION_AMOUNT);
        this.playerList = (ArrayList<Player>) extras.getSerializable(IntentParameter.PLAYER_LIST);

        this.selectedPlayers = new ArrayList<>();

        this.playerContainer = (LinearLayout) this.findViewById(R.id.playerContainer);

        for (Player player : this.playerList) {
            this.createPlayerElement(player);
        }
    }

    private void createPlayerElement(Player player) {
        final int playerViewId = playerElementId++;
        LayoutInflater inflater = this.getLayoutInflater();
        final View playerView = inflater.inflate(R.layout.display_player_element, null);
        this.playerElements.put(playerViewId, player);

        TextView playerNameText = (TextView) playerView.findViewById(R.id.playerNameText);
        ImageButton playerButton = (ImageButton) playerView.findViewById(R.id.playerButton);

        playerNameText.setText(player.getName());
        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePlayersActivity.this.selectedPlayers.add(ChoosePlayersActivity.this.playerElements.get
                        (playerViewId));
            }
        });

        this.playerContainer.addView(playerView);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acceptButton:

                break;
        }
    }
}
