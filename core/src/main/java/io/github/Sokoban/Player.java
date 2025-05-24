package io.github.Sokoban;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements GestureDetector.GestureListener {

    private boolean dirty; //needs to be drawn/updated

    //facing direction
    private int directionX;
    private int directionY;

    //float timer; //temp, for pan
    public Player(){
        super(Sokoban.tex_playerFront);
        face(0,-1);
        dirty=true;
    }
    public Player(int x, int y){
        super(Sokoban.tex_playerFront);
        face(x,y);
        dirty=true;
    }
    public void moveX(float x){
        if(x<0||x>0){
            super.translateX(x);
        }
    }
    public void moveY(float y) {
        if(y>0||y<0){
            super.translateY(y);
        }
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public void face(int x, int y){//direction TODO rewrite removing flip and using playerLeft and playerRight
        directionX=x;
        directionY=y;
        if(y==0){
            setFlip(x<0, false);
            setTexture(Sokoban.tex_playerSide);
        }
        if(x==0){
            flip(isFlipX(), false);
            if(y<0){
                setTexture(Sokoban.tex_playerFront);
            }else if(y>0){
                setTexture(Sokoban.tex_playerBack);
            }
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
            dirty = true;

            if(Math.abs(velocityX) > Math.abs(velocityY)){ //which axis flings more?

                setTexture(Sokoban.tex_playerSide);


                directionX = velocityX>0? 1 : -1;
                directionY = 0;

                setFlip(velocityX<0, false);

            }else{
                directionX=0;
                flip(isFlipX(), false);
                if(velocityY>0){
                    directionY=-1;
                    setTexture(Sokoban.tex_playerFront);
                }else if(velocityY<0){
                    directionY=1;
                    setTexture(Sokoban.tex_playerBack);
                }
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

    public void invalidate(){
        dirty = true;
    }

    boolean isDirty(){
        return dirty;
    }

    @Override
    public void draw(Batch batch){
        super.draw(batch);
        dirty = false;
    }
    @Override
    public void draw(Batch batch, float alphaModulation){
        super.draw(batch, alphaModulation);
    }
}
