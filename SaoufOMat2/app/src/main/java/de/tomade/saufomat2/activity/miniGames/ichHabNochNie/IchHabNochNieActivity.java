package de.tomade.saufomat2.activity.miniGames.ichHabNochNie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.activity.miniGames.MiniGame;

public class IchHabNochNieActivity extends Activity implements View.OnClickListener {
    private static final String TAG = IchHabNochNieActivity.class.getSimpleName();
    private static final String HAB_NIE = "Ich hab noch nie\n";
    private static Random random;

    private List<String> currentQuestions;
    private List<String> allQuestions;

    private TextView taskView;

    private boolean tutorialShown = false;
    private String currentTask;

    private boolean fromMenue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ich_hab_noch_nie);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            fromMenue = extras.getBoolean("fromMenue");
        }

        random = new Random();

        this.currentQuestions = new ArrayList<>();
        this.allQuestions = new ArrayList<>();

        initLists();

        this.taskView = (TextView) this.findViewById(R.id.taskText);
        this.setCurrentTask(getQuestion());
        this.taskView.setText(HAB_NIE + this.getCurrentTask());

        ImageButton popup = (ImageButton) this.findViewById(R.id.popupButton);
        ImageButton tutorial = (ImageButton) this.findViewById(R.id.tutorialButton);
        ImageButton back = (ImageButton) this.findViewById(R.id.backButton);

        if (!this.fromMenue) {
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
            Intent intent;
            if(fromMenue) {
                intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
                intent.putExtra("lastGame", MiniGame.ICH_HAB_NOCH_NIE);
            } else {
                intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
            }
            this.startActivity(intent);
        } else {
            if (this.isTutorialShown()) {
                this.setTutorialShown(false);
                this.taskView.setText(HAB_NIE + this.getCurrentTask());
            } else {
                switch (v.getId()) {
                    case R.id.popupButton:
                        nextQuestion();
                        break;
                    case R.id.tutorialButton:
                        showTutorial();
                        break;
                }
            }
        }
    }

    private void nextQuestion() {
        this.setCurrentTask(getQuestion());
        this.taskView.setText(HAB_NIE + this.getCurrentTask());
    }

    private void showTutorial() {
        this.taskView.setText("Anleitung:\nEs werden Sätze generiert, die mit \"Ich hab noch nie\" beginnen. Jeder, der die verneinte Handlung doch schon mal gemacht hat, muss trinken. Danach wird das Handy weitergegeben.");
        setTutorialShown(true);
    }

    private void initLists() {
        this.getAllQuestions().add("ein Pferd geritten");
        this.getAllQuestions().add("ein meine Eltern nackt gesehen");

        for (String s : IchHabNochNieTasks.TASKS) {
            this.getAllQuestions().add(s);
        }

        refreshList();
    }

    private String getQuestion() {
        int index = random.nextInt(getCurrentQuestions().size());
        String erg = getCurrentQuestions().get(index);
        getCurrentQuestions().remove(index);
        if (getCurrentQuestions().size() <= 0) {
            refreshList();
        }
        return erg;
    }

    private void refreshList() {
        for (String s : this.getAllQuestions()) {
            this.getCurrentQuestions().add(s);
        }
    }

    public List<String> getCurrentQuestions() {
        return currentQuestions;
    }

    public void setCurrentQuestions(List<String> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }

    public List<String> getAllQuestions() {
        return allQuestions;
    }

    public void setAllQuestions(List<String> allQuestions) {
        this.allQuestions = allQuestions;
    }

    public boolean isTutorialShown() {
        return tutorialShown;
    }

    public void setTutorialShown(boolean tutorialShown) {
        this.tutorialShown = tutorialShown;
    }

    public String getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(String currentTask) {
        this.currentTask = currentTask;
    }
}
