package com.mygdx.game.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

import java.util.Random;

public class FallStoneObject extends GameObject {
    private int livesLeft;
    private boolean isFrozen;
    private Vector2 frozenVelocity;
    private float frozenAngularVelocity;

    public FallStoneObject(int width, int height, String texturePath, World world) {
        super(texturePath, width / 2 + (new Random()).nextInt((GameSettings.SCREEN_WIDTH - width)), GameSettings.SCREEN_HEIGHT + height / 2, width, height, GameSettings.STONE_BIT, world);
        body.setLinearVelocity(new Vector2(0, -GameSettings.STONE_VELOCITY));
        livesLeft = 1;
        isFrozen = false;
        frozenVelocity = new Vector2();
        frozenAngularVelocity = 0;

    }

    public void freeze() {
        if (body != null && !isFrozen) {
            frozenVelocity.set(body.getLinearVelocity());
            frozenAngularVelocity = body.getAngularVelocity();
            body.setType(BodyDef.BodyType.StaticBody);
            isFrozen = true;
        }
    }

    public void unfreeze() {
        if (body != null && isFrozen) {
            body.setType(BodyDef.BodyType.DynamicBody);
            body.setLinearVelocity(frozenVelocity);
            body.setAngularVelocity(frozenAngularVelocity);

            isFrozen = false;
        }
    }

    public boolean isInFrame() {
        return getY() + height / 2f > 0;
    }

    @Override
    public void hit() {
        livesLeft -= 1;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }

}
