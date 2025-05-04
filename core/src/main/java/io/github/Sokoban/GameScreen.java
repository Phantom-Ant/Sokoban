package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen {
    private Sokoban game;
    private Level level;

    int width, height;
    int moves; //TODO remove and use moves size instead
    int pushes;


    private Stage stage;
    private FitViewport gameViewport;
    private ScreenViewport uiViewport;
    private InputMultiplexer inputMultiplexer;

    private SpriteBatch batch;
    private Table winRoot, root;
    private Window winScore;
    private TextButton btnUndo;
    private Label lblMoves, lblPushes, winMoves, winPushes;;
    private float timer;
    private Player player;
    private boolean[][] walls, targets, floor;
    private ArrayList<Box> boxes = new ArrayList<>();

    private List<Move> listMoves = new ArrayList<>();

    //TEST XSB (Level 1 from Thinking Rabbit)
    private static String testXSB = "____#####__________\n" +
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
    public GameScreen(Sokoban aGame){
        this(aGame, new Level(0, "", testXSB, "", ""));
    }
    public GameScreen(Sokoban aGame, Level level) {
        game = aGame;
        this.level = level;

        batch = new SpriteBatch();
        uiViewport = new ScreenViewport(); //viewport for ui elements (buttons)
        stage = new Stage(uiViewport, batch);

        ///textures
        player = new Player(0, -1);
        player.setSize(1,1);

        ///load level
        loadLevelFromXSB(level.data);

        gameViewport = new FitViewport(width, height);

        ///ui objects //
        // TODO implement timer, pushes labels

        // btn
        //TODO replace with image button
        //

        //undo btn
        btnUndo = new TextButton("UNDO", Sokoban.skin);
        onChange(btnUndo, () ->{
            btnUndo.setText("UNDO");
            undo();
        });

        lblMoves = new Label("Moves: 0", Sokoban.skin);
        //lblMoves.setColor(Color.WHITE);//TODO fix color

        lblPushes = new Label("Pushes: 0", game.skin);

        //TODO problem with background title bar lies on assets and ninepatch
        ///Score Window

        winMoves = new Label("Moves: 0", game.skin);
        winPushes = new Label("Pushes: 0", game.skin);

        winScore = new Window("",game.skin);
        winScore.setMovable(false);

        winScore.add(new Label("You won!", game.skin)).align(Align.center).height(60f).expandX().top().colspan(2).row();
        winScore.add(winMoves).expand();
        winScore.add(winPushes).expand();

        winScore.setVisible(false);

        ///

        root = new Table();
        root.setFillParent(true);
        //
        root.defaults().expand().bottom().right();
        //
        root.add(lblMoves);// moves lbl
        root.add(lblPushes);
        root.add(btnUndo).width(200f).height(150f);// undo btn


        winRoot = new Table();
        winRoot.setFillParent(true);
        //
        winRoot.add(winScore).width(750f).height(400f); //TODO change with dynamic size


        stage.addActor(root);
        stage.addActor(winRoot);

        //stage.setDebugAll(true);
        /// input processors
        inputMultiplexer = new InputMultiplexer();
        //
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new GestureDetector(player));
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
            if(tryMoving(player)){

                if(checkWin()){
                    //game.setScreen(new TitleScreen(game));//TODO change with more appropriate screen
                    //TODO show score dialog
                    winScore.setVisible(true);
                    winMoves.setText(lblMoves.getText());
                    winPushes.setText(lblPushes.getText());
                }

            }
        }

    }
    private void draw(){
        ScreenUtils.clear(Color.WHITE);//TEST

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

    ///


    public void undo(){
        Move move=null;
        if(!listMoves.isEmpty()){
            move = listMoves.remove(listMoves.size()-1);
        }
        if(move != null){
            player.translate(-move.x, -move.y);
            player.face(move.x, move.y);

            moves--;
            lblMoves.setText("Moves: "+ moves);

            if(move.box != null){
                move.box.translate(-move.x, -move.y);
                move.box.setPlaced(isOnTarget((int)move.box.getX(), (int)move.box.getY()));
                pushes--;
                lblPushes.setText("Pushes: "+pushes);
            }

        }
    }



    ///

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
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(walls[i][j])
                    batch.draw(Sokoban.tex_wall, j, height-1-i, 1, 1);
            }
        }
    }

    private void drawFloor(SpriteBatch batch){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(floor[i][j])
                    batch.draw(Sokoban.tex_floor, j, height-1-i, 1, 1);
            }
        }
    }
    private void drawTargets(SpriteBatch batch){
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                if(targets[i][j])
                    batch.draw(Sokoban.tex_target, j, height-1-i, 1, 1);
            }
        }
    }
    private void drawBoxes(SpriteBatch batch){
        for(Box box : boxes){
            batch.draw(box.getTexture(), box.getX(), box.getY(), 1, 1);
        }
    }




    ///
    boolean checkWin(){
        for(Box box : boxes ){
            if(!box.isPlaced()){
                return false;
            }
        }
        return true;
    }

    public void loadLevelFromXSB(String xsb){
        //xsb = xsb.replace("\r\n", "\n");//\r\n problem fix (currently fixed in backend)
        String[] xsbs = xsb.split("\n");

        height = xsbs.length;
        width = xsbs[0].length();

        walls = new boolean[height][width];
        floor = new boolean[height][width];
        targets = new boolean[height][width];
        Box box;
        char c;

        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                c = xsbs[i].charAt(j);

                walls[i][j] = c=='#';
                targets[i][j] = c=='.'||c=='+'||c=='*';
                floor[i][j] = c!='_' && c!='#';

                if(c=='@'||c=='+'){
                    player.setPosition(j,height-1-i);
                }
                if(c=='$'||c=='*'){
                    box = new Box(c=='*');
                    box.setPosition(j,height-1-i);
                    boxes.add(box);
                }

            }
        }
    }
    ///


    public boolean tryMoving(Player player){ //returns whether it moved

        int posX = (int)player.getX();
        int posY = (int)player.getY();

        int moveX = player.getDirectionX();
        int moveY = player.getDirectionY();

        Box box;

        boolean playerWallCollision = wallCollision(posX, posY, moveX, moveY);
        boolean canMoveForward = canMoveForward(posX, posY, moveX, moveY);
        boolean isBounded = (0<=posX && posX<width) && (0<=posY && posY<height);

        if(!playerWallCollision && canMoveForward && isBounded){
            box = pushBox(player, moveX, moveY);
            player.moveX(moveX);
            player.moveY(moveY);

            listMoves.add(new Move(moveX, moveY, box));

            if(box != null){
                pushes++;
                lblPushes.setText("Pushes: "+pushes);
            }

            moves++;
            lblMoves.setText("Moves: "+ moves);

            return true;
        }
        return false;
    }



    ///
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
        return targets[height-1-posY][posX];
    }
    public boolean wallCollision(int posX, int posY, int moveX, int moveY){
        //horizontal
        if(moveX != 0){
            int wallX = MathUtils.clamp(posX+moveX,0, width-1);
            return walls[height-1-posY][wallX]; //inverting y as coordinates work the way around (y goes up)
        }
        //vertical
        if(moveY != 0){
            int wallY = height-1 -MathUtils.clamp(posY+moveY,0, height-1); //inverting y as coordinates work the way around (y goes up)
            return walls[wallY][posX];
        }
        return false;
    }



    ///score methods

    //TODO implement score uploading to leaderboard

}
