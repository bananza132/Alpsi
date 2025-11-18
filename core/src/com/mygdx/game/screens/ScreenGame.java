package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSession;
import com.mygdx.game.GameSettings;
import com.mygdx.game.MyGdxGame;
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
    ArrayList<FallStoneObject> stoneArray;
    ArrayList<SmallStoneObject> smallStoneArray;
    //SmallStoneObject smallStoneObject;
    GroundObject groundObject;

    public ScreenGame(MyGdxGame myGdxGame){
        gameSession = new GameSession();
        this.myGdxGame=myGdxGame;
       alpObject=new AlpObject(GameSettings.SCREEN_WIDTH/2,100,GameSettings.ALP_WIDTH,GameSettings.ALP_HEIGHT, GameResources.ALP_IMG_PATH,myGdxGame.world);
       mountainObject=new MountainObject(0,0,GameSettings.MOUNTAIN_WIDTH,GameSettings.MOUNTAIN_HEIGHT,GameResources.MOUNTAINS_IMG_PATH);
        stoneArray = new ArrayList<>();
        smallStoneArray = new ArrayList<>();
        int countSmallStones = 5 + new Random().nextInt(4);
        for(int i=0;i<countSmallStones;i++){
            SmallStoneObject smallStoneObject=new SmallStoneObject(
                    new Random().nextInt(GameSettings.SCREEN_WIDTH), 100+new Random().nextInt(GameSettings.SCREEN_HEIGHT-100),
                    GameSettings.SMALL_STONE_WIDTH, GameSettings.SMALL_STONE_HEIGHT,
                    GameResources.SMALL_STONE_IMG_PATH, myGdxGame.world);
            smallStoneArray.add(smallStoneObject);
        }
        //groundObject = new GroundObject(0, 0, 720, 50, GameResources.GROUND_IMG_PATH, myGdxGame.world);
    }
    @Override
    public void show() {
        gameSession.startGame();
    }

    @Override
    public void render(float delta) {
        myGdxGame.stepWorld();
        handleInput();
        if (gameSession.shouldSpawnStone()) {
            FallStoneObject stoneObject = new FallStoneObject(
                    GameSettings.STONE_WIDTH, GameSettings.STONE_HEIGHT,
                    GameResources.STONE_IMG_PATH,
                    myGdxGame.world
            );
            stoneArray.add(stoneObject);
        }

        updateStone();

        draw();

    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            myGdxGame.touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            alpObject.move(myGdxGame.touch);
        }
    }

    private void draw() {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        myGdxGame.batch.begin();
        mountainObject.draw(myGdxGame.batch);
        for(SmallStoneObject smallStone: smallStoneArray) smallStone.draw(myGdxGame.batch);
        for (FallStoneObject stone : stoneArray) stone.draw(myGdxGame.batch);
        //groundObject.draw(myGdxGame.batch);
        alpObject.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }
    public void dispose() {
        mountainObject.dispose();
        alpObject.dispose();
        //groundObject.dispose();
        for(SmallStoneObject smallStone: smallStoneArray) smallStone.dispose();
        for (FallStoneObject stone: stoneArray) {
            stone.dispose();
        }
    }

    private void updateStone() {
        for (int i = 0; i < stoneArray.size(); i++) {
            if (!stoneArray.get(i).isInFrame()) {
                myGdxGame.world.destroyBody(stoneArray.get(i).body);
                stoneArray.remove(i--);
            }
        }
    }


}
