package de.tomade.saufomat2.activity.miniGames.kistenStapeln;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import de.tomade.saufomat2.R;
import de.tomade.saufomat2.activity.ChooseMiniGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGameActivity;
import de.tomade.saufomat2.activity.mainGame.MainGamePanel;
import de.tomade.saufomat2.model.MiniGame;

public class KistenStapelnActivity extends Activity implements View.OnClickListener {
    private static final String TAG = KistenStapelnActivity.class.getSimpleName();
    private final int BALANCE_TOLERANCE = 200;
    private float crateStartX;
    private float crateSTartY;
    private int createWidth;
    private int crateHeight;

    private KistenStapelnState gameState = KistenStapelnState.MOVING_CRATE;

    private int screenWidth;
    private int screenHeight;

    private boolean fromMenue = false;

    private RelativeLayout tutorialPanel;
    private TextView nextPlayerText;
    private TextView crateCounter;
    private RelativeLayout nextPlayerPanel;
    private RelativeLayout allLayout;
    private boolean tutorialShown = false;

    private List<ImageView> towerImageList = new ArrayList<>();
    private ImageView currentCrate;

    private float towerHeight = 0;
    private float targetY;

    private boolean touchDown = false;

    private float balance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisten_stapeln);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.fromMenue = extras.getBoolean("fromMenue");
        }

        this.currentCrate = (ImageView) this.findViewById(R.id.beerCrate0);
        this.towerImageList.add((ImageView)this.findViewById(R.id.targetImage));
        this.tutorialPanel = (RelativeLayout) this.findViewById(R.id.tutorialLayout);
        this.nextPlayerText = (TextView) this.findViewById(R.id.nextPlayerText);
        this.nextPlayerPanel = (RelativeLayout) this.findViewById(R.id.nextPlayerLayout);
        this.allLayout = (RelativeLayout) this.findViewById(R.id.allLayout);
        this.crateCounter = (TextView) this.findViewById(R.id.crateCounter);

        this.findViewById(R.id.tutorialButton).setOnClickListener(this);
        this.findViewById(R.id.backButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (this.isTutorialShown()) {
            this.tutorialPanel.setVisibility(View.GONE);
            this.setTutorialShown(false);
        } else {
            switch (v.getId()) {
                case R.id.backButton:
                    leaveGame();
                    break;
                case R.id.tutorialButton:
                    this.tutorialPanel.setVisibility(View.VISIBLE);
                    this.setTutorialShown(true);
                    break;
            }
        }
    }

    private void leaveGame() {
        Intent intent;
        if (fromMenue) {
            intent = new Intent(this.getApplicationContext(), ChooseMiniGameActivity.class);
            intent.putExtra("lastGame", MiniGame.KISTEN_STAPELN);
        } else {
            intent = new Intent(this.getApplicationContext(), MainGameActivity.class);
        }
        this.startActivity(intent);
    }

    public boolean isTutorialShown() {
        return tutorialShown;
    }

    public void setTutorialShown(boolean tutorialShown) {
        this.tutorialShown = tutorialShown;
        if (tutorialShown) {
            this.tutorialPanel.setVisibility(View.VISIBLE);
        } else {
            this.tutorialPanel.setVisibility(View.GONE);
        }
    }

    private float getTargetY() {
        ImageView element = towerImageList.get(towerImageList.size() - 1);
        return element.getY() - this.currentCrate.getHeight() / 2;
    }

    private void crateFall(){
        float targetY = this.towerImageList.get(this.towerImageList.size()-1).getY() - this.currentCrate.getHeight();
        float deltaY = targetY - this.currentCrate.getY();
        ObjectAnimator fallingCrate = ObjectAnimator.ofFloat(this.currentCrate, "y", targetY);
        fallingCrate.setDuration((long)deltaY);
        fallingCrate.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onCrateLanded();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fallingCrate.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.tutorialShown) {
            this.tutorialPanel.setVisibility(View.GONE);
            this.tutorialShown = false;
        } else {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(gameState == KistenStapelnState.MOVING_CRATE) {
                        this.crateStartX = this.currentCrate.getX();
                        this.crateSTartY = this.currentCrate.getY();
                        this.createWidth = this.currentCrate.getWidth();
                        this.crateHeight = this.currentCrate.getHeight();
                        this.targetY = getTargetY();
                        this.touchDown = true;
                    }
                    else if(gameState == KistenStapelnState.LANDED){
                    }
                    else if(gameState == KistenStapelnState.END_TURN){
                        this.startTurn();
                    }
                    else if(gameState == KistenStapelnState.GAME_END){
                        this.leaveGame();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if(gameState == KistenStapelnState.MOVING_CRATE) {
                        if (touchDown) {
                            touchDown = false;
                            this.gameState = KistenStapelnState.FALLING_CRATE;
                            this.crateFall();
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(gameState == KistenStapelnState.MOVING_CRATE) {
                        if (touchDown && this.gameState == KistenStapelnState.MOVING_CRATE) {
                            this.currentCrate.setX(event.getRawX() - this.currentCrate.getWidth() / 2);
                            if (event.getRawY() < this.targetY) {
                                this.currentCrate.setY(event.getRawY() - this.currentCrate.getHeight() / 2);
                            }
                        }
                    }
                    break;
                }
        }
        return true;
    }

    public void onCrateLanded(){
        this.gameState = KistenStapelnState.LANDED;
        float movement = (this.screenWidth/2 - this.currentCrate.getX() - this.currentCrate.getWidth() / 2) * (this.towerImageList.size() * 2);
        this.balance += movement;
        Log.d(TAG, "balance = " + this.balance);
        if(this.checkIfFalling()){
            this.startTowerFall();
        }
        else{
            this.endTurn();
        }
    }

    private void endGame(){
        this.nextPlayerPanel.setVisibility(View.VISIBLE);
        this.nextPlayerText.setText("Trink " + (this.towerImageList.size() - 1));
        this.gameState = KistenStapelnState.GAME_END;
    }

    private void startTowerFall(){
        this.gameState = KistenStapelnState.FALLING_TOWER;
        this.towerImageList.add(this.currentCrate);
        for(ImageView iv: this.towerImageList) {
            if(iv != this.towerImageList.get(0)) {
                float xDistance = this.screenWidth / 2 - iv.getX();

                ObjectAnimator xAnim = ObjectAnimator.ofFloat(iv, "x", (iv.getX() - this.balance) / 2);
                xAnim.setDuration(1000);
                ObjectAnimator yAnim = ObjectAnimator.ofFloat(iv, "y", screenHeight + 1000);
                yAnim.setDuration(1500);
                if(iv == this.towerImageList.get(this.towerImageList.size() - 1)){
                    yAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            endGame();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
                xAnim.start();
                yAnim.start();
            }
        }
    }

    private void endTurn(){
        //TODO:Spielerlogik einbauen
        towerHeight = this.towerImageList.get(this.towerImageList.size()-1).getY();
        if(towerHeight > screenHeight/2){
            towerHeight = 0;
        }
        this.towerImageList.add(this.currentCrate);
        this.nextPlayerPanel.setVisibility(View.VISIBLE);
        this.crateCounter.setText("Kisten: " + (this.towerImageList.size() - 1));
        this.currentCrate = new ImageView(this);
        this.currentCrate.setX(this.crateStartX);
        this.currentCrate.setY(this.crateSTartY);
        this.currentCrate.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                this.createWidth,
                this.crateHeight);
        this.currentCrate.setImageResource(R.drawable.beer_crate);
        allLayout.addView(this.currentCrate, lp);
        this.nextPlayerPanel.bringToFront();
        this.tutorialPanel.bringToFront();
        this.gameState = KistenStapelnState.END_TURN;
    }

    private void startTurn(){
        this.nextPlayerPanel.setVisibility(View.GONE);


        if(towerHeight != 0){
            this.gameState = KistenStapelnState.MOVING_TOWER;
            moveTowerDown(towerHeight);
        }
        else {
            this.gameState = KistenStapelnState.MOVING_CRATE;
        }
    }

    private void moveTowerDown(float towerHeight){
        float movingDistance = this.screenHeight - towerHeight;

        for(int i = 0; i < this.towerImageList.size(); i++){

            ObjectAnimator anim = ObjectAnimator.ofFloat(this.towerImageList.get(i), "y", this.towerImageList.get(i).getY() + movingDistance);
            anim.setDuration(1000);
            if(i == this.towerImageList.size() - 1){
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        gameState = KistenStapelnState.MOVING_CRATE;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
            anim.start();
        }
    }

    private boolean checkIfFalling(){
        if(this.balance > BALANCE_TOLERANCE
                || this.balance < -BALANCE_TOLERANCE){
            return true;
        }
        return false;
    }
}
