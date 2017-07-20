package com.tomade.saufomat.model;

import com.tomade.saufomat.model.player.Player;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woors on 04.04.2016.
 */
public class PlayerTest {

    @Test
    public void constructorTest1() {
        int id = new Player().getId();
        Assert.assertNotSame("Zwei Spieler mit sleber ID", id, new Player().getId());
    }

    @Test
    public void getPlayerByIdTest() {
        List<Player> playerList = new ArrayList<>();
        Player p = new Player();
        playerList.add(p);
        playerList.add(new Player());
        playerList.add(new Player());
        Assert.assertEquals("Falscher Spieler empfangen", p, Player.getPlayerById(playerList, p.getId()));
    }

    @Test
    public void getPlayerByIdTestFail() {
        List<Player> playerList = new ArrayList<>();
        Player p = new Player();
        playerList.add(p);
        playerList.add(new Player());
        playerList.add(new Player());
        Assert.assertNull("Falscher Spieler empfangen", Player.getPlayerById(playerList, p.getId() - 1));
    }
}