package com.mygdx.game.objects;

import com.badlogic.gdx.physics.box2d.World;

public class AlpObject extends GameObject {
    public AlpObject(int x, int y, int width, int height, String texturePath, World world) {
        super(texturePath, x, y, width, height, world  );
    }


}
