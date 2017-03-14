package de.tomade.saufomat2.constant;

/**
 * Definitionen von Übergabeparametern zwischen den Intents
 * Created by woors on 07.03.2017.
 */

public interface IntentParameter {
    String FROM_MAIN_GAME = "fromMainGame";
    String PLAYER_LIST = "playerList";
    String CURRENT_PLAYER_ID = "currentPlayerId";
    String LAST_GAME = "lastGame";

    interface MainGame {
        String CURRENT_TASK = "currentTask";
        String CURRENT_MINI_GAME = "currentMiniGame";
        String CURRENT_TASK_IS_MINI_GAME = "currentTaskIsMiniGame";
    }
}
