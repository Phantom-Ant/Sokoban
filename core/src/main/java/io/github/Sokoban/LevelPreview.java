package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class LevelPreview extends Table { //TODO FIX
    private String[] xsbs;
    public LevelPreview(String xsbData){
        xsbs = xsbData.split("\n");
    }
    @Override
    public void layout() {
        super.layout();
    }
    @Override
    public float getPrefWidth() {
        return 100f;
    }

    @Override
    public float getPrefHeight() {
        return 100f;
    }

    @Override
    public float getMinWidth() {
        return 0; // allow shrinking
    }

    @Override
    public float getMaxWidth() {
        return Float.MAX_VALUE; // allow growing
    }
    @Override
    public float getMinHeight() {
        return 0; // allow shrinking
    }

    @Override
    public float getMaxHeight() {
        return Float.MAX_VALUE; // allow growing
    }
    @Override
    public void draw(Batch batch, float parentAlpha){
        Gdx.app.log("LevelPreview", "batch transform: " + batch.getTransformMatrix());

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // Save the batch transform matrix
        batch.flush(); // finish previous batch drawing

        // Apply the transform for this actor, including translation, scale, rotation
        applyTransform(batch, computeTransform());

        int height = xsbs.length;
        int width = xsbs[0].length();

        char c;
        Texture tex;

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                c = xsbs[i].charAt(j);

                switch(c){
                    case '-':
                        tex = Sokoban.tex_floor;
                        break;
                    case '#':
                        tex = Sokoban.tex_wall;
                        break;
                    case '@':
                    case '+':
                        tex = Sokoban.tex_playerFront;
                        break;
                    case '$':
                        tex = Sokoban.tex_box;
                        break;
                    case '*':
                        tex = Sokoban.tex_boxPlaced;
                        break;
                    case '.':
                        tex = Sokoban.tex_target;
                        break;
                    default:
                        tex = null;
                        break;
                }

                if(tex != null) {
                    batch.draw(tex, j*getWidth()/width, (height-1-i)*getHeight()/height, getWidth()/width, getHeight()/height);
                }
            }
        }
        batch.flush();

        // Reset transform so subsequent draw calls are unaffected
        resetTransform(batch);
    }
}
