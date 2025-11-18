package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
        BodyDef def = new BodyDef(); // def - defenition (определение) это объект, который содержит все данные, необходимые для посторения тела

        def.type = BodyDef.BodyType.DynamicBody; // тип тела, который имеет массу и может быть подвинут под действием сил
        def.fixedRotation = true; // запрещаем телу вращаться вокруг своей оси
        Body body = world.createBody(def); // создаём в мире world объект по описанному нами определению

        CircleShape circleShape = new CircleShape(); // задаём коллайдер в форме круга
        circleShape.setRadius(Math.max(width, height) * GameSettings.SCALE / 2f); // определяем радиус круга коллайдера

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape; // устанавливаем коллайдер
        fixtureDef.density = 0.1f; // устанавливаем плотность тела
        fixtureDef.friction = 1f; // устанвливаем коэффициент трения
        fixtureDef.filter.categoryBits = cBits;
        if(cBits==GameSettings.STONE_BIT){
            fixtureDef.filter.maskBits = GameSettings.ALP_BIT;
        }

        body.createFixture(fixtureDef); // создаём fixture по описанному нами определению
        circleShape.dispose(); // так как коллайдер уже скопирован в fixutre, то circleShape может быть отчищена, чтобы не забивать оперативную память.

        body.setTransform(x * GameSettings.SCALE, y * GameSettings.SCALE, 0); // устанавливаем позицию тела по координатным осям и угол поворота
        return body;
    }

    public void dispose (){

        texture.dispose();
   }
}
