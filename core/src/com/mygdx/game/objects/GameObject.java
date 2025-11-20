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
    Texture texture;
    int width, height;
    public Body body;
    public short cBits;
    GameObject(String texturePath, int x, int y, int width, int height, short cBits, World world) {
        this.width = width;
        this.height = height;
        this.cBits = cBits;

        texture = new Texture(texturePath);
        if(cBits != GameSettings.GROUND_BIT) body = createBody(x, y, world);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX() - (width / 2f), getY() - (height / 2f), width, height);
    }

    public int getX() {
        return (int) (body.getPosition().x / GameSettings.SCALE);
    }

    public int getY() {
        return (int) (body.getPosition().y / GameSettings.SCALE);
    }

    public void setX(int x) {
        body.setTransform(x * GameSettings.SCALE, body.getPosition().y, 0);
    }

    public void setY(int y) {
        body.setTransform(body.getPosition().x, y * GameSettings.SCALE, 0);
    }

    public void hit() {
    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef();

        if (cBits == GameSettings.SMALL_STONE_BIT){
            def.type = BodyDef.BodyType.KinematicBody;
            def.fixedRotation = true;
            float[] verts = {-59, 100, -92, 0, -92, -16, -59, -50, 58, -50, 92, -16, 92, 16, 58, 100};
            for (int i = 0; i < verts.length; i++) {
                verts[i] *= GameSettings.SCALE;
            }
            Body body = world.createBody(def);

            PolygonShape shape = new PolygonShape();
            shape.set(verts);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 0.1f;
            fixtureDef.friction = 1f;
            fixtureDef.filter.categoryBits = cBits;
            fixtureDef.isSensor = false;
            if(cBits==GameSettings.STONE_BIT){
                fixtureDef.filter.maskBits = GameSettings.ALP_BIT;
            }
            else if(cBits==GameSettings.ALP_BIT){
                fixtureDef.filter.maskBits = GameSettings.STONE_BIT | GameSettings.SMALL_STONE_BIT | GameSettings.GROUND_BIT;
            }
            else if(cBits == GameSettings.SMALL_STONE_BIT){
                fixtureDef.filter.maskBits = GameSettings.ALP_BIT;
            }

            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(this);
            shape.dispose();

            body.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0);
            return body;
        }
        else if (cBits == GameSettings.ALP_BIT){
            def.type = BodyDef.BodyType.KinematicBody;
            def.type = BodyDef.BodyType.DynamicBody;
            def.fixedRotation = true;
            float[] verts = {-109, 66, -43, -150, 40, -150, 109, 66, 6, 150, -9, 150};
            for (int i = 0; i < verts.length; i++) {
                verts[i] *= GameSettings.SCALE;
            }
            Body body = world.createBody(def);
            PolygonShape shape = new PolygonShape();
            shape.set(verts);
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 0.1f;
            fixtureDef.friction = 1f;
            fixtureDef.filter.categoryBits = cBits;
            fixtureDef.isSensor = false;
            if(cBits==GameSettings.STONE_BIT){
                fixtureDef.filter.maskBits = GameSettings.ALP_BIT;
            }
            else if(cBits==GameSettings.ALP_BIT){
                fixtureDef.filter.maskBits = GameSettings.STONE_BIT | GameSettings.SMALL_STONE_BIT | GameSettings.GROUND_BIT;
            }
            else if(cBits == GameSettings.SMALL_STONE_BIT){
                fixtureDef.filter.maskBits = GameSettings.ALP_BIT;
            }
            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(this);
            shape.dispose();

            body.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0);
            return body;
        }
        else{
            def.type = BodyDef.BodyType.DynamicBody;
            def.fixedRotation = true;
            Body body = world.createBody(def);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(Math.max(width, height) * GameSettings.SCALE / 2f);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = circleShape;
            fixtureDef.density = 0.1f;
            fixtureDef.friction = 1f;
            fixtureDef.filter.categoryBits = cBits;
            fixtureDef.isSensor = false;
            if(cBits==GameSettings.STONE_BIT){
                fixtureDef.filter.maskBits = GameSettings.ALP_BIT;
            }
            else if(cBits==GameSettings.ALP_BIT){
                fixtureDef.filter.maskBits = GameSettings.STONE_BIT | GameSettings.SMALL_STONE_BIT | GameSettings.GROUND_BIT;
            }
            else if(cBits == GameSettings.SMALL_STONE_BIT){
                fixtureDef.filter.maskBits = GameSettings.ALP_BIT;
            }

            Fixture fixture = body.createFixture(fixtureDef);
            fixture.setUserData(this);
            circleShape.dispose();

            body.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0);
            return body;
        }
    }

    public void dispose (){

        texture.dispose();
   }
}
