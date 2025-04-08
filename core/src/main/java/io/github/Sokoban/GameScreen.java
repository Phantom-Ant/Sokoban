package io.github.Sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen {
    private Game game;
    private Stage stage;
    private FitViewport gameViewport;
    private ScreenViewport uiViewport;
    private InputMultiplexer inputMultiplexer;

    private SpriteBatch batch;
    private Table table;
    private TextButton btn;
    private float timer;

    private Player player;
    private Texture wallTexture;
    private boolean[][] walls;
    private Texture boxTexture;
    private ArrayList<Box> boxes = new ArrayList<>();

    public GameScreen(Game aGame) {
        game = aGame;

        batch = new SpriteBatch();

        uiViewport = new ScreenViewport(); //viewport for ui elements (buttons)
        gameViewport = new FitViewport(Sokoban.width, Sokoban.height); //viewport for rendering the game

        stage = new Stage(uiViewport, batch);

        //objects
        player = new Player(new Texture("img/playerFront.jpg"));

        player.setSize(1,1);
        player.setPosition(4,4);

        wallTexture = new Texture("img/wall.png"); //TODO use better image for walls
        walls = new boolean[][]{{true, true, true, true, true, true, true, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, true, true, true, true, true, true, true}};

        boxTexture = new Texture("img/box.png"); //test sprite

        //test boxes
        Box box = new Box(false);
        box.setPosition(2,2);
        boxes.add(box);

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
                //game.setScreen(new TitleScreen(game));
            }
        });

        table = new Table();
        table.setFillParent(true);

        //table.add(btn).center();// DEBUG BUTTON

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

        //TODO turn these in functions
        player.canMoveUp = !walls[MathUtils.clamp((int)player.getY()+1,0, Sokoban.width-1)][(int) player.getX()];
        player.canMoveDown = !walls[MathUtils.clamp((int)player.getY()-1,0, Sokoban.width-1)][(int) player.getX()];
        player.canMoveRight = !walls[(int) player.getY()][MathUtils.clamp((int)player.getX()+1,0, Sokoban.height-1)];
        player.canMoveLeft = !walls[(int) player.getY()][MathUtils.clamp((int)player.getX()-1,0, Sokoban.height-1)];

        /*
        //constrain player within bounds
        player.canMoveUp = player.getY()<Sokoban.height-1;
        player.canMoveDown = player.getY()>0;
        player.canMoveRight = player.getX()<Sokoban.width-1;
        player.canMoveLeft = player.getX()>0;
        */

    }
    private void draw(){
        ScreenUtils.clear(Color.BLACK);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        // textures and sprites drawing
        batch.begin();

        drawWalls(batch);
        drawBoxes(batch);

        player.draw(batch);

        batch.end();
        //

        uiViewport.apply();

        stage.act();
        stage.draw();
    }

    private void drawWalls(SpriteBatch batch){
        for(int i=0; i<Sokoban.height; i++){
            for(int j=0; j<Sokoban.width; j++){
                if(walls[i][j])
                    batch.draw(wallTexture, j, Sokoban.height-1-i, 1, 1);
            }
        }
    }

    private void drawBoxes(SpriteBatch batch){
        for(Box box : boxes){
            batch.draw(box.getTexture(), box.getX(), box.getY(), 1, 1);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        wallTexture.dispose();
        player.getTexture().dispose();
        boxTexture.dispose();
        stage.dispose();
    }
}
