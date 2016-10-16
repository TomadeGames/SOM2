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

    @Test
    public void constructorTest4() {
        Card c = new Card(0, 0);
        Assert.assertEquals(c.getColor(), CardColor.DIAMOND);
        Assert.assertEquals(c.getValue(), CardValue.TWO);

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
        for (int i = 0; i < 12; i++) {
            Card rCard = new Card(CardColor.DIAMOND, i);
            Assert.assertTrue(rCard.isRed());
        }
    }

    @Test
    public void testIsRedHeart() throws Exception {
        for (int i = 0; i < 12; i++) {
            Card rCard = new Card(CardColor.HEART, i);
            Assert.assertTrue(rCard.isRed());
        }
    }

    @Test
    public void testIsRedClub() throws Exception {
        for (int i = 0; i < 12; i++) {
            Card rCard = new Card(CardColor.CLUB, i);
            Assert.assertFalse(rCard.isRed());
        }
    }

    @Test
    public void testIsRedSpade() throws Exception {
        for (int i = 0; i < 12; i++) {
            Card rCard = new Card(CardColor.SPADE, i);
            Assert.assertFalse(rCard.isRed());
        }
    }

    @Test
    public void testIsHigherAs() throws Exception {
        for (int i = 0; i < 11; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Assert.assertTrue(c2.isHigherAs(c1));
        }
    }

    @Test
    public void testIsHigherAsFail() throws Exception {
        for (int i = 0; i < 11; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Assert.assertFalse(c1.isHigherAs(c2));
        }
    }

    @Test
    public void testIsHigherAsFail2() throws Exception {
        for (int i = 0; i < 12; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i);
            Assert.assertFalse(c1.isHigherAs(c2));
        }
    }

    @Test
    public void testIsBetween() throws Exception {
        for (int i = 0; i < 10; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Card c3 = new Card(CardColor.CLUB, i + 2);
            Assert.assertTrue(c2.isBetween(c1, c3));
        }
    }

    @Test
    public void testIsBetween2() throws Exception {
        for (int i = 0; i < 10; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Card c3 = new Card(CardColor.CLUB, i + 2);
            Assert.assertTrue(c2.isBetween(c3, c1));
        }
    }

    @Test
    public void testIsBetweenLower() throws Exception {
        for (int i = 0; i < 10; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Card c3 = new Card(CardColor.CLUB, i + 2);
            Assert.assertFalse(c1.isBetween(c2, c3));
        }
    }

    @Test
    public void testIsBetweenLower2() throws Exception {
        for (int i = 0; i < 10; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Card c3 = new Card(CardColor.CLUB, i + 2);
            Assert.assertFalse(c1.isBetween(c3, c2));
        }
    }

    @Test
    public void testIsBetweenHigher() throws Exception {
        for (int i = 0; i < 10; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Card c3 = new Card(CardColor.CLUB, i + 2);
            Assert.assertFalse(c3.isBetween(c1, c2));
        }
    }

    @Test
    public void testIsBetweenHigher2() throws Exception {
        for (int i = 0; i < 10; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Card c3 = new Card(CardColor.CLUB, i + 2);
            Assert.assertFalse(c3.isBetween(c2, c1));
        }
    }

    @Test
    public void testIsBetweenEqual1() throws Exception {
        for (int i = 0; i < 10; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i);
            Card c3 = new Card(CardColor.CLUB, i + 2);
            Assert.assertFalse(c2.isBetween(c1, c3));
        }
    }

    @Test
    public void testIsBetweenEqual2() throws Exception {
        for (int i = 0; i < 11; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Card c3 = new Card(CardColor.CLUB, i + 1);
            Assert.assertFalse(c2.isBetween(c1, c3));
        }
    }

    @Test
    public void testIsBetweenEqual3() throws Exception {
        for (int i = 0; i < 12; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i);
            Card c3 = new Card(CardColor.CLUB, i);
            Assert.assertFalse(c2.isBetween(c1, c3));
        }
    }

    @Test
    public void testEquals() throws Exception {
        for (int i = 0; i < 12; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i);

            Assert.assertTrue(c1.equals(c2));
        }
    }

    @Test
    public void testEqualsFail() throws Exception {
        for (int i = 0; i < 11; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.CLUB, i + 1);
            Assert.assertFalse(c1.equals(c2));
        }
    }

    @Test
    public void testEqualsFail2() throws Exception {
        for (int i = 0; i < 12; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.DIAMOND, i);

            Assert.assertFalse(c1.equals(c2));
        }
    }

    @Test
    public void testEqualsFail3() throws Exception {
        for (int i = 0; i < 11; i++) {
            Card c1 = new Card(CardColor.CLUB, i);
            Card c2 = new Card(CardColor.DIAMOND, i + 1);

            Assert.assertFalse(c1.equals(c2));
        }
    }

    @Test
    public void getCardValueAsIntTestAce() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        Assert.assertTrue(c1.getValueAsInt() == 12);
    }

    @Test
    public void getCardValueAsIntTestKing() {
        Card c1 = new Card(CardColor.CLUB, CardValue.KING);
        Assert.assertTrue(c1.getValueAsInt() == 11);
    }

    @Test
    public void getCardValueAsIntTestQueen() {
        Card c1 = new Card(CardColor.CLUB, CardValue.QUEEN);
        Assert.assertTrue(c1.getValueAsInt() == 10);
    }

    @Test
    public void getCardValueAsIntTestJack() {
        Card c1 = new Card(CardColor.CLUB, CardValue.JACK);
        Assert.assertTrue(c1.getValueAsInt() == 9);
    }

    @Test
    public void getCardValueAsIntTest10() {
        Card c1 = new Card(CardColor.CLUB, CardValue.TEN);
        Assert.assertTrue(c1.getValueAsInt() == 8);
    }

    @Test
    public void getCardValueAsIntTest9() {
        Card c1 = new Card(CardColor.CLUB, CardValue.NINE);
        Assert.assertTrue(c1.getValueAsInt() == 7);
    }

    @Test
    public void getCardValueAsIntTest8() {
        Card c1 = new Card(CardColor.CLUB, CardValue.EIGHT);
        Assert.assertTrue(c1.getValueAsInt() == 6);
    }

    @Test
    public void getCardValueAsIntTest7() {
        Card c1 = new Card(CardColor.CLUB, CardValue.SEVEN);
        Assert.assertTrue(c1.getValueAsInt() == 5);
    }

    @Test
    public void getCardValueAsIntTest6() {
        Card c1 = new Card(CardColor.CLUB, CardValue.SIX);
        Assert.assertTrue(c1.getValueAsInt() == 4);
    }

    @Test
    public void getCardValueAsIntTest5() {
        Card c1 = new Card(CardColor.CLUB, CardValue.FIFE);
        Assert.assertTrue(c1.getValueAsInt() == 3);
    }

    @Test
    public void getCardValueAsIntTest4() {
        Card c1 = new Card(CardColor.CLUB, CardValue.FOUR);
        Assert.assertTrue(c1.getValueAsInt() == 2);
    }

    @Test
    public void getCardValueAsIntTest3() {
        Card c1 = new Card(CardColor.CLUB, CardValue.THREE);
        Assert.assertTrue(c1.getValueAsInt() == 1);
    }

    @Test
    public void getCardValueAsIntTest2() {
        Card c1 = new Card(CardColor.CLUB, CardValue.TWO);
        Assert.assertTrue(c1.getValueAsInt() == 0);
    }


    @Test
    public void setCardColorAsIntTestSpade() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setColor(3);
        Assert.assertEquals(c1.getColor(), CardColor.SPADE);
    }

    @Test
    public void setCardColorAsIntTestClub() {
        Card c1 = new Card(CardColor.SPADE, CardValue.ACE);
        c1.setColor(2);
        Assert.assertEquals(c1.getColor(), CardColor.CLUB);
    }

    @Test
    public void setCardColorAsIntTestHeart() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setColor(1);
        Assert.assertEquals(c1.getColor(), CardColor.HEART);
    }

    @Test
    public void setCardColorAsIntTestDiamond() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setColor(0);
        Assert.assertEquals(c1.getColor(), CardColor.DIAMOND);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCardColorAsIntTestToLow() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setColor(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCardColorAsIntTestToHigh() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setColor(5);
    }

    @Test
    public void setCardValueAsIntTestTwo() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(0);
        Assert.assertEquals(c1.getValue(), CardValue.TWO);
    }

    @Test
    public void setCardValueAsIntTestThree() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(1);
        Assert.assertEquals(c1.getValue(), CardValue.THREE);
    }

    @Test
    public void setCardValueAsIntTestFour() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(2);
        Assert.assertEquals(c1.getValue(), CardValue.FOUR);
    }

    @Test
    public void setCardValueAsIntTestFive() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(3);
        Assert.assertEquals(c1.getValue(), CardValue.FIFE);
    }

    @Test
    public void setCardValueAsIntTestSix() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(4);
        Assert.assertEquals(c1.getValue(), CardValue.SIX);
    }

    @Test
    public void setCardValueAsIntTestSeven() {
        Card c1 = new Card(CardColor.HEART, CardValue.ACE);
        c1.setValue(5);
        Assert.assertEquals(c1.getValue(), CardValue.SEVEN);
    }

    @Test
    public void setCardValueAsIntTestEight() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(6);
        Assert.assertEquals(c1.getValue(), CardValue.EIGHT);
    }

    @Test
    public void setCardValueAsIntTestNine() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(7);
        Assert.assertEquals(c1.getValue(), CardValue.NINE);
    }

    @Test
    public void setCardValueAsIntTestTen() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(8);
        Assert.assertEquals(c1.getValue(), CardValue.TEN);
    }

    @Test
    public void setCardValueAsIntTestJack() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(9);
        Assert.assertEquals(c1.getValue(), CardValue.JACK);
    }

    @Test
    public void setCardValueAsIntTestQueen() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(10);
        Assert.assertEquals(c1.getValue(), CardValue.QUEEN);
    }

    @Test
    public void setCardValueAsIntTestKing() {
        Card c1 = new Card(CardColor.CLUB, CardValue.ACE);
        c1.setValue(11);
        Assert.assertEquals(c1.getValue(), CardValue.KING);
    }

    @Test
    public void setCardValueAsIntTestAce() {
        Card c1 = new Card(CardColor.CLUB, CardValue.TWO);
        c1.setValue(12);
        Assert.assertEquals(c1.getValue(), CardValue.ACE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCardValueAsIntTestToLow() {
        Card c1 = new Card(CardColor.CLUB, CardValue.TWO);
        c1.setValue(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCardValueAsIntTestToHigh() {
        Card c1 = new Card(CardColor.CLUB, CardValue.TWO);
        c1.setValue(13);
    }

    @Test
    public void getRandomCardHigher7Test() {
        for(int i = 0; i < 64; i++) {
            Card c = Card.getRandomCard7OrHigher();
            Assert.assertTrue("Karte > 7: " + c.toString(), c.getValueAsInt() >= 5);
        }
    }
}