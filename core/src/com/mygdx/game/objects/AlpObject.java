package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class AlpObject extends GameObject {
    public AlpObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, world  );
        body.setLinearDamping(10);

    }

    public void move(Vector3 vector3) {
        float fx = (vector3.x - getX()) * GameSettings.ALP_FORCE_RATIO;
        float fy = (vector3.y - getY()) * GameSettings.ALP_FORCE_RATIO;
        body.applyForceToCenter(
                new Vector2(
                        (vector3.x - getX()) * GameSettings.ALP_FORCE_RATIO,
                        (vector3.y - getY()) * GameSettings.ALP_FORCE_RATIO
                ),
                true
        );


    }
}
