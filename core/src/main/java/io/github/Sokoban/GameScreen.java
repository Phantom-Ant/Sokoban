package io.github.Sokoban;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GameScreen implements Screen { //TODO add restart and home button
    private Sokoban game;
    private Level level;

    int width, height;
    int moves; //TODO remove and use moves size instead
    int pushes;
    boolean won;
    private float timer;
    private int millis;


    private Stage stage;
    private FitViewport gameViewport;
    private ScreenViewport uiViewport;
    private InputMultiplexer inputMultiplexer;

    private SpriteBatch batch;
    private Table winRoot, tblWinLbl, root, tblScore, tblButtons;
    private Window winScore;
    private ImageButton btnUndo, btnHome, btnLevels, winBtnHome, winBtnLevels, btnLeaderboard;
    private TextButton btnPublishScore;
    private Label lblMoves, lblPushes, lblTimer, winMoves, winPushes, winTimer;
    private Player player;
    private boolean[][] walls, targets, floor;
    private ArrayList<Box> boxes = new ArrayList<>();

    private List<Move> listMoves = new ArrayList<>();

    //TEST XSB (Level 1 from Thinking Rabbit)
    private static String testXSB2 ="____#####__________\n" +
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
    private static String testXSB = "#####\n" +
                                    "#@$.#\n" +
                                    "#####\n";
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

        ///ui objects///

        //undo btn
        btnUndo = new ImageButton(game.skin, "undo");
        onChange(btnUndo, this::undo);

        btnHome = new ImageButton(game.skin, "home");
        onChange(btnHome, () -> game.setScreen(game.title_screen));

        btnLevels = new ImageButton(game.skin, "levels");
        onChange(btnLevels, () -> game.setScreen(new LevelsScreen(game)));
        //

        winBtnHome = new ImageButton(game.skin, "home");
        onChange(winBtnHome, ()-> game.setScreen(game.title_screen));

        winBtnLevels = new ImageButton(game.skin, "levels");
        onChange(winBtnLevels, ()-> game.setScreen(new LevelsScreen(game)));

        btnLeaderboard = new ImageButton(game.skin, "leaderboard");
        onChange(btnLeaderboard, ()-> game.setScreen(new LeaderBoardScreen(game, level)));

        btnPublishScore = new TextButton("Publish score", game.skin);
        if(game.user != null){
            onChange(btnPublishScore, this::uploadScore);
        }else{
            //TODO improve ui and set disabled
            btnPublishScore.setDisabled(true);
            btnPublishScore.setTouchable(Touchable.disabled);
            btnPublishScore.setText("Login to publish score");
        }

        //

        lblMoves = new Label("Moves: 0", game.skin, "table");
        lblMoves.setAlignment(Align.center);

        lblPushes = new Label("Pushes: 0", game.skin, "table");
        lblPushes.setAlignment(Align.center);

        lblTimer = new Label("Timer: 0", game.skin, "table");
        lblTimer.setAlignment(Align.center);

        //TODO problem with background title bar lies on assets and ninepatch
        ///Score Window

        winMoves = new Label("Moves: 0", game.skin, "text");
        winMoves.setAlignment(Align.center);

        winPushes = new Label("Pushes: 0", game.skin, "text");
        winPushes.setAlignment(Align.center);

        winTimer = new Label("Timer: 0", game.skin, "text");
        winTimer.setAlignment(Align.center);

        tblWinLbl = new Table();
        //tblWinLbl.setFillParent(true);

        tblWinLbl.add(winMoves).grow();
        tblWinLbl.add(winPushes).grow();
        tblWinLbl.add(winTimer).grow();



        winScore = new Window("",game.skin);
        winScore.setMovable(false);

        winScore.add(tblWinLbl).grow().colspan(3).row();

        winScore.add(winBtnHome).height(120f).growX();
        winScore.add(winBtnLevels).height(120f).growX();
        winScore.add(btnLeaderboard).height(120f).growX().row();
        winScore.add(btnPublishScore).height(120f).colspan(3).growX();

        winScore.setVisible(false);

        ///

        tblScore = new Table();
        tblScore.defaults().height(150f).uniform().growX();

        tblScore.add(lblMoves);// moves lbl
        tblScore.add(lblPushes);
        tblScore.add(lblTimer);

        //

        tblButtons = new Table();
        tblButtons.defaults().width(175f).height(175f).right();
        tblButtons.add(btnHome);
        tblButtons.add(btnLevels);
        tblButtons.add(btnUndo).spaceLeft(20f);


        root = new Table();
        root.setFillParent(true);
        //
        root.defaults().expandY();
        //

        root.add(tblScore).top().growX().row();
        root.add(tblButtons).bottom().right();


        winRoot = new Table();
        winRoot.setFillParent(true);
        //
        winRoot.add(winScore).width(1000f).height(520f).row(); //TODO change with dynamic size

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

        float delta = Gdx.graphics.getDeltaTime();

        if(!won){
            timer += delta;
            millis = (int) (timer*1000);


            //TODO refactor: define elsewhere / use less variables
            long hours = millis / 3600000;
            long minutes = (millis % 3600000) / 60000;
            long seconds = (millis % 60000) / 1000;

            if(hours>0)
                lblTimer.setText("Timer: "+ String.format("%02d:%02d:%02d", hours, minutes, seconds));
            else
                lblTimer.setText("Timer: "+ String.format("%02d:%02d", minutes, seconds));

            //TODO show timer in (hh):mm:ss

            if(player.isDirty() && tryMoving(player)) {// if the player needs to be drawn
                won = checkWin();
            }
        }else{
            //game.setScreen(new TitleScreen(game));//TODO change with more appropriate screen
            //TODO show score dialog
            winScore.setVisible(true);
            //TODO show timer with milliseconds also
            winMoves.setText(lblMoves.getText());
            winPushes.setText(lblPushes.getText());
            winTimer.setText(lblTimer.getText());
        }

    }
    private void draw(){
        ScreenUtils.clear(game.backgroundColor);//TEST

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
    private void uploadScore(){
        //TODO fix set disabled over here too
        btnPublishScore.setDisabled(true);
        btnPublishScore.setTouchable(Touchable.disabled);

        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest httpRequest = requestBuilder.newRequest().method(Net.HttpMethods.POST).header("Content-Type", "application/json").url(game.backend_url+"publish_score.php").build();

        Score score = new Score(game.user.username, level.id, millis, moves, pushes);

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);

        String jsonStr = json.toJson(score);

        httpRequest.setContent(jsonStr);
        Gdx.net.sendHttpRequest(httpRequest, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse){
                Gdx.app.postRunnable(()->{
                    String result = httpResponse.getResultAsString().trim(); //fix final \n

                    //Gdx.app.log("Error", httpResponse.getStatus().getStatusCode()+" "+result);

                    btnPublishScore.setText(result);
                });
            }

            @Override
            public void failed(Throwable t){
                Gdx.app.log("Error", t.getMessage());
            }

            @Override
            public void cancelled() {

            }
        });
    }
}
