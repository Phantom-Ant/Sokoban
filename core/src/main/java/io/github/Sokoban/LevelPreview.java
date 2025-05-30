package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

public class LevelPreview extends Table { //TODO FIX
    private final String[] xsbs;
    private final int width, height;
    private float prefWidth = 100f;
    private float prefHeight = 100f;

    public LevelPreview(String xsbData) {
        xsbs = xsbData.split("\n");
        height = xsbs.length;
        width = xsbs[0].length();
    }

    @Override
    public void layout() {
        //super.layout();

        // Recalculate preferred height based on actual width
        float actualWidth = getWidth(); // This is the width assigned by layout
        float tileSize = actualWidth / width;
        prefWidth = actualWidth;
        prefHeight = tileSize * height;

        // You can optionally invalidate if height changes and layout needs to reflow
        invalidateHierarchy(); // triggers parent layout if needed
    }

    @Override
    public float getPrefWidth() {
        return prefWidth;
    }

    @Override
    public float getPrefHeight() {
        return prefHeight;
    }

    @Override
    public float getMinWidth() {
        return 0;
    }

    @Override
    public float getMinHeight() {
        return 0;
    }

    @Override
    public float getMaxWidth() {
        return Float.MAX_VALUE;
    }

    @Override
    public float getMaxHeight() {
        return Float.MAX_VALUE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.flush();

        applyTransform(batch, computeTransform());

        // Calculate tile size to fit inside actor bounds while preserving aspect ratio
        float tileSizeX = getWidth() / width;
        float tileSizeY = getHeight() / height;
        float tileSize = Math.min(tileSizeX, tileSizeY);

        float totalWidth = tileSize * width;
        float totalHeight = tileSize * height;

        // Center the level preview inside this actor's bounds
        float offsetX = (getWidth() - totalWidth) / 2f;
        float offsetY = (getHeight() - totalHeight) / 2f;

        char c;
        Texture tex;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                c = xsbs[i].charAt(j);

                switch (c) {
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
                }

                if (tex != null) {
                    batch.draw(tex, offsetX+j*tileSize, offsetY+(height-1-i)*tileSize, tileSize, tileSize);
                }
            }
        }

        batch.flush();

        resetTransform(batch);
    }
}
