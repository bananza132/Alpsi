package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSettings;

public class AlpObject extends GameObject {
    int livesLeft;
    private boolean isMovingToStone = false;
    private SmallStoneObject targetStone;
    private boolean isFrozen = false;
    private Vector2 frozenVelocity = new Vector2();
    private float frozenAngularVelocity = 0;
    private Texture leftTexture;
    private Texture rightTexture;
    public AlpObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.ALP_BIT,world  );
        leftTexture = new Texture(GameResources.ALP_LEFT_IMG_PATH);
        rightTexture = new Texture(GameResources.ALP_RIGHT_IMG_PATH);
        body.setLinearDamping(10);
        livesLeft = 3;

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

    public void move(Vector3 target) {
        if (target.x - getX() < 0) {
            texture = leftTexture;
        } else {
            texture = rightTexture;
        }

        Vector2 targetMeters = new Vector2(target.x * GameSettings.SCALE, target.y * GameSettings.SCALE);
        Vector2 alpPosMeters = body.getPosition();
        Vector2 dir = targetMeters.cpy().sub(alpPosMeters);
        float distance = dir.len();
        if (distance < 5f) {
            body.setLinearVelocity(0, 0);
            return;
        }
        dir.nor();
        float speed = 100f;
        body.setLinearVelocity(dir.scl(speed));
    }

    public boolean isMoving() {
        if (body == null) return false;
        return body.getLinearVelocity().len() > 0.01f;
    }


    public int getLiveLeft() {
        return livesLeft;
    }

    @Override
    public void hit() {
        livesLeft -= 1;
    }

    public boolean isAlive() {
        return livesLeft > 0;
    }
    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
    @Override
    public void dispose() {
        super.dispose();
        leftTexture.dispose();
        rightTexture.dispose();
    }
}
