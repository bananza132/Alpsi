package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameResources;
import com.mygdx.game.GameSettings;

public class AlpObject extends GameObject {
    int livesLeft;
    private boolean isMovingToStone = false;
    private SmallStoneObject targetStone;
    public AlpObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.ALP_BIT,world  );
        body.setLinearDamping(10);
        livesLeft = 3;

    }

    public void move(Vector3 vector3) {
        // Меняем текстуру в зависимости от направления
        if (vector3.x - getX() < 0) {
            texture = new Texture(GameResources.ALP_LEFT_IMG_PATH);
        } else {
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
