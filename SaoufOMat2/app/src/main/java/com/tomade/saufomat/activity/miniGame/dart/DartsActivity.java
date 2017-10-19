package com.tomade.saufomat.activity.miniGame.dart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tomade.saufomat.DrinkHelper;
import com.tomade.saufomat.R;
import com.tomade.saufomat.activity.SwipeController;
import com.tomade.saufomat.activity.mainGame.task.TaskParser;
import com.tomade.saufomat.activity.miniGame.BaseMiniGameActivity;
import com.tomade.saufomat.activity.miniGame.BaseMiniGamePresenter;
import com.tomade.saufomat.constant.Direction;
import com.tomade.saufomat.model.player.Player;

public class DartsActivity extends BaseMiniGameActivity<BaseMiniGamePresenter<DartsActivity>> {
    private static final String TAG = DartsActivity.class.getSimpleName();
    private static final int X_FACTOR = 50;
    private static final int Y_FACTOR = 100;
    private static final int ANIMATION_DURATION = 400;
    private SwipeController swipeController;
    private ImageView arrowImage;
    private ImageView targetImage;
    private Point startPosition;
    private float startScaleX;
    private float startScaleY;
    private DartState gameState;
    private RelativeLayout popupLayout;
    private TextView popupText;
    private TextView playerText;
    private AnimationSet idleAnimation;

    @Override
    protected void initPresenter() {
        this.presenter = new BaseMiniGamePresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setContentView(R.layout.activity_dart);
        super.onCreate(savedInstanceState);
        this.swipeController = new SwipeController();
        this.targetImage = this.findViewById(R.id.targetImage);
        this.popupLayout = this.findViewById(R.id.popUpLayout);
        this.popupText = this.findViewById(R.id.popupText);
        this.arrowImage = this.findViewById(R.id.arrowImage);
        this.idleAnimation = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.dart_arrow_idle);
        this.arrowImage.post(new Runnable() {
            @Override
            public void run() {
                DartsActivity.this.startPosition = new Point((int) DartsActivity.this.arrowImage.getX(), (int)
                        DartsActivity.this.arrowImage.getY());
                DartsActivity.this.startScaleX = DartsActivity.this.arrowImage.getScaleX();
                DartsActivity.this.startScaleY = DartsActivity.this.arrowImage.getScaleY();
            }
        });
        if (this.presenter.isFromMainGame()) {
            this.playerText = this.findViewById(R.id.playerNameText);
            this.playerText.setText(this.getCurrentPlayer().getName());
            this.findViewById(R.id.backButtonLayout).setVisibility(View.GONE);
            this.findViewById(R.id.playerLayout).setVisibility(View.VISIBLE);
        }
        this.gameState = DartState.DART_THROWABLE;
        this.startIdleAnimation();
    }

    private void stopIdleAnimation() {
        this.idleAnimation.cancel();
        this.arrowImage.clearAnimation();
    }


    private void startIdleAnimation() {
        final AnimationSet idleAnimation = this.idleAnimation;
        idleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (DartsActivity.this.gameState == DartState.DART_THROWABLE) {
                    DartsActivity.this.arrowImage.startAnimation(idleAnimation);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        this.arrowImage.startAnimation(idleAnimation);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.gameState == DartState.DART_THROWABLE) {
            if (this.swipeController.handleSwipe(event)) {
                if (this.swipeController.getDirectionY() == Direction.UP) {
                    this.gameState = DartState.ANIMATION;
                    this.throwDart();
                }
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "down at " + event.getX() + ", " + event.getY());
                break;
            case MotionEvent.ACTION_UP:
                switch (this.gameState) {
                    case GAME_OVER:
                        this.presenter.leaveGame();
                        break;
                    case DART_ARRIVED:
                        if (this.presenter.isFromMainGame()) {
                            this.playerText.setText(this.getCurrentPlayer().getName());
                        }
                        this.popupLayout.setVisibility(View.GONE);
                        this.resetArrow();
                        this.gameState = DartState.DART_THROWABLE;
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.debugButton:
                this.resetArrow();
                break;
            case R.id.targetImage:
                break;
        }
    }

    private void resetArrow() {
        this.arrowImage.setX(this.startPosition.x);
        this.arrowImage.setY(this.startPosition.y);
        this.arrowImage.setScaleX(this.startScaleX);
        this.arrowImage.setScaleY(this.startScaleY);
        this.startIdleAnimation();
    }

    private void throwDart() {
        this.stopIdleAnimation();
        final Point arrowPosition = this.calculateThrowingPosition();
        this.arrowImage.animate().x(arrowPosition.x).y(arrowPosition.y).scaleX(0.3f).scaleY
                (-0.10f).setDuration(ANIMATION_DURATION).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                endThrow(arrowPosition);
            }
        }).start();
    }

    private void endThrow(Point arrowImagePosition) {
        final DartsField hiddenField = this.getTargetField(this.getArrowTop(arrowImagePosition));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showText(hiddenField);
            }
        }, 500);
        Log.i(TAG, "Field hit is: " + hiddenField);
    }

    private void showText(DartsField fieldHit) {
        String text = fieldHit.getName() + "\n";
        if (this.presenter.isFromMainGame()) {
            text += TaskParser.parseText(fieldHit.getPrice(), this.presenter
                    .getCurrentPlayer());
            switch (fieldHit) {
                case OUT:
                    this.getCurrentPlayer().increaseDrinks(5);
                    break;
                case MIDDLE:
                    DrinkHelper.increaseAllButOnePlayer(3, this.getCurrentPlayer(), this);
                    break;
                case INNER_BLUE:
                    DrinkHelper.increaseLeft(3, this);
                    break;
                case OUTER_BLUE:
                    DrinkHelper.increaseLeft(1, this);
                    break;
                case INNER_GREEN:
                    DrinkHelper.increaseRight(3, this);
                    break;
                case OUTER_GREEN:
                    DrinkHelper.increaseRight(1, this);
                    break;
                case INNER_YELLOW:
                    this.getCurrentPlayer().increaseDrinks(3);
                    break;
                case OUTER_YELLOW:
                    this.getCurrentPlayer().increaseDrinks(1);
                    break;
            }
        } else {
            Player currentPlayer = new Player();
            Player lastPlayer = new Player();
            lastPlayer.setName("Dein rechter Nachbar");
            currentPlayer.setNextPlayer(currentPlayer);
            currentPlayer.setLastPlayer(lastPlayer);
            currentPlayer.setName("Dein linker Nachbar");
            text += TaskParser.parseText(fieldHit.getPrice(), currentPlayer);
        }
        if (this.presenter.isFromMainGame()) {
            if (this.presenter.getCurrentPlayerAtStart() == this.presenter
                    .getCurrentPlayer().getNextPlayer()) {
                this.gameState = DartState.GAME_OVER;
                text += "\n\nSpiel " + "vorbei!";
            } else {
                this.presenter.nextPlayer();
                text += "\n\n" + this.getCurrentPlayer().getName() + " ist dran";
                this.gameState = DartState.DART_ARRIVED;
            }
        } else {
            text += "\n\nDer nächste ist dran";
            this.gameState = DartState.DART_ARRIVED;
        }
        this.popupText.setText(text);
        this.popupLayout.setVisibility(View.VISIBLE);
    }

    private DartsField getTargetField(Point arrowPosition) {
        Point middle = new Point((int) (this.targetImage.getX() + (this.targetImage.getWidth() / 2)),
                (int) (this.targetImage.getY() + (this.targetImage.getHeight() / 2)));
        double radiusToMiddleCircelEnd = this.targetImage.getWidth() * 0.0924 / 2;
        double radiusToInnerCircelEnd = this.targetImage.getWidth() * 0.5089 / 2;
        double radiusToInnerRingEnd = this.targetImage.getWidth() * 0.5885 / 2;
        double radiusToOuterCircleEnd = this.targetImage.getWidth() * 0.8658 / 2;

        double distanceFromMiddle = this.calculateDistanceFromMiddle(arrowPosition, middle);
        double angel = this.calculateAngel(middle, arrowPosition);

        if (distanceFromMiddle < radiusToMiddleCircelEnd) {
            return DartsField.MIDDLE;
        } else if (distanceFromMiddle < radiusToInnerCircelEnd) {
            return this.getSegment(true, angel);
        } else if (distanceFromMiddle < radiusToInnerRingEnd) {
            return DartsField.RING;
        } else if (distanceFromMiddle < radiusToOuterCircleEnd) {
            return this.getSegment(false, angel);
        } else {
            return DartsField.OUT;
        }
    }

    private DartsField getSegment(boolean innerCircle, double angel) {
        if (innerCircle) {
            if (angel < 45) {
                return DartsField.INNER_GREEN;
            }
            if (angel < 90) {
                return DartsField.INNER_YELLOW;
            }
            if (angel < 135) {
                return DartsField.INNER_BLUE;
            }
            if (angel < 180) {
                return DartsField.INNER_YELLOW;
            }
            if (angel < 225) {
                return DartsField.INNER_GREEN;
            }
            if (angel < 270) {
                return DartsField.INNER_YELLOW;
            }
            if (angel < 315) {
                return DartsField.INNER_BLUE;
            }
            if (angel < 360) {
                return DartsField.INNER_YELLOW;
            }
        } else {
            if (angel < 45) {
                return DartsField.OUTER_YELLOW;
            }
            if (angel < 90) {
                return DartsField.OUTER_BLUE;
            }
            if (angel < 135) {
                return DartsField.OUTER_YELLOW;
            }
            if (angel < 180) {
                return DartsField.OUTER_GREEN;
            }
            if (angel < 225) {
                return DartsField.OUTER_YELLOW;
            }
            if (angel < 270) {
                return DartsField.OUTER_BLUE;
            }
            if (angel < 315) {
                return DartsField.OUTER_YELLOW;
            }
            if (angel < 360) {
                return DartsField.OUTER_GREEN;
            }
        }
        throw new IllegalArgumentException("angel should be lesser than 360 but is " + angel + " innerCircel was " +
                innerCircle);
    }

    private double calculateDistanceFromMiddle(Point arrowTopPosition, Point middle) {
        return Math.sqrt(
                Math.pow(this.getAmount(arrowTopPosition.x - middle.x), 2)
                        + Math.pow(this.getAmount(arrowTopPosition.y - middle.y), 2));
    }

    private double getAmount(double value) {
        return value >= 0 ? value : -value;
    }


    private Point getArrowTop(Point arrowImagePosition) {
        int imageHeight = this.arrowImage.getHeight();
        Point arrowTop = new Point((int) (arrowImagePosition.x + (this.arrowImage.getWidth() / 2)), (int)
                ((arrowImagePosition.y) + this.arrowImage.getHeight() / 2.368));

        Log.d(TAG, "Arrow top is (" + arrowTop.x + ", " + arrowTop.y + ") height is " + imageHeight);
        return arrowTop;
    }

    private double calculateAngel(Point middlePosition, Point arrowPosition) {
        Point distanceVector = new Point(middlePosition.x - arrowPosition.x, middlePosition.y - arrowPosition.y);
        Point normalVector = new Point(0, 1);
        double lengthP1 = Math.sqrt(Math.pow(normalVector.x, 2) + Math.pow(normalVector.y, 2));
        double lengthP2 = Math.sqrt(Math.pow(distanceVector.x, 2) + Math.pow(distanceVector.y, 2));
        double bottomSide = lengthP1 * lengthP2;
        double topSide = (normalVector.x * distanceVector.x + normalVector.y * distanceVector.y);
        double resultBeforeArcos = topSide / bottomSide;
        double resultInRad = Math.acos(resultBeforeArcos);
        double result = Math.toDegrees(resultInRad);
        if (arrowPosition.x < middlePosition.x) {
            result = 360 - result;
        }
        Log.i(TAG, "Angel between mid and arrow is " + result);
        return result;
    }

    private Point calculateThrowingPosition() {
        int x;
        int y;

        x = (int) (this.arrowImage.getX() + this.swipeController.getDistanceX() * (this.swipeController.getDirectionX
                () == Direction.RIGHT ? 1 : -1) * X_FACTOR / this.swipeController.getDuration());
        y = (int) (this.arrowImage.getY() - this.swipeController.getDistanceY() * Y_FACTOR / this
                .swipeController.getDuration());
        Log.d(TAG, "Dartposition: (" + x + ":" + y + ")");
        return new Point(x, y);
    }
}
