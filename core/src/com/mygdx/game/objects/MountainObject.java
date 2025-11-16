package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;

public class MountainObject  {

    Texture texture;
    int x,y;
    int height,width;
    TextureRegion textureRegion;
   public MountainObject(int x, int y, int width, int height, String texturePath) {
     texture=new Texture(texturePath);
     this.x=x;
       this.y=y;
       this.height=height;
       this.width=width;
       textureRegion=new TextureRegion(texture,0,0,720,1280);
    }


    public void move() {

    }

    public void draw(SpriteBatch batch) {

      batch.draw(textureRegion,x,y,width,height );
    }
    public void dispose() {
        texture.dispose();
    }

}