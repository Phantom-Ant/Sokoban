package io.github.Sokoban;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Player extends Image {
    private int posX;
    private int posY;
    Texture texture;

    public Player(Texture texture, int posX,int posY){
        this.texture = texture;
        this.posX = posX;
        this.posY = posY;
    }

    public void moveBy(int x, int y){
        posX += x;
        posY += y;
    }
    public void draw(SpriteBatch batch){
        batch.draw(texture, posX, posY, 1, 1);
    }

}
