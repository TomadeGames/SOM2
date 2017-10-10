package com.tomade.saufomat.constant;

/**
 * Definitionen von Übergabeparametern zwischen den Intents
 * Created by woors on 07.03.2017.
 */

public interface IntentParameter {
    String FROM_MAIN_GAME = "fromMainGame";
    String PLAYER_LIST = "playerList";
    String CURRENT_PLAYER = "currentPlayer";
    String LAST_GAME = "lastGame";

    /**
     * Übergabeparemeter für Minispiele
     */
    interface MainGame {
        String CURRENT_TASK = "mainGame_currentTask";
        String CURRENT_MINI_GAME = "mainGame_currentMiniGame";
        String CURRENT_TASK_IS_MINI_GAME = "mainGame_currentTaskIsMiniGame";
        String AD_COUNTER = "mainGame_adCounter";
        String NEW_GAME = "mainGame_newGame";
        String NEW_TASK_EVENT = "mainGame_newTaskEvent";
    }

    /**
     * Übergabeparameter für Tutorials
     */
    interface Tutorial {
        String TEXT_ID = "tutorial_textKey";
    }
}
