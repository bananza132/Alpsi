package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.GameSettings;

import java.util.Random;

public class MountainObject {
    private int texture1Y, texture2Y;
    private final int height, width;

    private final Texture texture;
    private TextureRegion[] textureRegions;
    private TextureRegion texture1, texture2;

    private final int speed;
    private boolean isMoving;
    private int moveDistance;
    private final int maxMoveDistance;

    public MountainObject(int x, int y, int width, int height, String texturePath) {
        texture1Y = 0;
        texture2Y = GameSettings.SCREEN_HEIGHT;
        this.height = height;
        this.width = width;
        texture = new Texture(texturePath);
        textureRegions = new TextureRegion[3];
        for (int i = 0; i < 3; i++) {
            TextureRegion textureRegion = new TextureRegion(texture, i * 720, 0, 720, 1280);
            textureRegions[i] = textureRegion;
        }
        int randomIndex = new Random().nextInt(3);
        texture1 = textureRegions[randomIndex];
        randomIndex = new Random().nextInt(3);
        texture2 = textureRegions[randomIndex];
        speed = 5;
        isMoving = false;
        moveDistance = 0;
        maxMoveDistance = 100;
    }

    public void move() {
        if (!isMoving) return;

        texture1Y -= speed;
        texture2Y -= speed;
        moveDistance += speed;

        if (moveDistance >= maxMoveDistance) {
            stopMoving();
        }

        if (texture1Y <= -GameSettings.SCREEN_HEIGHT) {
            texture1Y = GameSettings.SCREEN_HEIGHT;
            int randomIndex = new Random().nextInt(3);
            texture1 = textureRegions[randomIndex];
        }

        if (texture2Y <= -GameSettings.SCREEN_HEIGHT) {
            texture2Y = GameSettings.SCREEN_HEIGHT;
            int randomIndex = new Random().nextInt(3);
            texture2 = textureRegions[randomIndex];
        }
    }

    public void startMoving() {
        isMoving = true;
        moveDistance = 0;
    }

    public void stopMoving() {
        isMoving = false;
        moveDistance = 0;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture1, 0, texture1Y, width, height);
        batch.draw(texture2, 0, texture2Y, width, height);
    }

    public void dispose() {
        texture.dispose();
    }
}