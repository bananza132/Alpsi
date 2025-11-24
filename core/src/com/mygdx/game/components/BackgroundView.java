package com.mygdx.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameSettings;

public class BackgroundView extends View {
    private final Texture texture;

    public BackgroundView(String pathToTexture) {
        super(0, 0);
        texture = new Texture(pathToTexture);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

}
