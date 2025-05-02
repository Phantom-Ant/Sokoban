package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TitleScreen implements Screen {
    ScreenViewport viewport;

    Sokoban game;
    Stage stage;

    Table root, tblUp, tblCenter;
    Label lblTitle, lblUser;
    TextButton btnStart, btnAccount;

    public TitleScreen(Sokoban aGame){
        viewport = new ScreenViewport();

        game = aGame;
        stage = new Stage(viewport);

        lblTitle = new Label("SOKOBAN", Sokoban.skin);
        lblTitle.setFontScale(14f);

        String strUser = ""; //TODO use a better approach
        if(game.user != null){
            strUser = game.user.username;
        }else{
            strUser = "No user";
        }

        lblUser = new Label(strUser, game.skin);
        lblUser.setAlignment(Align.center);

        btnStart = new TextButton("Levels", Sokoban.skin);
        btnAccount = new TextButton("Account", Sokoban.skin);

        onChange(btnStart, () -> game.setScreen(new LevelsScreen(game)));
        onChange(btnAccount, () -> {
            game.setScreen(new LoginScreen(game));
        });

        root = new Table();
        root.setFillParent(true);

        tblUp = new Table();
        tblUp.defaults().width(Value.percentWidth(0.3f, root));
        tblUp.defaults().height(120f);
        tblUp.defaults().expandX();

        tblUp.add(lblUser).right();
        tblUp.add(btnAccount).right();

        tblCenter = new Table();
        tblCenter.defaults().width(Value.percentWidth(0.3f, root));
        tblCenter.defaults().height(120f);
        tblCenter.defaults().space(20f);

        tblCenter.add(btnStart).row();
        //tblCenter.add(new TextButton("hi", Sokoban.gameSkin)).bottom().row();

        root.add(tblUp).growX().row();
        root.add(lblTitle).height(Value.percentHeight(0.3f, root)).row();
        root.add(tblCenter).expand().center();

        //stage.setDebugAll(true); //click 1:1 to see lines better
        stage.addActor(root);
    }

    public static void onChange(Actor actor, Runnable runnable){
        actor.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                runnable.run();
            }
        });
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void render(float delta) {
        logic();
        draw();
    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}

    public void logic(){
        if(game.user != null){
            lblUser.setText(game.user.username);
        }else{
            lblUser.setText("No user");
        }
    }

    public void draw(){
        ScreenUtils.clear(Color.WHITE); //TEST
        stage.act();
        stage.draw();
    }


    @Override
    public void hide() {}
    @Override
    public void dispose() {
        stage.dispose();
    }
}
