package io.github.Sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen {
    private Game game;
    private Stage stage;
    private FitViewport gameViewport;
    private ScreenViewport uiViewport;
    private InputMultiplexer inputMultiplexer;

    private SpriteBatch batch;
    private Texture background;
    private Table table;
    private TextButton btn;
    private float timer;

    //private Player player;
    private Sprite player;

    public GameScreen(Game aGame) {
        game = aGame;

        batch = new SpriteBatch();
        background = new Texture("img/scatola.jpg");

        //player = new Player(new Texture("img/ominoDavanti.jpg"), 4, 4);
        player = new Sprite(new Texture("img/ominoDavanti.jpg"));
        player.setSize(1,1);

        player.setPosition(4,4);

        uiViewport = new ScreenViewport(); //viewport for ui elements (buttons)
        gameViewport = new FitViewport(Sokoban.width, Sokoban.height); //viewport for rendering the game

        stage = new Stage(uiViewport, batch);


        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = Sokoban.font;
        //
        btn = new TextButton("Click?", style);
        //
        btn.getLabel().setAlignment(Align.center);


        table = new Table();
        table.setFillParent(true);


        btn.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btn.setText("Click");
            }
        });

        //table.add(btn).center(); DEBUG BUTTON

        stage.addActor(table);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);

        //temporarily arrangement for movement listener (i recommend to collapse this mess)
        inputMultiplexer.addProcessor(new GestureDetector(new GestureDetector.GestureListener() {
            @Override
            public boolean touchDown(float x, float y, int pointer, int button) {return false;}
            @Override
            public boolean tap(float x, float y, int count, int button) {return false;}
            @Override
            public boolean longPress(float x, float y) {return false;}
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
                    btn.setText(direction);

                    if(direction.equals("Right")){ //temp
                        //player.moveBy(1,0);
                        player.translateX(1);
                        player.setFlip(false, false);
                        player.setTexture(new Texture("img/ominoLato.jpg"));
                    }else if(direction.equals("Left")){
                        //player.moveBy(-1,0);
                        player.translateX(-1);
                        player.setFlip(true, false);
                        player.setTexture(new Texture("img/ominoLato.jpg"));
                    }else if(direction.equals("Down")){
                        //player.moveBy(0,-1);
                        player.translateY(-1);
                        player.setFlip(false, false);
                        player.setTexture(new Texture("img/ominoDavanti.jpg"));
                    }else { //Up
                        //player.moveBy(0,1);
                        player.translateY(1);
                        player.setFlip(false, false);
                        player.setTexture(new Texture("img/ominoDietro.jpg"));
                    }
                }
                return true;
            }

            @Override
            public boolean pan(float x, float y, float deltaX, float deltaY) {return false;}
            @Override
            public boolean panStop(float x, float y, int pointer, int button) {return false;}
            @Override
            public boolean zoom(float initialDistance, float distance) {return false;}
            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {return false;}
            @Override
            public void pinchStop() {}
        }));


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {

    }

    @Override
    public void render(float delta) {
        input();
        logic();
        draw();
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        uiViewport.update(width, height, true);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

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

        //constrain player within bounds
        player.setX(MathUtils.clamp(player.getX(), 0, Sokoban.width-player.getWidth()));
        player.setY(MathUtils.clamp(player.getY(), 0, Sokoban.height-player.getHeight()));
    }
    private void draw(){
        ScreenUtils.clear(Color.BLACK);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        //
        batch.begin();
        batch.draw(background, 0, 0, 1, 1);
        batch.draw(background, 0, Sokoban.height-1, 1, 1);
        batch.draw(background, Sokoban.width-1, 0, 1, 1);

        player.draw(batch);

        batch.end();
        //

        uiViewport.apply();

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        player.getTexture().dispose();
        stage.dispose();
    }
}
