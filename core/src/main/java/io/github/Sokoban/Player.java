package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Player extends Sprite implements GestureDetector.GestureListener {

    public Player(Texture texture){
        super(texture);
    }

    @Override
    public void translateX(float xAmount) {
        setTexture(new Texture("img/ominoLato.jpg"));
        setFlip(xAmount<0, false);
        super.translateX(xAmount);
    }

    @Override
    public void translateY(float yAmount) {
        if(yAmount<0){
            setTexture(new Texture("img/ominoDavanti.jpg"));
        }else{
            setTexture(new Texture("img/ominoDietro.jpg"));
        }
        flip(isFlipX(), false);
        super.translateY(yAmount);
    }

    //Gesture listener
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {return false;}
    @Override
    public boolean tap(float x, float y, int count, int button) {return false;}
    @Override
    public boolean longPress(float x, float y) {return false;}
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Vector2 vec2 = new Vector2(velocityX, velocityY);
        if(vec2.len() > 500){
            if(Math.abs(velocityX) > Math.abs(velocityY)){
                translateX(velocityX>0? 1 : -1);
            }else{
                translateY(velocityY>0? -1 : 1);
            }
        }
        return true;
    }
    //pan vs fling which to choose?
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return true;
    }
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {return false;}
    @Override
    public boolean zoom(float initialDistance, float distance) {return false;}
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {return false;}
    @Override
    public void pinchStop() {}
    //End gesture handling

    @Override
    public void draw(Batch batch){
        super.draw(batch);
    }
    @Override
    public void draw(Batch batch, float alphaModulation){
        super.draw(batch, alphaModulation);
    }
}
