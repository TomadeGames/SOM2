package de.tomade.saufomat2.activity.miniGames.memory;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by woors on 30.05.2016.
 */
public class MemoryField {
    private static final String TAG = MemoryField.class.getSimpleName();
    private final int FIELD_SIZE = 20;
    private int SYMBOL_WIDTH = 50;
    private int SYMBOL_HEIGHT = 50;
    private int SYMBOL_DIFF_X = 25;
    private int SYMBOL_DIFF_Y = 25;

    private int screenWidth;
    private int screenHeight;
    private Bitmap[] icons = new Bitmap[5];
    private Bitmap backside;
    private MemorySymbol[] memorySymbols = new MemorySymbol[FIELD_SIZE];
    private MemoryAnimationListener listener;
    private boolean fieldLoaded = false;

    public MemoryField(MemoryAnimationListener listener, Bitmap beer, Bitmap crate, Bitmap shot, Bitmap cocktail, Bitmap dice, Bitmap backside, int screenWidth, int screenHeight){
        this.icons[MemoryPicture.BEER] = beer;
        this.icons[MemoryPicture.COCKTAIL] = cocktail;
        this.icons[MemoryPicture.CRATE] = crate;
        this.icons[MemoryPicture.SHOT] = shot;
        this.icons[MemoryPicture.GAME] = dice;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.backside = backside;
        this.listener = listener;

        this.SYMBOL_WIDTH = screenWidth / 5;
        this.SYMBOL_HEIGHT = SYMBOL_WIDTH;
        this.SYMBOL_DIFF_X = SYMBOL_WIDTH / 4;
        this.SYMBOL_DIFF_Y = SYMBOL_HEIGHT / 4;

        this.createField();
        this.fieldLoaded = true;
    }

    public void turnSymbolUp(int x, int y){
        int index = x + 5 * y;
        memorySymbols[index].FlipUp();
    }

    public void turnSymbolDown(int x, int y){
        int index = x + 5 * y;
        memorySymbols[index].FlipDown();
    }

    private void createField(){
        Random random = new Random();
        List<Integer> indexPool = new ArrayList<>();
        for(int i = 0; i < FIELD_SIZE; i++){
            indexPool.add(i);
        }

        List<Integer> picturePool = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            picturePool.add(i%5);
        }

        for(int index = 0; index < FIELD_SIZE; index++){
            int pictureIndex = random.nextInt(picturePool.size());
            int pictureId = picturePool.get(pictureIndex);
            picturePool.remove(pictureIndex);

            int posX = SYMBOL_DIFF_X + SYMBOL_WIDTH / 2 + getXPosByIndex(index) - getXPosByIndex(1);


            int y = -1;
            for(int i=index; i >= 0; y++){
                i -= 5;
            }
            int posY = SYMBOL_DIFF_Y + SYMBOL_HEIGHT / 2 + getYPosByIndex(y) - getYPosByIndex(1);

            memorySymbols[index] = new MemorySymbol(pictureId, icons[pictureId], backside, posX, posY, SYMBOL_WIDTH, SYMBOL_HEIGHT);
            memorySymbols[index].AddAnimationListener(listener);
            Log.d(TAG, "Bild: " + pictureId + " an index: " + index + " X: " + posX + " Y: " + posY);
        }
    }

    private int getXPosByIndex(int index){
        return (index % 5) * (SYMBOL_DIFF_X + SYMBOL_WIDTH);
    }

    private int getYPosByIndex(int index){
        return index * (SYMBOL_DIFF_Y + SYMBOL_HEIGHT);
    }

    public void draw(Canvas canvas){
        if(fieldLoaded) {
            for (MemorySymbol m : memorySymbols) {
                m.draw(canvas);
            }
        }
    }

    public void moveField(float dX, float dY){
        for(MemorySymbol m: memorySymbols){
            m.setX((int)(m.getX() + dX));
            m.setY((int)(m.getY() + dY));
        }
    }
}
