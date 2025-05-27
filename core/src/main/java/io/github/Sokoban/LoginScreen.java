package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class LoginScreen implements Screen {
    ScreenViewport viewport;

    Sokoban game;
    Stage stage;

    Table root, tblBack;

    TextField tfdName, tfdPassword;
    TextButton btnLogin, btnRegister;
    ImageButton btnBack;

    public LoginScreen(Sokoban aGame){
        viewport = new ScreenViewport();

        game = aGame;
        stage = new Stage(viewport);
        //
        tfdName = new TextField("", game.skin);
        tfdName.setAlignment(Align.center);
        tfdName.setMessageText("Name");

        tfdPassword = new TextField("", game.skin);
        tfdPassword.setAlignment(Align.center);
        tfdPassword.setMessageText("Password");

        btnLogin = new TextButton("Login", game.skin);
        onChange(btnLogin, this::login);

        btnRegister = new TextButton("Register", game.skin);
        onChange(btnRegister, ()-> game.setScreen(new RegisterScreen(game)));
        //
        root = new Table();
        root.setFillParent(true);

        root.defaults().width(Value.percentWidth(0.3f, root));
        root.defaults().height(120f);
        root.defaults().space(20f);;
        root.defaults().expandX();

        root.add(tfdName).row();
        root.add(tfdPassword).row();

        root.add(btnLogin).row();
        root.add(btnRegister).spaceTop(40f);

        //stage.setDebugAll(true); //click 1:1 to see lines better

        btnBack = new ImageButton(game.skin, "back");
        onChange(btnBack, ()->game.setScreen(game.title_screen));

        tblBack = new Table();
        tblBack.setFillParent(true);

        tblBack.add(btnBack).bottom().left().expand();

        stage.addActor(root);
        stage.addActor(tblBack);
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
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
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

    public void login(){
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.POST).header("Content-Type", "application/json").url(game.backend_url+"login.php").build();

        //TODO make regex validation
        User user = new User(tfdName.getText(), "", tfdPassword.getText());

        //TEST user
        //User user = new User("thinking_rabbit", "thinkingrabbit@gmail.com", "pass");

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        String jsonStr = json.toJson(user);

        httpRequest.setContent(jsonStr);
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse){

                String result = httpResponse.getResultAsString().trim(); //fix final \n

                //Gdx.app.log("Error", result);

                if(result.equals("Login successful")){
                    game.user = user;
                    Preferences account = Gdx.app.getPreferences("Account");
                    account.putString("username", user.username);
                    account.putString("email", user.email);
                    account.putString("password", user.password);
                    account.flush();
                }
                game.setScreen(game.title_screen);

                //TODO print error message on screen when login fails
            }

            @Override
            public void failed(Throwable t){
                Gdx.app.log("test", t.getMessage());
            }

            @Override
            public void cancelled() {

            }
        });
    }
}
