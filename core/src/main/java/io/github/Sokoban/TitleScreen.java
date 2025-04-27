package io.github.Sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TitleScreen implements Screen {
    Game game;
    Stage stage;

    Table table;
    TextButton btnStart;

    public TitleScreen(Game aGame){
        game = aGame;
        stage = new Stage(new ScreenViewport());

        table = new Table();
        table.setFillParent(true);

        TextButton.TextButtonStyle txtbtnStyle = new TextButton.TextButtonStyle();
        txtbtnStyle.font = Sokoban.font;

        btnStart = new TextButton("Levels", txtbtnStyle);
        btnStart.getLabel().setAlignment(Align.center);

        btnStart.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {return true;}
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new LevelsScreen(game));
            }
        });
        //table.add(lblTitle).center();
        table.add(btnStart).center();
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.act();
        stage.draw();
    }
    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        stage.dispose();
    }
}
