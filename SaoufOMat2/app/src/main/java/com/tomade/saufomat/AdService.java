package com.tomade.saufomat;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Verwaltung der Werbung
 * Created by woors on 12.07.2017.
 */

public class AdService {
    private static final String TAG = AdService.class.getSimpleName();
    private static InterstitialAd interstitialAd = null;

    /**
     * Initialisiert die Vollbildwerbung des Hautpspiels
     *
     * @param context der Context
     */
    public static void initializeInterstitialAd(Context context) {
        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(context);
            interstitialAd.setAdUnitId(context.getString(R.string.maingame_ad_id));
            Log.i(TAG, "InterstitialAd initialized");
            requestAd();
        }
    }

    /**
     * Setzt einen AdListener
     *
     * @param adListener der AdListener
     */
    public static void setAdListener(AdListener adListener) {
        interstitialAd.setAdListener(adListener);
    }

    /**
     * Zeigt die Vollbildwerbung des Hauptspiels an
     */
    public static void requestAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        Log.i(TAG, "new InterstitialAd requested");
    }

    /**
     * Gibt die Vollbildwerbung des Hauptspiels zurück
     *
     * @return die Werbung des Hauptspiels
     */
    public static InterstitialAd getInterstitialAd() {
        return interstitialAd;
    }
}
