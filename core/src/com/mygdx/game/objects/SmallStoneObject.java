package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class SmallStoneObject extends GameObject{
    public SmallStoneObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.SMALL_STONE_BIT,world  );
        body.setLinearDamping(10);
    }

    public boolean isTouched(Vector3 vector3) {
        if (vector3.x >= getX()-width/2 && vector3.x <= getX()+width/2 && vector3.y >= getY()-height/2 &&
                vector3.y <= getY() + height/2){
            return true;
        }
        return false;
    }
}
