package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TestBackend implements Screen {
    Sokoban game;
    Stage stage;

    Table table;
    TextButton btnGet;
    TextButton btnPost;

    public TestBackend(Sokoban aGame){
        game = aGame;
        stage = new Stage(new ScreenViewport());

        table = new Table();
        table.setFillParent(true);

        TextButton.TextButtonStyle txtbtnStyle = new TextButton.TextButtonStyle();
        txtbtnStyle.font = Sokoban.skin.getFont("font-export.fnt");

        btnGet = new TextButton("Get", txtbtnStyle);

        btnGet.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
                Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.GET).url("http://10.0.2.2/PHP/BackendTest/").build();
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse) {
                        Array<Utente> users = new Array<>();

                        Json json = new Json();
                        String jsonStr = httpResponse.getResultAsString();

                        JsonReader reader = new JsonReader();
                        JsonValue map = reader.parse(jsonStr);

                        JsonValue entry=map.child;
                        do{
                            Utente user = json.readValue(Utente.class, entry);
                            users.add(user);
                            entry = entry.next;
                        }while(entry != null);

                        /*for(JsonValue entry : map){ //less efficient
                            Utente user = json.readValue(Utente.class, entry);
                            users.add(user);
                        }*/

                        btnGet.setText(users.get(0).toString());

                    }

                    @Override
                    public void failed(Throwable t) {
                        btnGet.setText("Failed ");
                        Gdx.app.log("Error", t.getMessage());
                    }

                    @Override
                    public void cancelled() {}
                });
            }
        });

        btnPost = new TextButton("Post", txtbtnStyle);

        btnPost.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
                Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.POST).header("Content-Type", "application/json").url("http://10.0.2.2/PHP/BackendTest/").build();

                Utente user = new Utente("porto", "fino");

                Json json = new Json();
                json.setOutputType(JsonWriter.OutputType.json);

                String jsonStr = json.toJson(user);

                httpRequest.setContent(jsonStr);
                Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
                    @Override
                    public void handleHttpResponse(Net.HttpResponse httpResponse){

                        btnPost.setText(""+httpResponse.getStatus().getStatusCode());
                    }

                    @Override
                    public void failed(Throwable t) {
                        btnPost.setText("Failed ");
                        Gdx.app.log("Error", t.getMessage());
                    }

                    @Override
                    public void cancelled() {

                    }
                });
            }
        });

        table.add(btnPost);
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
