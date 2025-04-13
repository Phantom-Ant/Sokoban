package io.github.Sokoban;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Box extends Sprite {
    private boolean placed;

    public Box(Texture texture){
        super(texture);
    }
    public Box(boolean placed){
        if(placed){
            setTexture(new Texture("img/boxPlaced.png")); //TODO parametrize
        }else{
            setTexture(new Texture("img/box.png")); //TODO parametrize
        }
    }
    public boolean isPlaced(){
        return placed;
    }
    public void setPlaced(boolean placed) {
        if(placed){
            setTexture(new Texture("img/boxPlaced.png")); //TODO parametrize
        }else{
            setTexture(new Texture("img/box.png")); //TODO parametrize
        }
        this.placed = placed;
    }

    //could be useful overriding these
    @Override
    public void draw(Batch batch){
        super.draw(batch);
    }
    @Override
    public void draw(Batch batch, float alphaModulation){
        super.draw(batch, alphaModulation);
    }
}
