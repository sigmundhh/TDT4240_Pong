package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.MyGdxGame;


public abstract class GameSprite {

    private static final int SENSITIVITY = 1000;
    private Vector3 position;
    private Vector3 velocity;
    private Texture texture;
    private boolean directionRight = true;

    public GameSprite(int x, int y, String filename){
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture(filename);
    }

    public void randomStep(double dt){
        this.getPosition().x += 50*dt*(Math.random()-0.5);
        this.getPosition().y += 50*dt*(Math.random()-0.5);
    }

    public void step(double dt){
        this.getPosition().x += getVelocity().x*dt;
        this.getPosition().y += getVelocity().y*dt;
        this.update(dt);
    }

    //Should be implemented in PlayerPaddle instead, but it caused some problems
    public void moveUp(float dt){
        this.position.y = this.position.y + SENSITIVITY*dt;
    }
    public void moveDown(float dt){
        this.position.y = this.position.y - SENSITIVITY*dt;
    }

    public void setPosY(int y){
        this.position.y = y;
    }

    public void stepBack(double dt){
        this.getPosition().x -= getVelocity().x*dt;
        this.getPosition().y -= getVelocity().y*dt;
        this.update(dt);
    }

    public void moveTowards(float posX, float posY){
        System.out.println((posX - this.getPosition().x) + ", " + (posY - this.getPosition().y));
        this.setVelocity(new Vector3(posX - this.getPosition().x, MyGdxGame.HEIGHT - posY - this.getPosition().y, 0));
    }

    public void edgeBounceCheckRoutine(){
        //Right edge
        if(getPosition().x + texture.getWidth() > MyGdxGame.WIDTH) {
            getPosition().x = MyGdxGame.WIDTH - texture.getWidth();
            bounceHorizontally();
        }
        //Left edge
        if(getPosition().x < 0) {
            getPosition().x = 0;
            bounceHorizontally();
        }
        //Top edge
        if(getPosition().y + texture.getHeight() > MyGdxGame.HEIGHT) {
            getPosition().y = MyGdxGame.HEIGHT - texture.getHeight();
            bounceVertically();
        }
        //Bottom edge
        if(getPosition().y < 0) {
            getPosition().y = 0;
            bounceVertically();
        }
    }

    public void bounceHorizontally(){
        getVelocity().x = -getVelocity().x;
        directionRight ^= true;

    }
    public void bounceVertically(){
        getVelocity().y = -getVelocity().y;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public Texture getTexture() {
        return texture;
    }

    public boolean getDirection() {
        return directionRight;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public abstract void update(double dt);
}
