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

    private Player player;

    public GameScreen(Game aGame) {
        game = aGame;

        batch = new SpriteBatch();

        uiViewport = new ScreenViewport(); //viewport for ui elements (buttons)
        gameViewport = new FitViewport(Sokoban.width, Sokoban.height); //viewport for rendering the game

        stage = new Stage(uiViewport, batch);

        //objects

        background = new Texture("img/scatola.jpg"); //test sprite

        player = new Player(new Texture("img/ominoDavanti.jpg"));

        player.setSize(1,1);
        player.setPosition(4,4);


        // btn
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = Sokoban.font;
        //
        btn = new TextButton("Click?", style);
        //
        btn.getLabel().setAlignment(Align.center);
        //
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

        table = new Table();
        table.setFillParent(true);

        //table.add(btn).center(); DEBUG BUTTON

        stage.addActor(table);

        // input processors
        inputMultiplexer = new InputMultiplexer();
        //
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new GestureDetector(player));
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
