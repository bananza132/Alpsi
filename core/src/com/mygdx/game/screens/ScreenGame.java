package com.mygdx.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSettings;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.objects.AlpObject;
import com.mygdx.game.objects.MountainObject;

public class ScreenGame extends ScreenAdapter   {
    MyGdxGame myGdxGame;
    AlpObject alpObject;
    MountainObject mountainObject;
    public ScreenGame(MyGdxGame myGdxGame){
        this.myGdxGame=myGdxGame;
       alpObject=new AlpObject(GameSettings.SCREEN_WIDTH/2,150,GameSettings.ALP_WIDTH,GameSettings.ALP_HEIGHT, GameResources.ALP_IMG_PATH,myGdxGame.world);
       mountainObject=new MountainObject(0,0,GameSettings.MOUNTAIN_WIDTH,GameSettings.MOUNTAIN_HEIGHT,GameResources.MOUNTAINS_IMG_PATH,myGdxGame.world);
    }
}
