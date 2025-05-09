package io.github.Sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Sokoban extends Game {
    public Skin skin;

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

    public String backend_url;

    public User user; //TEMP TEST
    public Screen previousScreen; //TEMP TEST

    public Screen title_screen;

    @Override
    public void create() {//TODO replace static texture variables in player and box

        skin = new Skin(Gdx.files.internal("skins/metalui/metal-ui.json"));

        skin.getFont("font").getData().setScale(4.0f);


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

        backend_url = "http://10.0.2.2/PHP/Sokoban/";
        title_screen = new TitleScreen(this);
        this.setScreen(title_screen);
    }

    @Override
    public void setScreen(Screen screen) {
        previousScreen = getScreen();
        super.setScreen(screen);
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
