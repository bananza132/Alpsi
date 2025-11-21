package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class SmallStoneObject extends GameObject{
    public SmallStoneObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.SMALL_STONE_BIT,world  );
        body.setLinearDamping(10);
    }

    public boolean isTouched(Vector3 vector3) {
        if (vector3.x >= getX()-width/2f && vector3.x <= getX()+width/2f && vector3.y >= getY()-height/2f &&
                vector3.y <= getY() + height/2f){
            return true;
        }
        return false;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
