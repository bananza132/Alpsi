package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class GameObject {
    Body body;
    short cBits;
    Texture texture;
    int width, height;

    GameObject(String texturePath, int x, int y, int width, int height, short cBits, World world) {
        this.width = width;
        this.height = height;
        this.cBits = cBits;

        texture = new Texture(texturePath);
        body = createBody(x, y, world);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - (width / 2f), getY() - (height / 2f), width, height);
    }

    public float getX() {
        return (body.getPosition().x / GameSettings.SCALE);
    }

    public void setX(float x) {
        body.setTransform(x * GameSettings.SCALE, body.getPosition().y, 0);
    }

    public float getY() {
        return (body.getPosition().y / GameSettings.SCALE);
    }

    public void setY(float y) {
        body.setTransform(body.getPosition().x, y * GameSettings.SCALE, 0);
    }

    public Body getBody(){
        return body;
    }

    public void setPosition(float x, float y){
        body.setTransform(x, y, 0);
    }

    public void hit() {
    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef();
        float[] vertsStone = {-30, 25, -46, 0, -46, -8, -30, -25, 29, -25, 46, -8, 46, 8, 29, 25};
        float[] vertsAlp = {-73, 44, -28, -100, 28, -100, 73, 44, 5, 100, -5, 100};
        Body createBody;
        PolygonShape polygonShape;
        CircleShape circleShape;
        FixtureDef fixtureDef;
        Fixture fixture;

        switch(cBits){
            case GameSettings.SMALL_STONE_BIT:
                def.type = BodyDef.BodyType.KinematicBody;
                def.fixedRotation = true;
                for (int i = 0; i < vertsStone.length; i++) {
                    vertsStone[i] *= GameSettings.SCALE;
                }
                createBody = world.createBody(def);

                polygonShape = new PolygonShape();
                polygonShape.set(vertsStone);

                fixtureDef = new FixtureDef();
                fixtureDef.shape = polygonShape;
                fixtureDef.density = 0.1f;
                fixtureDef.friction = 1f;
                fixtureDef.filter.categoryBits = cBits;
                fixtureDef.isSensor = false;

                fixture = createBody.createFixture(fixtureDef);
                fixture.setUserData(this);
                polygonShape.dispose();

                createBody.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0);
                return createBody;

            case GameSettings.ALP_BIT:
                def.type = BodyDef.BodyType.DynamicBody;
                def.fixedRotation = true;
                for (int i = 0; i < vertsAlp.length; i++) {
                    vertsAlp[i] *= GameSettings.SCALE;
                }
                createBody = world.createBody(def);
                polygonShape = new PolygonShape();
                polygonShape.set(vertsAlp);
                fixtureDef = new FixtureDef();
                fixtureDef.shape = polygonShape;
                fixtureDef.density = 0.1f;
                fixtureDef.friction = 1f;
                fixtureDef.filter.categoryBits = cBits;
                fixtureDef.isSensor = false;
                fixtureDef.filter.maskBits = GameSettings.STONE_BIT  | GameSettings.GROUND_BIT;
                fixture = createBody.createFixture(fixtureDef);
                fixture.setUserData(this);
                polygonShape.dispose();

                createBody.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0);
                return createBody;

            case GameSettings.GROUND_BIT:
                def.type = BodyDef.BodyType.StaticBody;
                def.fixedRotation = true;
                createBody = world.createBody(def);

                polygonShape = new PolygonShape();
                float halfWidth = (width / 2f) * GameSettings.SCALE;
                float halfHeight = (height / 2f) * GameSettings.SCALE;
                polygonShape.setAsBox(halfWidth, halfHeight);

                fixtureDef = new FixtureDef();
                fixtureDef.shape = polygonShape;
                fixtureDef.density = 0f;
                fixtureDef.friction = 1f;
                fixtureDef.filter.categoryBits = cBits;
                fixtureDef.filter.maskBits = GameSettings.ALP_BIT;

                fixture = createBody.createFixture(fixtureDef);
                fixture.setUserData(this);
                polygonShape.dispose();

                createBody.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0);
                return createBody;

            default:
                def.type = BodyDef.BodyType.DynamicBody;
                def.fixedRotation = true;
                createBody = world.createBody(def);

                circleShape = new CircleShape();
                circleShape.setRadius(Math.max(width, height) * GameSettings.SCALE / 2f);

                fixtureDef = new FixtureDef();
                fixtureDef.shape = circleShape;
                fixtureDef.density = 0.1f;
                fixtureDef.friction = 1f;
                fixtureDef.filter.categoryBits = cBits;
                fixtureDef.isSensor = false;
                if (cBits == GameSettings.STONE_BIT) {
                    fixtureDef.filter.maskBits = GameSettings.ALP_BIT;
                }
                fixture = createBody.createFixture(fixtureDef);
                fixture.setUserData(this);
                circleShape.dispose();

                createBody.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0);
                return createBody;
        }
    }

    public void dispose() {
        texture.dispose();
    }
}
