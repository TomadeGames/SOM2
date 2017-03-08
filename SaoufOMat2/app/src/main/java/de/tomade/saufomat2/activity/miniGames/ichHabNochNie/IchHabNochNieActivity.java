package de.tomade.saufomat2.activity.miniGames.ichHabNochNie;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.miniGames.BaseMiniGame;

public class IchHabNochNieActivity extends BaseMiniGame implements View.OnClickListener {
    private static Random random;

    private List<String> currentQuestions;
    private List<String> allQuestions;

    private TextView taskView;

    private boolean tutorialShown = false;
    private String currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_ich_hab_noch_nie);

        random = new Random();

        this.currentQuestions = new ArrayList<>();
        this.allQuestions = new ArrayList<>();

        this.initLists();

        this.taskView = (TextView) this.findViewById(R.id.taskText);
        this.currentTask = this.getQuestion();
        this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_i_have_never, this.currentTask));

        ImageButton popup = (ImageButton) this.findViewById(R.id.popupButton);
        ImageButton tutorial = (ImageButton) this.findViewById(R.id.tutorialButton);
        ImageButton back = (ImageButton) this.findViewById(R.id.backButton);

        if (this.fromMainGame) {
            back.setVisibility(View.INVISIBLE);
            TextView backText = (TextView) this.findViewById(R.id.backText);
            backText.setVisibility(View.INVISIBLE);
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
        this.currentTask = this.getQuestion();
        this.taskView.setText(this.getString(R.string.minigame_ich_hab_noch_nie_i_have_never, this.currentTask));
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
