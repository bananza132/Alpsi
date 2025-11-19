package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.GameSettings;

import java.util.Random;

public class MountainObject  {

    Texture texture;
    int texture1Y,texture2Y ;
    int height,width;
    TextureRegion[] textureRegions;
    TextureRegion texture1,texture2;
    int speed=5;
   public MountainObject(int x, int y, int width, int height, String texturePath) {
     texture=new Texture(texturePath);
     texture1Y=0;
     texture2Y= GameSettings.SCREEN_WIDTH;
       this.height=height;
       this.width=width;
       textureRegions=new TextureRegion[3];
       for (int i = 0; i < 3; i++) {
           TextureRegion textureRegion=new TextureRegion(texture,i*720,0,720,1280);
           textureRegions[i]=textureRegion;
       }
       int randomIndex=new Random().nextInt(3);
       texture1=textureRegions[randomIndex];
       randomIndex=new Random().nextInt(3);
       texture2=textureRegions[randomIndex];

    }


    public void move() {
texture1Y-=speed;
texture2Y-=speed;
        if (texture1Y <= -GameSettings.SCREEN_WIDTH) {
            texture1Y= GameSettings.SCREEN_WIDTH;
            int randomIndex=new Random().nextInt(3);
            texture1=textureRegions[randomIndex];
        }
        if (texture2Y <= -GameSettings.SCREEN_WIDTH) {
            texture2Y = GameSettings.SCREEN_WIDTH;
          int  randomIndex=new Random().nextInt(3);
            texture2=textureRegions[randomIndex];
        }
    }

    public void draw(SpriteBatch batch) {


      batch.draw(texture1,0,texture1Y,width,height );

        batch.draw(texture2,0,texture2Y,width,height );
    }
    public void dispose() {
        texture.dispose();
    }

}