package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSettings;

public class AlpObject extends GameObject {
    int livesLeft;
    public AlpObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.ALP_BIT,world  );
        body.setLinearDamping(10);
        livesLeft = 3;

    }

    public void move(Vector3 vector3) {
        float fx = (vector3.x - getX()) * GameSettings.ALP_FORCE_RATIO;
        float fy = (vector3.y - getY()) * GameSettings.ALP_FORCE_RATIO;
        if(vector3.x - getX() < 0){
            texture = new Texture(GameResources.ALP_LEFT_IMG_PATH);
        }
        else{
            texture = new Texture(GameResources.ALP_RIGHT_IMG_PATH);
        }
        body.setLinearVelocity(
                new Vector2(
                        (vector3.x - getX()),
                        (vector3.y - getY())
                )
        );
    }

    public int getLiveLeft() {
        return livesLeft;
    }

    @Override
    public void hit() {
        livesLeft -= 1;
        System.out.println(livesLeft);
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }
}
