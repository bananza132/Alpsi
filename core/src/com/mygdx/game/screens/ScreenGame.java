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
import com.mygdx.game.components.RecordsListView;
import com.mygdx.game.components.TextView;
import com.mygdx.game.managers.ContactManager;
import com.mygdx.game.managers.GrabManager;
import com.mygdx.game.managers.MemoryManager;
import com.mygdx.game.objects.AlpObject;
import com.mygdx.game.objects.FallStoneObject;
import com.mygdx.game.objects.GroundObject;
import com.mygdx.game.objects.MountainObject;
import com.mygdx.game.objects.SmallStoneObject;

import java.util.ArrayList;
import java.util.Random;

public class ScreenGame extends ScreenAdapter {
    private final MyGdxGame myGdxGame;
    private final GameSession gameSession;
    private final ImageView topBlackoutView;
    private final LiveView liveView;
    private final TextView scoreTextView;
    private final ButtonView inventoryButton;
    private final ImageView fullBlackoutView;
    private final TextView inventoryTextView;
    private final ButtonView homeButton;
    private final ButtonView continueButton;
    private final ImageView amogusImage;
    private final BackgroundView backgroundView;
    private final TextView recordsTextView;
    private final RecordsListView recordsListView;
    private final ButtonView homeButton2;
    private ContactManager contactManager;
    private GrabManager grabManager;
    private AlpObject alpObject;
    private MountainObject mountainObject;
    private GroundObject groundObject;
    private ArrayList<FallStoneObject> stoneArray;
    private ArrayList<SmallStoneObject> smallStoneArray;
    private SmallStoneObject pastStone;
    private SmallStoneObject targetStone;
    private boolean targetIsRightSide;
    private boolean isGrabbing;
    private boolean waitingForGrab;

    private float pastAlpHeight;
    private boolean hasMountainMoved;
    private boolean movementTriggered;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        gameSession = new GameSession();
        contactManager = new ContactManager(myGdxGame.world);
        grabManager = new GrabManager(myGdxGame.world);
        alpObject = new AlpObject(GameSettings.SCREEN_WIDTH / 2, 150, GameSettings.ALP_WIDTH, GameSettings.ALP_HEIGHT, GameResources.ALP_IMG_PATH, myGdxGame.world);
        mountainObject = new MountainObject(0, 0, GameSettings.MOUNTAIN_WIDTH, GameSettings.MOUNTAIN_HEIGHT, GameResources.MOUNTAINS_IMG_PATH);
        groundObject = new GroundObject(GameSettings.SCREEN_WIDTH / 2, 0, 720, 100, GameResources.GROUND_IMG_PATH, myGdxGame.world);
        stoneArray = new ArrayList<>();
        smallStoneArray = new ArrayList<>();
        for (int i = 0; i < GameSettings.SMALL_STONES_COUNT; i++) {
            SmallStoneObject smallStoneObject = new SmallStoneObject((i % 2) * 500 + new Random().nextInt(200), (i / 2 + 1) * 200 + new Random().nextInt(150), GameSettings.SMALL_STONE_WIDTH, GameSettings.SMALL_STONE_HEIGHT, GameResources.SMALL_STONE_IMG_PATH, myGdxGame.world);
            smallStoneArray.add(smallStoneObject);
        }

        topBlackoutView = new ImageView(0, 1180, GameResources.BLACKOUT_TOP_IMG_PATH);
        liveView = new LiveView(470, 1215);
        scoreTextView = new TextView(myGdxGame.commonBlackFont, 50, 1215);
        inventoryButton = new ButtonView(605, 1185, 80, 80, GameResources.INVENTORY_IMG_PATH);

        fullBlackoutView = new ImageView(0, 0, GameResources.BLACKOUT_FULL_IMG_PATH);
        inventoryTextView = new TextView(myGdxGame.largeWhiteFont, 250, 1200, "Inventory");
        homeButton = new ButtonView(138, 100, 200, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_SHORT_BG_IMG_PATH, "Home");
        continueButton = new ButtonView(393, 100, 200, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_SHORT_BG_IMG_PATH, "Continue");
        amogusImage = new ImageView(140, 300, GameSettings.AMOGUS_WIDTH, GameSettings.AMOGUS_HEIGHT, GameResources.AMOGUS_IMG_PATH);

        backgroundView = new BackgroundView(GameResources.BACKGROUND_DEATH_IMG_PATH);
        recordsListView = new RecordsListView(myGdxGame.commonWhiteFont, 690);
        recordsTextView = new TextView(myGdxGame.largeWhiteFont, 206, 842, "Last records");
        homeButton2 = new ButtonView(280, 365, 160, 70, myGdxGame.commonBlackFont, GameResources.BUTTON_SHORT_BG_IMG_PATH, "Home");

        isGrabbing = false;
        waitingForGrab = false;

        pastAlpHeight = alpObject.getY();
        hasMountainMoved = false;
        movementTriggered = false;
    }

    @Override
    public void show() {
        restartGame();
    }

    @Override
    public void render(float delta) {
        myGdxGame.stepWorld();
        handleInput();
        if (gameSession.state == GameState.PLAYING) {
            moveAlpTowardsStone();
            checkAndGrab();
            mountainObject.move();
            checkAndRespawnSmallStones();
            moveEnvironment();
            movementTriggered = true;
            if (!isGrabbing && movementTriggered) {
                movementTriggered = false;
            }
            if (gameSession.shouldSpawnStone()) {
                FallStoneObject stoneObject = new FallStoneObject(GameSettings.STONE_WIDTH, GameSettings.STONE_HEIGHT, GameResources.STONE_IMG_PATH, myGdxGame.world);
                stoneArray.add(stoneObject);
            }
            if (!alpObject.isAlive()) {
                gameSession.endGame();
                recordsListView.setRecords(MemoryManager.loadRecordsTable());
            }
            liveView.setLeftLives(alpObject.getLiveLeft());
            gameSession.updateScore();
            scoreTextView.setText("Score: " + gameSession.getScore());
            updateStone();
        }
        draw();
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();

        mountainObject.draw(myGdxGame.batch);
        groundObject.draw(myGdxGame.batch);
        for (SmallStoneObject smallStone : smallStoneArray) smallStone.draw(myGdxGame.batch);
        alpObject.draw(myGdxGame.batch);
        for (FallStoneObject stone : stoneArray) stone.draw(myGdxGame.batch);
        topBlackoutView.draw(myGdxGame.batch);
        scoreTextView.draw(myGdxGame.batch);
        liveView.draw(myGdxGame.batch);
        inventoryButton.draw(myGdxGame.batch);

        if (gameSession.state == GameState.PAUSED) {
            fullBlackoutView.draw(myGdxGame.batch);
            inventoryTextView.draw(myGdxGame.batch);
            homeButton.draw(myGdxGame.batch);
            continueButton.draw(myGdxGame.batch);
            amogusImage.draw(myGdxGame.batch);
        } else if (gameSession.state == GameState.ENDED) {
            backgroundView.draw(myGdxGame.batch);
            fullBlackoutView.draw(myGdxGame.batch);
            recordsTextView.draw(myGdxGame.batch);
            recordsListView.draw(myGdxGame.batch);
            homeButton2.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            switch (gameSession.state) {
                case PLAYING:
                    if (inventoryButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.pauseGame();
                        alpObject.freeze();
                        for (FallStoneObject stoneObject : stoneArray) {
                            stoneObject.freeze();
                        }
                        break;
                    }
                    for (SmallStoneObject smallStone : smallStoneArray) {
                        if (smallStone.isTouched(myGdxGame.touch)) {
                            if (myGdxGame.touch.y - alpObject.getY() > -20f && myGdxGame.touch.y - alpObject.getY() < 400f) {
                                if (!isGrabbing && !waitingForGrab) {
                                    moveAndGrab(smallStone);
                                } else {
                                    releaseStone();
                                    if (!isGrabbing && !waitingForGrab) {
                                        moveAndGrab(smallStone);
                                    }
                                }
                            }
                            break;
                        }
                    }
                    break;

                case PAUSED:
                    if (continueButton.isHit(myGdxGame.touch.x, myGdxGame.touch.y)) {
                        gameSession.resumeGame();
                        alpObject.unfreeze();
                        for (FallStoneObject stone : stoneArray) {
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

    private void moveAndGrab(SmallStoneObject smallStone) {
        pastAlpHeight = alpObject.getY();
        if (pastStone != null) {
            targetIsRightSide = pastStone.getX() <= smallStone.getX();
        } else {
            targetIsRightSide = smallStone.getX() >= alpObject.getX();
        }
        waitingForGrab = true;
        targetStone = smallStone;
        if (targetIsRightSide) {
            alpObject.move(new Vector3(targetStone.getX() + targetStone.getWidth() / 2f, targetStone.getY(), 0), targetIsRightSide);
        } else {
            alpObject.move(new Vector3(targetStone.getX() - targetStone.getWidth() / 2f, targetStone.getY(), 0), targetIsRightSide);
        }
        pastStone = smallStone;
    }

    private void moveAlpTowardsStone() {
        if (!waitingForGrab || targetStone == null || alpObject == null) return;

        Vector2 alpPos = new Vector2(alpObject.getX() * GameSettings.SCALE, alpObject.getY() * GameSettings.SCALE);
        Vector2 targetStoneCenter = new Vector2(targetStone.getX() * GameSettings.SCALE, targetStone.getY() * GameSettings.SCALE);
        float halfWidthMeters = (targetStone.getWidth() * GameSettings.SCALE) / 2f;

        Vector2 targetPoint;
        if (targetIsRightSide) {
            targetPoint = new Vector2(targetStoneCenter.x + halfWidthMeters, targetStoneCenter.y);
        } else {
            targetPoint = new Vector2(targetStoneCenter.x - halfWidthMeters, targetStoneCenter.y);
        }

        float distance = alpPos.dst(targetPoint);
        if (distance > 0.5f) {
            alpObject.moveTowards(targetPoint, GameSettings.ALP_SPEED);
        } else {
            checkAndGrab();
        }
    }

    private void checkAndGrab() {
        if (!waitingForGrab || isGrabbing || targetStone == null) return;

        Vector2 alpPos = new Vector2(alpObject.getX(), alpObject.getY());
        Vector2 stonePos = new Vector2(targetStone.getX(), targetStone.getY());
        float distance = alpPos.dst(stonePos);
        if (distance <= 200f) {
            tryGrabStone(new Vector3(targetStone.getX(), targetStone.getY(), 0), targetIsRightSide);
            targetStone = null;
            waitingForGrab = false;
        }

    }

    private void checkAndRespawnSmallStones() {
        float maxY = -GameSettings.SCREEN_HEIGHT;
        for (SmallStoneObject stone : smallStoneArray) {
            if (stone.getY() > maxY) {
                maxY = stone.getY();
            }
        }
        for (int i = 0; i < smallStoneArray.size(); i++) {
            SmallStoneObject stone = smallStoneArray.get(i);
            if (stone.getY() + stone.getHeight() < 0) {
                Random random = new Random();
                float newX = (i % 2) * 500 + random.nextInt(200);
                float newY;
                if (i < 2) {
                    newY = maxY + (i + 1) % 2 * 100 + random.nextInt(50);
                } else {
                    newY = maxY + (i % 2) * 150 + random.nextInt(50);
                }
                stone.setPosition(newX * GameSettings.SCALE, newY * GameSettings.SCALE);
                if (newY > maxY) {
                    maxY = newY;
                }
            }
        }
    }

    private void moveEnvironment() {
        if (isGrabbing && !movementTriggered && alpObject.getY() - pastAlpHeight > 25f) {
            float dropDistance = alpObject.getY() / 3f;
            for (SmallStoneObject s : smallStoneArray) {
                float nx = s.getX() * GameSettings.SCALE;
                float ny = s.getY() * GameSettings.SCALE - (dropDistance * GameSettings.SCALE);
                s.setPosition(nx, ny);
            }
            alpObject.setPosition(alpObject.getX() * GameSettings.SCALE, alpObject.getY() * GameSettings.SCALE - dropDistance * GameSettings.SCALE);
            mountainObject.startMoving();
            if (!hasMountainMoved) {
                groundObject.move();
                hasMountainMoved = true;
            }
        }
    }

    private void updateStone() {
        for (int i = 0; i < stoneArray.size(); i++) {
            if (!stoneArray.get(i).isAlive()) {
                if (myGdxGame.audioManager.isSoundOn)
                    myGdxGame.audioManager.stoneStoneSound.play(0.2f);
            }
            if (!stoneArray.get(i).isInFrame() || !stoneArray.get(i).isAlive()) {
                myGdxGame.world.destroyBody(stoneArray.get(i).getBody());
                stoneArray.remove(i--);
            }
        }
    }

    private void tryGrabStone(Vector3 touchPos, boolean flag) {
        for (SmallStoneObject smallStone : smallStoneArray) {
            if (smallStone == targetStone && targetStone != null) {
                if (alpObject == null) {
                    waitingForGrab = false;
                    return;
                }
                float grabX, grabY;
                if (!flag) {
                    grabX = smallStone.getX() + smallStone.getWidth() / 2f;
                    grabY = smallStone.getY();
                } else {
                    grabX = smallStone.getX() - smallStone.getWidth() / 2f;
                    grabY = smallStone.getY();
                }

                float alpHandX = flag ? (alpObject.getX() + alpObject.getWidth() / 2f) : (alpObject.getX() - alpObject.getWidth() / 2f);
                float alpHandY = alpObject.getY() + GameSettings.ALP_HAND;

                Vector2 grabPointMeters = new Vector2(grabX * GameSettings.SCALE, grabY * GameSettings.SCALE);
                Vector2 alpPointMeters = new Vector2(alpHandX * GameSettings.SCALE, alpHandY * GameSettings.SCALE);
                grabManager.grab(alpObject.getBody(), smallStone.getBody(), grabPointMeters, alpPointMeters);
                if (myGdxGame.audioManager.isSoundOn)
                    myGdxGame.audioManager.stoneDoodleSound.play(0.2f);
                gameSession.climbingRegistration();
                isGrabbing = true;
                return;
            }
        }
    }

    private void releaseStone() {
        grabManager.release();
        isGrabbing = false;
    }

    private void restartGame() {
        contactManager = new ContactManager(myGdxGame.world);
        grabManager = new GrabManager(myGdxGame.world);
        if (alpObject != null) {
            myGdxGame.world.destroyBody(alpObject.getBody());
        }
        for (int i = 0; i < stoneArray.size(); i++) {
            myGdxGame.world.destroyBody(stoneArray.get(i).getBody());
            stoneArray.remove(i--);
        }
        for (int i = 0; i < smallStoneArray.size(); i++) {
            myGdxGame.world.destroyBody(smallStoneArray.get(i).getBody());
            smallStoneArray.remove(i--);
        }

        alpObject = new AlpObject(GameSettings.SCREEN_WIDTH / 2, 150, GameSettings.ALP_WIDTH, GameSettings.ALP_HEIGHT, GameResources.ALP_IMG_PATH, myGdxGame.world);
        mountainObject = new MountainObject(0, 0, GameSettings.MOUNTAIN_WIDTH, GameSettings.MOUNTAIN_HEIGHT, GameResources.MOUNTAINS_IMG_PATH);
        groundObject = new GroundObject(GameSettings.SCREEN_WIDTH / 2, 0, 720, 100, GameResources.GROUND_IMG_PATH, myGdxGame.world);
        smallStoneArray = new ArrayList<>();
        for (int i = 0; i < GameSettings.SMALL_STONES_COUNT; i++) {
            SmallStoneObject smallStoneObject = new SmallStoneObject((i % 2) * 500 + new Random().nextInt(200), (i / 2 + 1) * 200 + new Random().nextInt(150), GameSettings.SMALL_STONE_WIDTH, GameSettings.SMALL_STONE_HEIGHT, GameResources.SMALL_STONE_IMG_PATH, myGdxGame.world);
            smallStoneArray.add(smallStoneObject);
        }

        targetStone = null;
        isGrabbing = false;
        waitingForGrab = false;

        hasMountainMoved = false;
        movementTriggered = false;

        gameSession.startGame();
    }

    public void dispose() {
        alpObject.dispose();
        mountainObject.dispose();
        groundObject.dispose();
        for (FallStoneObject stone : stoneArray) stone.dispose();
        for (SmallStoneObject smallStone : smallStoneArray) smallStone.dispose();

        topBlackoutView.dispose();
        liveView.dispose();
        scoreTextView.dispose();
        inventoryButton.dispose();

        fullBlackoutView.dispose();
        inventoryTextView.dispose();
        homeButton.dispose();
        continueButton.dispose();
        amogusImage.dispose();

        backgroundView.dispose();
        recordsTextView.dispose();
        recordsListView.dispose();
        homeButton2.dispose();
    }
}
