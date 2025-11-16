package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

import java.util.Random;

public class FallStoneObject extends GameObject {
    public FallStoneObject(int width, int height, String texturePath, World world) {
        super(
                texturePath,
                width / 2 + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - width)),
                GameSettings.SCREEN_HEIGHT + height / 2,
                width, height,
                world
        );
        body.setLinearVelocity(new Vector2(0, -GameSettings.STONE_VELOCITY));
    }

    public boolean isInFrame() {
        return getY() + height / 2 > 0;
    }


}
