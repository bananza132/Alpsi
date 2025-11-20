package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSession;
import com.mygdx.game.GameSettings;
import com.mygdx.game.GameState;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.components.BackgroundView;
import com.mygdx.game.components.ButtonView;
import com.mygdx.game.components.ImageView;
import com.mygdx.game.components.LiveView;
import com.mygdx.game.components.TextView;
import com.mygdx.game.managers.ContactManager;
import com.mygdx.game.managers.GrabManager;
import com.mygdx.game.objects.AlpObject;
import com.mygdx.game.objects.FallStoneObject;
import com.mygdx.game.objects.GroundObject;
import com.mygdx.game.objects.MountainObject;
import com.mygdx.game.objects.SmallStoneObject;

import java.util.ArrayList;
import java.util.Random;

public class ScreenGame extends ScreenAdapter   {
    MyGdxGame myGdxGame;
    GameSession gameSession;
    AlpObject alpObject;
    MountainObject mountainObject;
    private boolean hasMountainMoved = false;
    private boolean touchProcessed = false;
    private Vector3 lastTouch = new Vector3();
    ArrayList<FallStoneObject> stoneArray;
    ArrayList<SmallStoneObject> smallStoneArray;
    //SmallStoneObject smallStoneObject;
    GroundObject groundObject;
    ContactManager contactManager;
    GrabManager grabManager;
    private boolean isGrabbing = false;
    private boolean waitingForGrab = false;
    private SmallStoneObject targetStone;
    private boolean targetIsRightSide;
    private Vector3 targetTouchPos;
    private SmallStoneObject currentGrabbedStone;
    //Playing
    BackgroundView backgroundView;
    ImageView topBlackoutView;
    LiveView liveView;
    //TextView scoreTextView;
    ButtonView inventoryButton;

    // PAUSED state UI
    ImageView fullBlackoutView;
    TextView inventoryTextView;
    ButtonView homeButton;
    ButtonView continueButton;

    // ENDED state UI
    //TextView recordsTextView;
    //RecordsListView recordsListView;
    ButtonView homeButton2;

    public ScreenGame(MyGdxGame myGdxGame){
        gameSession = new GameSession();
        this.myGdxGame=myGdxGame;
        contactManager = new ContactManager(myGdxGame.world);
        grabManager = new GrabManager(myGdxGame.world);
       alpObject=new AlpObject(GameSettings.SCREEN_WIDTH/2,150,GameSettings.ALP_WIDTH,GameSettings.ALP_HEIGHT, GameResources.ALP_IMG_PATH,myGdxGame.world);
       mountainObject=new MountainObject(0,0,GameSettings.MOUNTAIN_WIDTH,GameSettings.MOUNTAIN_HEIGHT,GameResources.MOUNTAINS_IMG_PATH);
        stoneArray = new ArrayList<>();
        smallStoneArray = new ArrayList<>();
        int countSmallStones = 10;
        for(int i=0;i<countSmallStones;i++){
            SmallStoneObject smallStoneObject=new SmallStoneObject(
                    (i%2)*500 + new Random().nextInt(200), (i/2+1)*200+new Random().nextInt(150),
                    GameSettings.SMALL_STONE_WIDTH, GameSettings.SMALL_STONE_HEIGHT,
                    GameResources.SMALL_STONE_IMG_PATH, myGdxGame.world);
            smallStoneArray.add(smallStoneObject);
        }
        groundObject = new GroundObject(GameSettings.SCREEN_WIDTH/2, 0, 720, 100, GameResources.GROUND_IMG_PATH, myGdxGame.world);
        backgroundView = new BackgroundView(GameResources.BACKGROUND_DEATH_IMG_PATH);
        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(305, 1215);
        //scoreTextView = new TextView(myGdxGame.commonWhiteFont, 50, 1215);
        inventoryButton = new ButtonView(
                605, 1185,
                80, 80,
                GameResources.INVENTORY_IMG_PATH
        );

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);
        inventoryTextView = new TextView(myGdxGame.largeWhiteFont, 150, 1200, "Inventory/pause");
        homeButton = new ButtonView(
                138, 100,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
        continueButton = new ButtonView(
                393, 100,
                200, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Continue"
        );

        //recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 690);
        //recordsTextView = new TextView(myGdxGame.largeWhiteFont, 206, 842, "Last records");
        homeButton2 = new ButtonView(
                280, 365,
                160, 70,
                myGdxGame.commonBlackFont,
                GameResources.BUTTON_SHORT_BG_IMG_PATH,
                "Home"
        );
    }
    @Override
    public void show() {
        restartGame();
        int countSmallStones = 10;
        for(int i=0;i<countSmallStones;i++){
            SmallStoneObject smallStoneObject=new SmallStoneObject(
                    (i%2)*500 + new Random().nextInt(200), (i/2+1)*200+new Random().nextInt(150),
                    GameSettings.SMALL_STONE_WIDTH, GameSettings.SMALL_STONE_HEIGHT,
                    GameResources.SMALL_STONE_IMG_PATH, myGdxGame.world);
            smallStoneArray.add(smallStoneObject);
        }
    }

    @Override
    public void render(float delta) {
        myGdxGame.stepWorld();
        handleInput();
        if (waitingForGrab && !isGrabbing) {
            checkAndGrab();
        }
        mountainObject.move();
        if (gameSession.state == GameState.PLAYING) {
            if (gameSession.shouldSpawnStone()) {
                FallStoneObject stoneObject = new FallStoneObject(
                        GameSettings.STONE_WIDTH, GameSettings.STONE_HEIGHT,
                        GameResources.STONE_IMG_PATH,
                        myGdxGame.world
                );
                stoneArray.add(stoneObject);
            }
            if (!alpObject.isAlive()) {
                gameSession.state = GameState.ENDED;          }
            liveView.setLeftLives(alpObject.getLiveLeft());
        }



        updateStone();

        draw();

    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            switch (gameSession.state) {
                case PLAYING:
                    if (inventoryButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                        alpObject.freeze();
                        for(FallStoneObject stoneObject:stoneArray){
                            stoneObject.freeze();
                        }
                        break;
                    }

                    for (SmallStoneObject smallStone : smallStoneArray) {
                        if (smallStone.isTouched(myGdxGame.touch)) {

                            // Определяем сторону относительно альпиниста
                            boolean isRightSide = myGdxGame.touch.x >= alpObject.getX();
                            if (!isGrabbing && !waitingForGrab) {
                                // ПЕРВОЕ нажатие - начинаем движение к камню
                                alpObject.move(myGdxGame.touch);
                                mountainObject.startMoving();

                                // Сохраняем цель для последующего захвата
                                waitingForGrab = true;
                                targetStone = smallStone;
                                targetIsRightSide = isRightSide;
                                targetTouchPos = new Vector3(myGdxGame.touch);
                            } else {
                                releaseStone();
                                smallStone.body.setActive(false);
                                if (!isGrabbing && !waitingForGrab) {
                                    // ПЕРВОЕ нажатие - начинаем движение к камню
                                    alpObject.move(myGdxGame.touch);
                                    mountainObject.startMoving();
                                    groundObject.move();

                                    // Сохраняем цель для последующего захвата
                                    waitingForGrab = true;
                                    targetStone = smallStone;
                                    targetIsRightSide = isRightSide;
                                    targetTouchPos = new Vector3(myGdxGame.touch);
                                }
                            }

                            // Двигаем альпиниста

                            break;
                        }
                    }
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                        alpObject.unfreeze();
                        for(FallStoneObject stone:  stoneArray){
                            stone.unfreeze();
                        }
                    } else if (homeButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.screenMenu);
                    }
                    break;

                case ENDED:
                    if (homeButton2.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        myGdxGame.setScreen(myGdxGame.screenMenu);
                    }
                    break;
            }
        }
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        mountainObject.draw(myGdxGame.batch);
        for (FallStoneObject stone : stoneArray) stone.draw(myGdxGame.batch);
        groundObject.draw(myGdxGame.batch);
        alpObject.draw(myGdxGame.batch);
        for(SmallStoneObject smallStone: smallStoneArray) smallStone.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        inventoryButton.draw(myGdxGame.batch);
        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            inventoryTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            backgroundView.draw(myGdxGame.batch);
            fullBlackoutView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }
        myGdxGame.batch.end();
    }
    public void dispose() {
        mountainObject.dispose();
        alpObject.dispose();
        groundObject.dispose();
        for(SmallStoneObject smallStone: smallStoneArray) smallStone.dispose();
        for (FallStoneObject stone: stoneArray) {
            stone.dispose();
        }
        topBlackoutView.dispose();
        liveView.dispose();
        inventoryButton.dispose();
        fullBlackoutView.dispose();
        inventoryTextView.dispose();
        homeButton.dispose();
        homeButton2.dispose();
        continueButton.dispose();
        backgroundView.dispose();
    }

    private void updateStone() {
        for (int i = 0; i < stoneArray.size(); i++) {
            if (!stoneArray.get(i).isAlive()) {
                //gameSession.destructionRegistration();
                if (myGdxGame.audioManager.isSoundOn) myGdxGame.audioManager.stoneStoneSound.play(0.2f);
            }
            if (!stoneArray.get(i).isInFrame() || !stoneArray.get(i).isAlive()) {
                myGdxGame.world.destroyBody(stoneArray.get(i).body);
                stoneArray.remove(i--);
            }
        }
    }

    private void restartGame() {

        for (int i = 0; i < stoneArray.size(); i++) {
            myGdxGame.world.destroyBody(stoneArray.get(i).body);
            stoneArray.remove(i--);
        }
        for (int i = 0; i < smallStoneArray.size(); i++) {
            myGdxGame.world.destroyBody(smallStoneArray.get(i).body);
            smallStoneArray.remove(i--);
        }

        if (alpObject != null) {
            myGdxGame.world.destroyBody(alpObject.body);
        }
        mountainObject=new MountainObject(0,0,GameSettings.MOUNTAIN_WIDTH,GameSettings.MOUNTAIN_HEIGHT,GameResources.MOUNTAINS_IMG_PATH);

        alpObject = new AlpObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.ALP_WIDTH, GameSettings.ALP_HEIGHT,
                GameResources.ALP_IMG_PATH,
                myGdxGame.world
        );
        gameSession.startGame();
    }

    private void checkAndGrab() {
        if (targetStone == null) return;

        Vector2 alpPos = new Vector2(alpObject.getX(), alpObject.getY());
        Vector2 stonePos = new Vector2(targetStone.getX(), targetStone.getY());
        float distance = alpPos.dst(stonePos);

        System.out.println("Distance to stone: " + distance);

        // Если альпинист достаточно близко к камню - выполняем захват
        if (distance <= 200f) { // Уменьшите это значение по необходимости
            tryGrabStone(targetTouchPos, targetIsRightSide);
            waitingForGrab = false;
            targetStone = null;
        }

        if (!alpObject.isMoving() && distance > 50f) {
            waitingForGrab = false;
            targetStone = null;
        }
    }

    private void tryGrabStone(Vector3 touchPos, boolean flag) {

        for (SmallStoneObject smallStone : smallStoneArray) {
            if (smallStone.isTouched(touchPos)) {
                // Проверяем расстояние между Alp и камнем
                Vector2 alpPos = new Vector2(alpObject.getX(), alpObject.getY());
                Vector2 stonePos = new Vector2(smallStone.getX(), smallStone.getY());

                float distance = alpPos.dst(stonePos);
                System.out.println(distance);
                    float maxGrabDistance = 600f; // Максимальная дистанция захвата
                    Vector2 grabPoint;
                Vector2 alpPoint;
                        if(!flag){
                            grabPoint = new Vector2(smallStone.getX()+smallStone.getWidth()/2f,
                                    smallStone.getY()-smallStone.getHeight()/2f);
                            alpPoint=new Vector2(alpObject.getX()-alpObject.getWidth()/2f,alpObject.getY()+alpObject.getHeight()/2f);
                        }
                        else{
                            grabPoint = new Vector2(smallStone.getX()-smallStone.getWidth()/2f,
                                    smallStone.getY()-smallStone.getHeight()/2f);
                            alpPoint=new Vector2(alpObject.getX()+alpObject.getWidth()/2f,alpObject.getY()+alpObject.getHeight()/2f);
                        }


                        // Создаем соединение
                        grabManager.grab(alpObject.body, smallStone.body, grabPoint,alpPoint);
                        System.out.println("Grabbed");

                        isGrabbing = true;
                        currentGrabbedStone = smallStone;
                        return;


            }
        }
    }

    private void releaseStone() {
        grabManager.release();
        System.out.println("Released");
        isGrabbing = false;
        currentGrabbedStone = null;
    }


}
