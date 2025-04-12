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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

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
    private boolean[][] targets;
    private ArrayList<Box> boxes = new ArrayList<>();

    public GameScreen(Game aGame) {//TODO load levels from the string and remember to set placed attribute on boxes
        game = aGame;

        batch = new SpriteBatch();

        uiViewport = new ScreenViewport(); //viewport for ui elements (buttons)
        gameViewport = new FitViewport(Sokoban.width, Sokoban.height); //viewport for rendering the game

        stage = new Stage(uiViewport, batch);

        //textures
        wallTexture = new Texture("img/wall.png"); //TODO use better image for walls
        boxTexture = new Texture("img/box.png");
        targetTexture = new Texture("img/target.png");//TODO use better image for targets

        //objects
        player = new Player(new Texture("img/playerFront.jpg"));

        player.setSize(1,1);
        player.setPosition(4,5);

        //walls
        walls = new boolean[][]{{true, true, true, true, true, true, true, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, false, false, false, false, false, false, true},
                                {true, true, true, true, true, true, true, true}};
        //targets
        targets = new boolean[][]{{false, false, false, false, false, false, false, false},
                                  {false, false, false, false, false, false, false, false},
                                  {false, false, false, false, false, false, true, false},
                                  {false, false, false, false, false, false, false, false},
                                  {false, false, false, false, false, false, true, false},
                                  {false, false, false, false, false, false, false, false},
                                  {false, false, false, false, false, false, true, false},
                                  {false, false, false, false, false, false, false, false}};
        //boxes

        Box box = new Box(false);
        box.setPosition(5,5);
        boxes.add(box);
        box = new Box(false);
        box.setPosition(5,3);
        boxes.add(box);
        box = new Box(false);
        box.setPosition(5,1);
        boxes.add(box);

        // btn
        /*
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
        });*/

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
        if(player.isDirty()) {// if the player needs to be drawn
            tryMoving(player);
        }
        if(checkWin()){
            game.setScreen(new TitleScreen(game));//TODO change with more appropriate screen
        }

    }

    public void tryMoving(Player player){

        int posX = (int)player.getX();
        int posY = (int)player.getY();

        int moveX = player.getDirectionX();
        int moveY = player.getDirectionY();

        boolean playerWallCollision = wallCollision(posX, posY, moveX, moveY);
        boolean canMoveForward = canMoveForward(posX, posY, moveX, moveY);
        boolean isBounded = (0<=posX && posX<Sokoban.width) && (0<=posY && posY<Sokoban.height);

        if(!playerWallCollision && canMoveForward && isBounded){

            pushBox(player, moveX, moveY);
            player.moveX(moveX);
            player.moveY(moveY);
        }
    }
    public boolean canMoveForward(int posX, int posY, int moveX, int moveY){
        int boxX;
        int boxY;

        for(Box box: boxes){
            boxX = (int) box.getX();
            boxY = (int) box.getY();
            if(posX+moveX==boxX && posY+moveY==boxY){//if moves towards box
                if(wallCollision(boxX, boxY, moveX, moveY)){
                    return false;
                }
                if(boxAt(boxX+moveX, boxY+moveY) != null){
                    return false;
                }
            }
        }
        return true;
    }

    public Box boxAt(int posX, int posY){

        int boxX;
        int boxY;

        for(Box box: boxes){
            boxX = (int) box.getX();
            boxY = (int) box.getY();
            if(posX==boxX && posY==boxY){
                return box;
            }
        }
        return null;
    }
    public boolean isOnTarget(int posX, int posY){
        return targets[Sokoban.height-1-posY][posX];
    }

    public void pushBox( Player player, int moveX, int moveY){
        int posX = (int) player.getX();
        int posY = (int) player.getY();

        Box box = boxAt(posX+moveX, posY+moveY);

        if(box != null){
            box.translate(moveX, moveY);
            box.setPlaced(isOnTarget((int)box.getX(), (int)box.getY()));
        }

    }

    public boolean wallCollision(int posX, int posY, int moveX, int moveY){
        //horizontal
        if(moveX != 0){
            int wallX = MathUtils.clamp(posX+moveX,0, Sokoban.height-1);
            return walls[Sokoban.height-1-posY][wallX]; //inverting y as coordinates work the way around (y goes up)
        }
        //vertical
        if(moveY != 0){
            int wallY = Sokoban.height-1 -MathUtils.clamp(posY+moveY,0, Sokoban.width-1); //inverting y as coordinates work the way around (y goes up)
            return walls[wallY][posX];
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
        for(int i=0; i<Sokoban.height; i++){
            for(int j=0; j<Sokoban.width; j++){
                if(targets[i][j])
                    batch.draw(targetTexture, j, Sokoban.height-1-i, 1, 1);
            }
        }
    }

    boolean checkWin(){
        for(Box box : boxes ){
            if(!box.isPlaced()){
                return false;
            }
        }
        return true;
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
