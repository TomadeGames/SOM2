package com.tomade.saufomat.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * Basisklasse für Activities
 * Created by woors on 03.08.2017.
 */

public abstract class BaseActivity<PRESENTER extends BasePresenter> extends Activity {
    protected PRESENTER presenter;

    protected int screenHeight;
    protected int screenWidth;

    /**
     * Initialisert den Presenter
     */
    protected abstract void initPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        wm.getDefaultDisplay().getSize(screenSize);
        this.screenHeight = screenSize.y;
        this.screenWidth = screenSize.x;

        this.initPresenter();
        this.presenter.onCreate(savedInstanceState);
    }
}
