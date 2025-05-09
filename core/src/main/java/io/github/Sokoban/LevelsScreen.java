package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelsScreen implements Screen {
    Sokoban game;
    Stage stage;


    Table tblList;
    Table root;
    ScrollPane srlPane;

    public LevelsScreen(Sokoban aGame){//TODO rework ui, add leaderboard button for each level
        game = aGame;
        stage = new Stage(new ScreenViewport());

        root = new Table(game.skin);
        root.setFillParent(true);

        tblList = new Table(game.skin);
        tblList.setFillParent(true);

        srlPane = new ScrollPane(tblList);

        root.add(srlPane).grow();

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(game.backend_url+"levels.php").build();//backend url
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {

                Json json = new Json();
                String jsonStr = httpResponse.getResultAsString();
                //Gdx.app.log("test", jsonStr);
                JsonReader reader = new JsonReader();
                JsonValue map = reader.parse(jsonStr);

                JsonValue entry=map.child;
                while(entry != null){
                    Level level = json.readValue(Level.class, entry);

                    //Gdx.app.log("test", level.name);

                    TextButton btnLevel = new TextButton(level.name+"\npublisher: "+level.publisher+"\npublish date: "+ level.publish_date, game.skin);
                    btnLevel.getLabel().setAlignment(Align.center);

                    onChange(btnLevel, () -> game.setScreen(new GameScreen(game, level)) );
                    tblList.add(btnLevel).growX().row();

                    entry = entry.next;
                }

            }
            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Error", t.getMessage());
            }
            @Override
            public void cancelled() {}
        });

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
