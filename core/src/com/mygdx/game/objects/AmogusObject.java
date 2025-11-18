package com.mygdx.game.objects;

import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

public class AmogusObject extends GameObject {
    public AmogusObject (int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, GameSettings.AMOGUS_OBJECT_BIT,world);
    }

}
