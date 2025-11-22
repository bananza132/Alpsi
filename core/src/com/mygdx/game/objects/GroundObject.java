package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class GroundObject extends GameObject {
    boolean isMoving = false;

    public GroundObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.GROUND_BIT, world);
        body = createBody(x, y, world);
    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody;
        def.fixedRotation = true;
        Body body = world.createBody(def);

        PolygonShape polygonShape = new PolygonShape();
        float halfWidth = (width / 2f) * GameSettings.SCALE;
        float halfHeight = (height / 2f) * GameSettings.SCALE;
        polygonShape.setAsBox(halfWidth, halfHeight);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.density = 0f;
        fixtureDef.friction = 1f;
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = GameSettings.ALP_BIT;

        body.createFixture(fixtureDef);
        polygonShape.dispose();

        body.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0);
        return body;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - width / 2f, getY(), width, height / 2f);
    }

    public void startMoving() {
        isMoving = true;
    }

    public void stopMoving() {
        isMoving = false;
    }

    public void move() {
        body.setTransform(body.getPosition().x, -height * 2 * GameSettings.SCALE, 0);
    }
}
