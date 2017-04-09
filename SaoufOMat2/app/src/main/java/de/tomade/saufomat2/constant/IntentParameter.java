package de.tomade.saufomat2.constant;

/**
 * Definitionen von Übergabeparametern zwischen den Intents
 * Created by woors on 07.03.2017.
 */

public interface IntentParameter {
    String FROM_MAIN_GAME = "fromMainGame";
    String PLAYER_LIST = "playerList";
    String CURRENT_PLAYER = "currentPlayer";
    String LAST_GAME = "lastGame";

    interface MainGame {
        String CURRENT_TASK = "currentTask";
        String CURRENT_MINI_GAME = "currentMiniGame";
        String CURRENT_TASK_IS_MINI_GAME = "currentTaskIsMiniGame";
        String AD_COUNTER = "adCounter";
        String NEW_GAME = "newGame";
    }

    interface ChoosePlayersActivity {
        String SELECTION_AMOUNT = "selectionAmount";
    }
}
