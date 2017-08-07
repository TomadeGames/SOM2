package com.tomade.saufomat;

import android.util.Log;

import com.tomade.saufomat.model.player.Player;

/**
 * Verwaltet Spieler
 * Created by woors on 07.08.2017.
 */

public class PlayerService {
    private static final String TAG = PlayerService.class.getSimpleName();

    public static boolean fillPlayerData(Player newPlayer, String name, String weight, String gender) {
        newPlayer.setName(name);
        try {
            newPlayer.setWeight((int) Float.parseFloat(weight));
        } catch (NumberFormatException e) {
            Log.w(TAG, "Error et formating Weight to Float: " + weight, e);
        }
        boolean genderSet = false;
        if (gender.equals("Mann")) {
            newPlayer.setIsMan(true);
            genderSet = true;
        } else if (gender.equals("Frau")) {
            newPlayer.setIsMan(false);
            genderSet = true;
        }
        if (!newPlayer.getName().isEmpty() && newPlayer.getWeight() > 0 && genderSet) {
            return true;
        } else {
            return false;
        }
    }
}
