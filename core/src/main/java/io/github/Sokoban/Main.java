package io.github.Sokoban;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.File;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    //private SpriteBatch batch;
    private Texture image;
    private Stage stage;
    private Table table;
    private TextButton btn;
    private Label testLabel;
    private float timer;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());

        BitmapFont font = new BitmapFont(); // default font
        font.getData().setScale(10.0f);
        //
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        //
        btn = new TextButton("Click?", style);
        //
        testLabel = btn.getLabel();
        testLabel.setAlignment(Align.center);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);

        image = new Texture("libgdx.png");
        Image img = new Image(image);

        img.addAction(Actions.moveBy(1,1));

        inputMultiplexer.addProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean tap(float x, float y, int count, int button) {
                return false;
            }

            @Override
            public boolean longPress(float x, float y) {
                return false;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                Vector2 vec2 = new Vector2(velocityX, velocityY);
                if(vec2.len() > 500){
                    String direction = "";
                    if(Math.abs(velocityX) > Math.abs(velocityY)){
                        direction = velocityX>0? "Right" : "Left";
                    }else{
                        direction = velocityY>0? "Down" : "Up";
                    }
                    //Gdx.app.log("Fling", direction);
                    testLabel.setText(direction);

                    if(direction.equals("Right")){
                        img.addAction(Actions.moveBy(100, 0));
                    }else if(direction.equals("Left")){
                        img.addAction(Actions.moveBy(-100,0));
                    }else if(direction.equals("Down")){
                        img.addAction(Actions.moveBy(0, -100));
                    }else { //Up
                        img.addAction(Actions.moveBy(0, 100));
                    }

                }

                return true;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {
                return false;
            }

            @Override
            public boolean panStop(float x, float y, int pointer, int button) {
                return false;
            }

            @Override
            public boolean zoom(float initialDistance, float distance) {
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
                return false;
            }

            @Override
            public void pinchStop() {

            }
        }));

        Gdx.input.setInputProcessor(inputMultiplexer);

        table = new Table();
        table.setFillParent(true);

        btn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                testLabel.setText("Click");
            }
        });

        //table.add(btn).center();

        table.add(img).center();
        stage.addActor(table);
    }

    @Override
    public void render() {
        input();
        logic();
        draw();
    }

    private void input(){
    }
    private void logic(){
        //timer test
        /*
        float delta = Gdx.graphics.getDeltaTime();
        timer += delta;
        timer *= 100;
        timer = (int) timer;
        timer /= 100;
        testLabel.setText(""+timer);
        */
    }
    private void draw(){
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        //batch.begin();
        //batch.draw(image, 140, 210);
        stage.act();
        stage.draw();

        //batch.end();
    }

    @Override
    public void dispose() {
        //batch.dispose();
        image.dispose();
        stage.dispose();
    }
}
