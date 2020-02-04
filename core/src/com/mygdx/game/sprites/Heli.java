package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.awt.SystemTray;

public class Heli extends GameSprite {

    Array<Texture> textures ;
    private final static double animMaxTime = 0.1;
    private double timeSinceLastTextureChange  = 0;
    private int textureIndex = 0;

    public Heli(int x, int y){
        super(x,y, "attackhelicopter.PNG");
        textures = new Array<Texture>();
        textures.add(new Texture("heli4.png"));
        textures.add(new Texture("heli3.png"));
        textures.add(new Texture("heli2.png"));
        textures.add(new Texture("heli1.png"));
    }


    public void update(double dt) {
        //Implement animation, use dt.
        timeSinceLastTextureChange += dt;
        if(timeSinceLastTextureChange > animMaxTime){
            this.setTexture(textures.get(textureIndex));
            textureIndex++;
            if(textureIndex >= 4){
                textureIndex = 0;
            }
            timeSinceLastTextureChange = 0;
        }
    }
}
