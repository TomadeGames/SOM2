package com.tomade.saufomat.activity;

import android.os.Bundle;

/**
 * Basisklasse für Presnter
 * Created by woors on 03.08.2017.
 */

public abstract class BasePresenter<ACTIVITY extends BaseActivity> {
    protected ACTIVITY activity;

    /**
     * Konstruktor
     *
     * @param activity die Activity zu dem Presenter
     */
    public BasePresenter(ACTIVITY activity) {
        this.activity = activity;
    }

    /**
     * Wird beim erstellen der Activity aufgerufen
     *
     * @param savedInstanceState -
     */
    public abstract void onCreate(Bundle savedInstanceState);
}
