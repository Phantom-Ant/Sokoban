package io.github.Sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LevelsScreen implements Screen {
    Game game;
    Stage stage;


    Table tblList;
    Table tblOut;
    ScrollPane srlPane;

    public LevelsScreen(Game aGame){//TODO clean this up
        game = aGame;
        stage = new Stage(new ScreenViewport());

        tblList = new Table(Sokoban.gameSkin);
        tblList.setFillParent(true);

        srlPane = new ScrollPane(tblList);

        tblOut = new Table(Sokoban.gameSkin);
        tblOut.setFillParent(true);
        tblOut.add(srlPane).expand().fill();

        TextButton.TextButtonStyle txtbtnStyle = new TextButton.TextButtonStyle();
        txtbtnStyle.font = Sokoban.font;

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("http://10.0.2.2/PHP/Sokoban/").build();//backend url
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
                    Level level = json.readValue(Level.class, entry);

                    Gdx.app.log("test", level.name);

                    TextButton btnLevel = new TextButton(level.name+"\npublisher: "+level.publisher+"\npublish date: "+ level.publish_date, txtbtnStyle);
                    btnLevel.getLabel().setAlignment(Align.left);
                    btnLevel.addListener(new InputListener(){
                        @Override
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }

                        @Override
                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            game.setScreen(new GameScreen(game, level));
                        }
                    });
                    tblList.add(btnLevel).row();

                    entry = entry.next;
                }

            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("Error", t.getMessage());
            }

            @Override
            public void cancelled() {

            }
        });

        stage.addActor(tblOut);
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
