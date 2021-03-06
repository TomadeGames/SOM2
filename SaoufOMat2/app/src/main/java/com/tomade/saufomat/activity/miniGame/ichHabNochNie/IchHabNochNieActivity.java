package com.tomade.saufomat.activity.miniGame.ichHabNochNie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.miniGame.BaseMiniGameActivity;
import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;
import com.tomade.saufomat.persistance.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Minispiel Ich hab noch nie
 */
public class IchHabNochNieActivity extends BaseMiniGameActivity<BaseMiniGamePresenter> implements View.OnClickListener {
    private TextView turnCounter;
    private TextView taskView;

    private String currentTask;
    private int turnCount = 0;
    private boolean gameOver = false;

    List<String> currentQuestions = new ArrayList<>();

    @Override
    protected void initPresenter() {
        this.presenter = new BaseMiniGamePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ich_hab_noch_nie);

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
            this.turnCounter.setText(String.format(this.getString(R.string.minigame_turn_counter),
                    this.turnCount + 1, TARGET_TURN_COUNT));
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
            if (this.turnCount >= TARGET_TURN_COUNT) {
                this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_game_over));
                this.gameOver = true;
            } else {
                this.turnCounter.setText(String.format(this.getString(R.string.minigame_turn_counter),
                        this.turnCount + 1, TARGET_TURN_COUNT));
            }
        }
    }

    private String getQuestion() {
        Random random = new Random(System.currentTimeMillis());
        if (this.presenter.isFromMainGame()) {
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            List<String> questions = databaseHelper.getUnusedIchHabNochNieTasks();

            if (questions.isEmpty()) {
                databaseHelper.resetIchHabNochNieTasks(IchHabNochNieTasks.TASKS);
                questions = databaseHelper.getUnusedIchHabNochNieTasks();
            }

            int index = random.nextInt(questions.size());

            String result = questions.get(index);
            databaseHelper.useIchHabNochNieTask(result);
            return result;
        }
        if (this.currentQuestions.isEmpty()) {
            Collections.addAll(this.currentQuestions, IchHabNochNieTasks.TASKS);
        }
        int index = random.nextInt(this.currentQuestions.size());
        String result = this.currentQuestions.get(index);
        this.currentQuestions.remove(result);
        return result;
    }
}
