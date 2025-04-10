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

    private Texture wallTexture, boxTexture, targetTexture;
    private Player player;
    private boolean[][] walls;
    private ArrayList<Box> boxes = new ArrayList<>();
    private ArrayList<Sprite> targets = new ArrayList<>();

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
        boxTexture = new Texture("img/box.png"); //test sprite
        targetTexture = new Texture("img/target.png");//TODO use better image for targets

        walls = new boolean[][]{{true, true, true, true, true, true, true, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, true, true, true, true, true, true, true}};


        //test boxes
        Box box = new Box(false);
        box.setPosition(2,2);
        boxes.add(box);
        box = new Box(false);
        box.setPosition(1,1);
        boxes.add(box);
        box = new Box(false);
        box.setPosition(2,1);
        boxes.add(box);

        //targets
        Sprite target = new Sprite(targetTexture);
        target.setPosition(6,2);
        targets.add(target);
        target = new Sprite(targetTexture);
        target.setPosition(4,2);
        targets.add(target);
        target = new Sprite(targetTexture);
        target.setPosition(6,4);
        targets.add(target);

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
        //TODO can i move this and related logic to an update(walls, boxes) method inside player? would it be better?
        player.canMoveUp = canPlayerMoveDirection(0,1);
        player.canMoveDown = canPlayerMoveDirection(0,-1);
        player.canMoveRight = canPlayerMoveDirection(1,0);
        player.canMoveLeft = canPlayerMoveDirection(-1,0);

        //TODO turn these in functions
        /*
        //constrain player within bounds
        player.canMoveUp = player.getY()<Sokoban.height-1;
        player.canMoveDown = player.getY()>0;
        player.canMoveRight = player.getX()<Sokoban.width-1;
        player.canMoveLeft = player.getX()>0;
        */

    }

    boolean canPlayerMoveDirection(int x, int y){

        int posX = (int)player.getX();
        int posY = (int)player.getY();

        boolean playerWallCollision = wallCollision(posX, posY, x, y);
        boolean boxPushable = pushableBox(posX, posY, x, y);

        return !playerWallCollision && boxPushable;
    }

    boolean pushableBox(int posX, int posY, int moveX, int moveY){//returns true if the box can be moved
        int boxX;
        int boxY;
        for(Box box: boxes){
            boxX = (int) box.getX();
            boxY = (int) box.getY();
            if(posX+moveX==boxX && posY+moveY==boxY){//if moves towards box
                if(wallCollision(boxX, boxY, moveX, moveY)){
                    return false;
                }else{
                    return pushableBox(boxX, boxY, moveX, moveY);
                }
            }
        }
        return true;
    }

    //TODO check if it is on target and set placed attribute on the box
    void pushBox(int posX, int posY, int moveX, int moveY){
        int boxX;
        int boxY;
        for(Box box: boxes){
            boxX = (int) box.getX();
            boxY = (int) box.getY();

            if(posX==boxX && posY==boxY){
                /*if(){ //target check

                }*/
                Gdx.app.log("BoxDirection", new Vector2(moveX, moveY).toString());
            }

        }
    }

    boolean wallCollision(int posX, int posY, int moveX, int moveY){
        if(moveX != 0) //horizontal
            return walls[posY][MathUtils.clamp(posX+moveX,0, Sokoban.height-1)];
        if(moveY != 0){//vertical
            return walls[MathUtils.clamp(posY+moveY,0, Sokoban.width-1)][posX];
        }
        return false;
    }


    private void draw(){
        ScreenUtils.clear(Color.BLACK);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        // textures and sprites drawing
        batch.begin();

        drawWalls(batch);
        drawTargets(batch);
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

    private void drawTargets(SpriteBatch batch){
        for(Sprite target: targets){
            batch.draw(targetTexture, target.getX(), target.getY(), 1, 1);
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
