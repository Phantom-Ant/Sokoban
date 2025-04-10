package io.github.Sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Sokoban extends Game {
    static public Skin gameSkin;
    static public BitmapFont font; //default font

    static public int width; //temp
    static public int height; //temp

    @Override
    public void create() {
        width = 8;//TEST
        height = 8;//TEST

        gameSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        font = new BitmapFont(Gdx.files.internal("skins/default.fnt"));
        font.getData().setScale(10.0f);

        this.setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
