package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LeaderBoardScreen implements Screen {
    Sokoban game;
    Stage stage;


    Table tblList;
    Table root;
    //ScrollPane srlPane; //TODO fix scrollpane

    Label lblHeadUser, lblHeadMoves, lblHeadPushes, lblHeadTime; //TODO replace with button for Moves, Pushes, Time to order accordingly
    Label lblUser, lblMoves, lblPushes, lblTime;
    ImageButton btnBack;
    TextButton btnLevel;
    public LeaderBoardScreen(Sokoban aGame, Level level){//TODO rework ui order scores list
        game = aGame;
        stage = new Stage(new ScreenViewport());

        root = new Table(game.skin);
        root.setFillParent(true);

        lblHeadUser = new Label("User", game.skin, "table");
        lblHeadMoves = new Label("Moves", game.skin, "table");
        lblHeadPushes = new Label("Pushes", game.skin, "table");
        lblHeadTime = new Label("Time", game.skin, "table");

        lblHeadUser.setAlignment(Align.center);
        lblHeadMoves.setAlignment(Align.center);
        lblHeadPushes.setAlignment(Align.center);
        lblHeadTime.setAlignment(Align.center);

        btnBack = new ImageButton(game.skin, "back-table");
        onChange(btnBack, ()-> game.setScreen(game.previousScreen));

        btnLevel = new TextButton("Play: "+level.name, game.skin, "table");
        onChange(btnLevel, ()-> game.setScreen(new GameScreen(game, level)));


        tblList = new Table(game.skin);
        tblList.setFillParent(true);
        //
        tblList.defaults().height(120f);
        tblList.defaults().growX();

        tblList.add(btnBack);
        tblList.add(btnLevel).colspan(3).row();

        tblList.add(lblHeadUser).spaceBottom(10f);
        tblList.add(lblHeadMoves).spaceBottom(10f);
        tblList.add(lblHeadPushes).spaceBottom(10f);
        tblList.add(lblHeadTime).spaceBottom(10f).row();

        //tblList.add(new TextButton("test", game.skin));

        //tblList.debug();

        root.add(tblList).growX().top();

        //root.debug();

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.POST).header("Content-Type", "application/json").url(game.backend_url+"leaderboard.php").build();//backend url

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        String jsonStr = json.toJson(level);

        httpRequest.setContent(jsonStr);

        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {



                Json json = new Json();
                String jsonStr = httpResponse.getResultAsString();
                Gdx.app.log("test", jsonStr);
                JsonReader reader = new JsonReader();
                JsonValue map = reader.parse(jsonStr);

                JsonValue entry=map.child;


                while(entry != null){
                    Score score = json.readValue(Score.class, entry);

                    //Gdx.app.log("test", score.user);

                    lblUser = new Label(score.user, game.skin, "table");
                    lblMoves = new Label(""+score.moves, game.skin, "table");
                    lblPushes = new Label(""+score.pushes, game.skin, "table");
                    lblTime = new Label(""+score.time_spent, game.skin, "table");

                    lblUser.setAlignment(Align.center);
                    lblMoves.setAlignment(Align.center);
                    lblPushes.setAlignment(Align.center);
                    lblTime.setAlignment(Align.center);

                    tblList.add(lblUser);
                    tblList.add(lblMoves);
                    tblList.add(lblPushes);
                    tblList.add(lblTime).row();

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
        ScreenUtils.clear(game.backgroundColor); //TEST
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
