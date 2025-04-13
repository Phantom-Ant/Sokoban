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
            setTexture(Sokoban.tex_boxPlaced);
        }else{
            setTexture(Sokoban.tex_box);
        }
    }
    public boolean isPlaced(){
        return placed;
    }
    public void setPlaced(boolean placed) {
        if(placed){
            setTexture(Sokoban.tex_boxPlaced);
        }else{
            setTexture(Sokoban.tex_box);
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
