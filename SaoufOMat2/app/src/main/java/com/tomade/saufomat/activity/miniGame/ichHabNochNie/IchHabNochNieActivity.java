package com.tomade.saufomat.activity.miniGame.ichHabNochNie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGame.BaseMiniGameActivity;
import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IchHabNochNieActivity extends BaseMiniGameActivity<BaseMiniGamePresenter> implements View.OnClickListener {
    private static Random random;

    private List<String> currentQuestions;
    private List<String> allQuestions;

    private TextView turnCounter;
    private TextView taskView;

    private String currentTask;
    private int turnCount = 0;
    private int maxTurns;
    private boolean gameOver = false;

    @Override
    protected void initPresenter() {
        this.presenter = new BaseMiniGamePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ich_hab_noch_nie);

        random = new Random(System.currentTimeMillis());

        this.currentQuestions = new ArrayList<>();
        this.allQuestions = new ArrayList<>();

        this.initLists();

        this.taskView = this.findViewById(R.id.taskText);
        this.turnCounter = this.findViewById(R.id.turnCounter);
        this.currentTask = this.getQuestion();
        this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_i_have_never, this.currentTask));

        ImageButton popup = this.findViewById(R.id.popupButton);
        ImageButton back = this.findViewById(R.id.backButton);

        if (this.presenter.isFromMainGame()) {
            back.setVisibility(View.INVISIBLE);
            TextView backText = this.findViewById(R.id.backText);
            backText.setVisibility(View.INVISIBLE);
            this.maxTurns = this.presenter.getPlayerAmount() * 3;
            if (this.maxTurns > 30) {
                this.maxTurns = 30;
            }
            this.turnCounter.setText((this.turnCount + 1) + "/" + this.maxTurns);
        } else {
            this.turnCounter.setVisibility(View.GONE);
        }
        this.findViewById(R.id.tutorialButton).setOnClickListener(this);
        popup.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.popupButton:
                this.nextQuestion();
                break;
        }
    }

    private void nextQuestion() {
        if (this.gameOver) {
            this.presenter.leaveGame();
        }
        this.currentTask = this.getQuestion();
        this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_i_have_never, this.currentTask));
        this.turnCount++;
        if (this.presenter.isFromMainGame()) {
            if (this.turnCount >= this.maxTurns) {
                this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_game_over));
                this.gameOver = true;
            } else {
                this.turnCounter.setText((this.turnCount + 1) + "/" + this.maxTurns);
            }
        }
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
