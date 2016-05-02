package de.tomade.saufomat2.model.card;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by woors on 04.04.2016.
 */
public class CardTest {

    @Test
    public void constructorTest1() {
        Card c = new Card(CardColor.CLUB, CardValue.ACE);
        Assert.assertEquals(c.getColor(), CardColor.CLUB);
        Assert.assertEquals(c.getValue(), CardValue.ACE);
    }

    @Test
    public void constructorTest2() {
        Card c = new Card(CardColor.CLUB, 0);
        Assert.assertEquals(c.getValue(), CardValue.TWO);
    }

    @Test
    public void constructorTest3() {
        Card c = new Card(0, CardValue.ACE);
        Assert.assertEquals(c.getColor(), CardColor.DIAMOND);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorTest2Fail() {
        Card c = new Card(CardColor.CLUB, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorTest2Fail2() {
        Card c = new Card(CardColor.CLUB, 13);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorTest3Fail() {
        Card c = new Card(-1, CardValue.ACE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorTest3Fail2() {
        Card c = new Card(4, CardValue.ACE);
    }


    @Test
    public void testGetRandomCard() throws Exception {
        Assert.assertNotNull(Card.getRandomCard());
    }

    @Test
    public void testIsRedDiamond() throws Exception {
        Card rCard = new Card(CardColor.DIAMOND, 1);
        Assert.assertTrue(rCard.isRed());
    }

    @Test
    public void testIsRedHeart() throws Exception {
        Card rCard = new Card(CardColor.HEART, 1);
        Assert.assertTrue(rCard.isRed());
    }

    @Test
    public void testIsRedClub() throws Exception {
        Card rCard = new Card(CardColor.CLUB, 1);
        Assert.assertFalse(rCard.isRed());
    }

    @Test
    public void testIsRedSpade() throws Exception {
        Card rCard = new Card(CardColor.SPADE, 1);
        Assert.assertFalse(rCard.isRed());
    }

    @Test
    public void testIsHigherAs() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Assert.assertTrue(c2.isHigherAs(c1));
    }

    @Test
    public void testIsHigherAsFail() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Assert.assertFalse(c1.isHigherAs(c2));
    }

    @Test
    public void testIsHigherAsFail2() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 3);
        Card c2 = new Card(CardColor.CLUB, 3);
        Assert.assertFalse(c1.isHigherAs(c2));
    }

    @Test
    public void testIsBetween() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Card c3 = new Card(CardColor.CLUB, 4);
        Assert.assertTrue(c2.isBetween(c1, c3));
    }

    @Test
    public void testIsBetween2() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Card c3 = new Card(CardColor.CLUB, 4);
        Assert.assertTrue(c2.isBetween(c3, c1));
    }

    @Test
    public void testIsBetweenLower() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Card c3 = new Card(CardColor.CLUB, 4);
        Assert.assertFalse(c1.isBetween(c2, c3));
    }

    @Test
    public void testIsBetweenLower2() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Card c3 = new Card(CardColor.CLUB, 4);
        Assert.assertFalse(c1.isBetween(c3, c2));
    }

    @Test
    public void testIsBetweenHigher() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Card c3 = new Card(CardColor.CLUB, 4);
        Assert.assertFalse(c3.isBetween(c1, c2));
    }

    @Test
    public void testIsBetweenHigher2() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Card c3 = new Card(CardColor.CLUB, 4);
        Assert.assertFalse(c3.isBetween(c2, c1));
    }

    @Test
    public void testIsBetweenEqual1() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 2);
        Card c3 = new Card(CardColor.CLUB, 4);
        Assert.assertFalse(c2.isBetween(c1, c3));
    }

    @Test
    public void testIsBetweenEqual2() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 3);
        Card c3 = new Card(CardColor.CLUB, 3);
        Assert.assertFalse(c2.isBetween(c1, c3));
    }

    @Test
    public void testIsBetweenEqual3() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 2);
        Card c2 = new Card(CardColor.CLUB, 2);
        Card c3 = new Card(CardColor.CLUB, 2);
        Assert.assertFalse(c2.isBetween(c1, c3));
    }

    @Test
    public void testEquals() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 1);
        Card c2 = new Card(CardColor.CLUB, 1);

        Assert.assertTrue(c1.equals(c2));
    }

    @Test
    public void testEqualsFail() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 1);
        Card c2 = new Card(CardColor.CLUB, 2);

        Assert.assertFalse(c1.equals(c2));
    }

    @Test
    public void testEqualsFail2() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 1);
        Card c2 = new Card(CardColor.DIAMOND, 1);

        Assert.assertFalse(c1.equals(c2));
    }

    @Test
    public void testEqualsFail3() throws Exception {
        Card c1 = new Card(CardColor.CLUB, 1);
        Card c2 = new Card(CardColor.DIAMOND, 2);

        Assert.assertFalse(c1.equals(c2));
    }

    @Test
    public void getCardValueAsIntTest() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        Assert.assertTrue(c1.getValueAsInt() == 12);
    }

    @Test
    public void setCardColorAsIntTest() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setColor(3);
        Assert.assertEquals(c1.getColor(), CardColor.SPADE);
    }

    @Test
    public void setCardValueAsIntTest() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(3);
        Assert.assertEquals(c1.getValue(), CardValue.FIFE);
    }

    @Test
    public void getRandomCardHigher7Test() {
        Card c = Card.getRandomCard7OrHigher();
        Assert.assertTrue("Karte > 7: " + c.toString(), c.getValueAsInt() >= 5);
    }
}