package com.tomade.saufomat.activity.miniGames.ichHabNochNie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGames.BaseMiniGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IchHabNochNieActivity extends BaseMiniGame implements View.OnClickListener {
    private static Random random;

    private List<String> currentQuestions;
    private List<String> allQuestions;

    private TextView turnCounter;
    private TextView taskView;

    private boolean tutorialShown = false;
    private String currentTask;
    private int turnCount = 0;
    private int maxTurns;
    private boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ich_hab_noch_nie);

        random = new Random();

        this.currentQuestions = new ArrayList<>();
        this.allQuestions = new ArrayList<>();

        this.initLists();

        this.taskView = (TextView) this.findViewById(R.id.taskText);
        this.turnCounter = (TextView) this.findViewById(R.id.turnCounter);
        this.currentTask = this.getQuestion();
        this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_i_have_never, this.currentTask));

        ImageButton popup = (ImageButton) this.findViewById(R.id.popupButton);
        ImageButton tutorial = (ImageButton) this.findViewById(R.id.tutorialButton);
        ImageButton back = (ImageButton) this.findViewById(R.id.backButton);

        if (this.fromMainGame) {
            back.setVisibility(View.INVISIBLE);
            TextView backText = (TextView) this.findViewById(R.id.backText);
            backText.setVisibility(View.INVISIBLE);
            this.maxTurns = this.playerList.size() * 3;
            if (this.maxTurns > 30) {
                this.maxTurns = 30;
            }
            this.turnCounter.setText((this.turnCount + 1) + "/" + this.maxTurns);
        } else {
            this.turnCounter.setVisibility(View.GONE);
        }

        popup.setOnClickListener(this);
        tutorial.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButton) {
            this.leaveGame();
        } else {
            if (this.tutorialShown) {
                this.tutorialShown = false;
                this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_i_have_never, this
                        .currentTask));
            } else {
                switch (v.getId()) {
                    case R.id.popupButton:
                        this.nextQuestion();
                        break;
                    case R.id.tutorialButton:
                        this.showTutorial();
                        break;
                }
            }
        }
    }

    private void nextQuestion() {
        if (this.gameOver) {
            this.leaveGame();
        }
        this.currentTask = this.getQuestion();
        this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_i_have_never, this.currentTask));
        this.turnCount++;
        if (this.fromMainGame) {
            if (this.turnCount >= this.maxTurns) {
                this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_game_over));
                this.gameOver = true;
            } else {
                this.turnCounter.setText((this.turnCount + 1) + "/" + this.maxTurns);
            }
        }
    }

    private void showTutorial() {
        this.taskView.setText(R.string.minigame_ich_hab_noch_nie_tutorial);
        this.tutorialShown = true;
    }

    private void initLists() {
        Collections.addAll(this.allQuestions, IchHabNochNieTasks.TASKS);

        this.refreshList();
    }

    private String getQuestion() {
        int index = random.nextInt(this.currentQuestions.size());
        String result = this.currentQuestions.get(index);
        this.currentQuestions.remove(index);
        if (this.currentQuestions.size() <= 0) {
            this.refreshList();
        }
        return result;
    }

    private void refreshList() {
        this.currentQuestions.addAll(this.allQuestions);
    }
}
