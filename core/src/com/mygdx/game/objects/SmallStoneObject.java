package com.mygdx.game.objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class SmallStoneObject extends GameObject{
    public SmallStoneObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.SMALL_STONE_BIT,world  );
        body = createBody(x, y, world);
        body.setLinearDamping(10);
    }

    private Body createBody(float x, float y, World world) {
        BodyDef def = new BodyDef(); // def - defenition (определение) это объект, который содержит все данные, необходимые для посторения тела

        def.type = BodyDef.BodyType.KinematicBody; // тип тела, который имеет массу и может быть подвинут под действием сил
        def.fixedRotation = true; // запрещаем телу вращаться вокруг своей оси
        Body body = world.createBody(def); // создаём в мире world объект по описанному нами определению

        CircleShape circleShape = new CircleShape(); // задаём коллайдер в форме круга
        circleShape.setRadius(Math.max(width, height) * GameSettings.SCALE / 2f); // определяем радиус круга коллайдера

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape; // устанавливаем коллайдер
        fixtureDef.density = 0.1f; // устанавливаем плотность тела
        fixtureDef.friction = 1f; // устанвливаем коэффициент трения
        fixtureDef.filter.categoryBits = cBits;
        fixtureDef.filter.maskBits = GameSettings.ALP_BIT;

        body.createFixture(fixtureDef); // создаём fixture по описанному нами определению
        circleShape.dispose(); // так как коллайдер уже скопирован в fixutre, то circleShape может быть отчищена, чтобы не забивать оперативную память.

        body.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0); // устанавливаем позицию тела по координатным осям и угол поворота
        return body;
    }
}
