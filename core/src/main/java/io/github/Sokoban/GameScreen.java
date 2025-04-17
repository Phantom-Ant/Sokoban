package io.github.Sokoban;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen {
    private Game game;
    private Stage stage;
    private FitViewport gameViewport;
    private ScreenViewport uiViewport;
    private InputMultiplexer inputMultiplexer;

    private SpriteBatch batch;
    private Table table;
    private TextButton btnUndo;
    private Label lblMoves;
    private float timer;
    private Player player;
    private boolean[][] walls;
    private boolean[][] targets;
    private boolean[][] floor;
    private ArrayList<Box> boxes = new ArrayList<>();

    private List<Move> moves = new ArrayList<>();

    //TEST XSB (Level 1 from Thinking Rabbit)
    private String testXSB = "____#####__________\n" +
                             "____#---#__________\n" +
                             "____#$--#__________\n" +
                             "__###--$##_________\n" +
                             "__#--$-$-#_________\n" +
                             "###-#-##-#___######\n" +
                             "#---#-##-#####--..#\n" +
                             "#-$--$----------..#\n" +
                             "#####-###-#@##--..#\n" +
                             "____#-----#########\n" +
                             "____#######________\n";

    public GameScreen(Game aGame) {
        //TODO consider landscape support
        game = aGame;
        batch = new SpriteBatch();
        uiViewport = new ScreenViewport(); //viewport for ui elements (buttons)
        stage = new Stage(uiViewport, batch);

        ///textures
        player = new Player(0, -1);
        player.setSize(1,1);

        ///load level
        //loadTestLevel();
        loadLevelFromXSB(testXSB);

        gameViewport = new FitViewport(Sokoban.width, Sokoban.height);

        ///ui objects //
        // TODO implement timer, moves and pushes labels

        // btn
        //TODO replace with image button
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = Sokoban.font;
        //
        btnUndo = new TextButton("UNDO", btnStyle);
        //
        btnUndo.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                btnUndo.setText("UNDO");
                undo();
                //game.setScreen(new TitleScreen(game));
            }
        });

        //moves label
        Label.LabelStyle lblStyle = new Label.LabelStyle();
        lblStyle.font = Sokoban.font;

        lblMoves = new Label("Moves: 0", lblStyle);


        //undo btn
        table = new Table();
        table.setFillParent(true);
        //
        table.align(Align.bottomRight);
        table.add(btnUndo);// undo btn
        //
        stage.addActor(table);
        //

        //moves label
        table = new Table();
        table.setFillParent(true);
        //
        table.align(Align.bottomLeft);
        table.add(lblMoves);// moves lbl
        //
        stage.addActor(table);
        //


        /// input processors
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
    private void draw(){
        ScreenUtils.clear(Color.BLACK);

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        // textures and sprites drawing
        batch.begin();

        drawFloor(batch);
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

    @Override
    public void dispose() {
        batch.dispose();
        Sokoban.tex_wall.dispose();
        player.getTexture().dispose();
        Sokoban.tex_box.dispose();
        stage.dispose();
    }


    public void undo(){
        Move move=null;
        if(!moves.isEmpty()){
            move = moves.remove(moves.size()-1);
        }
        if(move != null){
            player.translate(-move.x, -move.y);
            player.face(move.x, move.y);

            Sokoban.moves--;
            lblMoves.setText("Moves: "+Sokoban.moves);

            if(move.box != null){
                move.box.translate(-move.x, -move.y);
                move.box.setPlaced(isOnTarget((int)move.box.getX(), (int)move.box.getY()));
            }

        }
    }
    /*public void undoPush(){ //TODO make this
        Move move=null;
        if(!moves.isEmpty()){
            move = moves.remove(moves.size()-1);
        }
        if(move != null){
            player.translate(-move.x, -move.y);
            player.face(move.x, -move.y);
            if(move.box != null){
                move.box.translate(-move.x, -move.y);
                move.box.setPlaced(isOnTarget((int)move.box.getX(), (int)move.box.getY()));
            }
            //TODO decrease moves
        }
    }*/
    //TODO to implement redo do not remove the move, just get it, use moves counter to keep the index
    ///draw methods
    private void drawWalls(SpriteBatch batch){
        for(int i=0; i<Sokoban.height; i++){
            for(int j=0; j<Sokoban.width; j++){
                if(walls[i][j])
                    batch.draw(Sokoban.tex_wall, j, Sokoban.height-1-i, 1, 1);
            }
        }
    }

    private void drawFloor(SpriteBatch batch){
        for(int i=0; i<Sokoban.height; i++){
            for(int j=0; j<Sokoban.width; j++){
                if(floor[i][j])
                    batch.draw(Sokoban.tex_floor, j, Sokoban.height-1-i, 1, 1);
            }
        }
    }
    private void drawTargets(SpriteBatch batch){
        for(int i=0; i<Sokoban.height; i++){
            for(int j=0; j<Sokoban.width; j++){
                if(targets[i][j])
                    batch.draw(Sokoban.tex_target, j, Sokoban.height-1-i, 1, 1);
            }
        }
    }
    private void drawBoxes(SpriteBatch batch){
        for(Box box : boxes){
            batch.draw(box.getTexture(), box.getX(), box.getY(), 1, 1);
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


    /*public void loadTestLevel(){
        Sokoban.width = 8;//TEST
        Sokoban.height = 8;//TEST

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
        //floor
        floor = new boolean[][]{{false, false, false, false, false, false, false, false},
            {false, true, true, true, true, true, true, false},
            {false, true, true, true, true, true, true, false},
            {false, true, true, true, true, true, true, false},
            {false, true, true, true, true, true, true, false},
            {false, true, true, true, true, true, true, false},
            {false, true, true, true, true, true, true, false},
            {false, false, false, false, false, false, false, false}};
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

    }*/
    public void loadLevelFromXSB(String xsb){
        String[] xsbs = xsb.split("\n");

        Sokoban.height = xsbs.length;
        Sokoban.width = xsbs[0].length();

        walls = new boolean[Sokoban.height][Sokoban.width];
        floor = new boolean[Sokoban.height][Sokoban.width];
        targets = new boolean[Sokoban.height][Sokoban.width];
        Box box;
        char c;

        for(int i=0; i<Sokoban.height; i++){
            for(int j=0; j<Sokoban.width; j++){
                c = xsbs[i].charAt(j);

                walls[i][j] = c=='#';
                targets[i][j] = c=='.'||c=='+'||c=='*';
                floor[i][j] = c!='_' && c!='#';

                if(c=='@'||c=='+'){
                    player.setPosition(j,Sokoban.height-1-i);
                }
                if(c=='$'||c=='*'){
                    box = new Box(c=='*');
                    box.setPosition(j,Sokoban.height-1-i);
                    boxes.add(box);
                }

            }
        }
    }

    public void tryMoving(Player player){

        int posX = (int)player.getX();
        int posY = (int)player.getY();

        int moveX = player.getDirectionX();
        int moveY = player.getDirectionY();

        Box box;

        boolean playerWallCollision = wallCollision(posX, posY, moveX, moveY);
        boolean canMoveForward = canMoveForward(posX, posY, moveX, moveY);
        boolean isBounded = (0<=posX && posX<Sokoban.width) && (0<=posY && posY<Sokoban.height);

        if(!playerWallCollision && canMoveForward && isBounded){
            box = pushBox(player, moveX, moveY);
            player.moveX(moveX);
            player.moveY(moveY);

            moves.add(new Move(moveX, moveY, box));

            Sokoban.moves++;
            lblMoves.setText("Moves: "+Sokoban.moves);
        }
    }
    public Box pushBox( Player player, int moveX, int moveY){// returns true if a box was moved
        int posX = (int) player.getX();
        int posY = (int) player.getY();

        Box box = boxAt(posX+moveX, posY+moveY);

        if(box != null){
            box.translate(moveX, moveY);
            box.setPlaced(isOnTarget((int)box.getX(), (int)box.getY()));
            return box;
        }
        return null;
    }

    /// collisions and positions
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
    public boolean wallCollision(int posX, int posY, int moveX, int moveY){
        //horizontal
        if(moveX != 0){
            int wallX = MathUtils.clamp(posX+moveX,0, Sokoban.width-1);
            return walls[Sokoban.height-1-posY][wallX]; //inverting y as coordinates work the way around (y goes up)
        }
        //vertical
        if(moveY != 0){
            int wallY = Sokoban.height-1 -MathUtils.clamp(posY+moveY,0, Sokoban.height-1); //inverting y as coordinates work the way around (y goes up)
            return walls[wallY][posX];
        }
        return false;
    }
}
