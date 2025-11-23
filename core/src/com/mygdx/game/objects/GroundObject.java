package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class GroundObject extends GameObject {
    public GroundObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.GROUND_BIT, world);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - width / 2f, getY(), width, height / 2f);
    }

    public void move() {
        body.setTransform(body.getPosition().x, -height * 2 * GameSettings.SCALE, 0);
    }
}
