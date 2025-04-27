package io.github.Sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Sokoban extends Game {
    static public Skin gameSkin;
    static public BitmapFont font; //default font

    /// textures
    static public Texture tex_playerFront;
    static public Texture tex_playerBack;
    static public Texture tex_playerSide;
    static public Texture tex_wall;
    static public Texture tex_floor;
    static public Texture tex_target;
    static public Texture tex_box;
    static public Texture tex_boxPlaced;
    ///


    @Override
    public void create() {
        gameSkin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        font = new BitmapFont(Gdx.files.internal("skins/default.fnt"));
        font.getData().setScale(4.0f);

        ///textures
        tex_playerFront = new Texture("img/playerFront.jpg");//TODO use transparent image for player
        tex_playerBack = new Texture("img/playerBack.jpg");//TODO use transparent image for player
        tex_playerSide = new Texture("img/playerSide.jpg");//TODO use transparent image for player
        tex_wall = new Texture("img/wall.png"); //TODO use better image for walls
        tex_floor = new Texture("img/floor.png");
        tex_target = new Texture("img/target.png");//TODO use better image for targets
        tex_box = new Texture("img/box.png");
        tex_boxPlaced = new Texture("img/boxPlaced.png");
        ///

        this.setScreen(new LevelsScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
