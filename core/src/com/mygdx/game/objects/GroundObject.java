package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class GroundObject extends GameObject{
    public GroundObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.GROUND_BIT,world  );
        body = createBody(x, y, world);
    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef();

        def.type = BodyDef.BodyType.StaticBody; // тип тела, который имеет массу и может быть подвинут под действием сил
        def.fixedRotation = true; // запрещаем телу вращаться вокруг своей оси
        def.position.set(x, y);

        Body body = world.createBody(def);

        PolygonShape polygonShape = new PolygonShape();
        float halfWidth = (width / 2f) * GameSettings.SCALE;
        float halfHeight = (height / 2f) * GameSettings.SCALE;
        polygonShape.setAsBox(halfWidth, halfHeight); // определяем радиус круга коллайдера

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape; // устанавливаем коллайдер
        fixtureDef.density = 0f; // устанавливаем плотность тела
        fixtureDef.friction = 1f; // устанвливаем коэффициент трения
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = GameSettings.ALP_BIT;

        body.createFixture(fixtureDef); // создаём fixture по описанному нами определению
        polygonShape.dispose(); // так как коллайдер уже скопирован в fixutre, то circleShape может быть отчищена, чтобы не забивать оперативную память.

        body.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0); // устанавливаем позицию тела по координатным осям и угол поворота
        return body;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, getX()-width/2f, getY(), width, height/2f);
    }
}
