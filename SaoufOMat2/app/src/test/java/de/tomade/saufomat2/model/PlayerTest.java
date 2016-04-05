package de.tomade.saufomat2.model;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by woors on 04.04.2016.
 */
public class PlayerTest {

    @Test
    public void ConstructorTest1() {
        int id = new Player().getId();
        Assert.assertNotSame("Zwei Spieler mit sleber ID", id, new Player().getId());
    }

    @Test
    public void ConstructorTest2() {
        String name = "test";
        int weight = 1;
        boolean isMan = true;
        int drinks = 0;
        int nextPlayerId = 0;
        int lastPlayerId = 0;
        Player p = new Player(name, weight, isMan, drinks, nextPlayerId, lastPlayerId);

        Assert.assertEquals("Construktorfehler", p.getName(), name);
        Assert.assertEquals("Construktorfehler", p.getWeight(), weight);
        Assert.assertEquals("Construktorfehler", p.getIsMan(), isMan);
        Assert.assertEquals("Construktorfehler", p.getDrinks(), drinks);
        Assert.assertEquals("Construktorfehler", p.getNextPlayerId(), nextPlayerId);
        Assert.assertEquals("Construktorfehler", p.getLastPlayerId(), lastPlayerId);
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