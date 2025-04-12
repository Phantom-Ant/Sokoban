package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements GestureDetector.GestureListener {
    boolean canMoveRight;
    boolean canMoveLeft;
    boolean canMoveUp;
    boolean canMoveDown;
    //float timer; //temp, for pan
    public Player(Texture texture){ //TODO (maybe) use texture packer
        super(texture);
        canMoveRight = true;
        canMoveLeft = true;
        canMoveUp = true;
        canMoveDown = true;

    }

    public void moveX(float x){

        if(x<0 || x>0){
            setFlip(x<0, false);
        }
        if((canMoveLeft && x<0) || (canMoveRight && x>0)){
            super.translateX(x);
        }
    }

    public void moveY(float y) {
        flip(isFlipX(), false);
        if((canMoveUp && y>0) || (canMoveDown && y<0)){
            super.translateY(y);
        }
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
        /**/
        Vector2 fling = new Vector2(velocityX, velocityY);
        if(fling.len() > 500){ //idk arbitrary value for velocity

            if(Math.abs(velocityX) > Math.abs(velocityY)){ //which axis flings more?

                setTexture(new Texture("img/playerSide.jpg"));//TODO parametrize
                //horizontal positive?canMove?
                moveX(velocityX>0? 1 : -1);
            }else{
                if(velocityY>0){
                    setTexture(new Texture("img/playerFront.jpg"));//TODO parametrize
                }else if(velocityY<0){
                    setTexture(new Texture("img/playerBack.jpg"));//TODO parametrize
                }
                //vertical positive?canMove?
                moveY(velocityY>0? -1 : 1);
            }
        }
        return true;
        //return false;
    }
    //for now i prefer using fling
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        /*
        float delta = Gdx.graphics.getDeltaTime();
        timer += delta;

        if(timer>.2f){ //another arbitrary value
            timer = 0f;
            if(Math.abs(deltaX) > Math.abs(deltaY)){
                translateX(deltaX>0? 1 : -1);
            }else{
                translateY(deltaY>0? -1 : 1);
            }
        }
        return true;
        */
        return false;
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
