package com.mygdx.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.GameSprite;

public class PauseState extends State {
    private Texture backGround = new Texture("Gray.png");
    BitmapFont font = new BitmapFont();

    public PauseState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    protected void handleInput(float dt) {
        //R-button
        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            gsm.pop();
        }
    }

    @Override
    public void update(float dt) {
        this.handleInput(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(backGround, cam.position.x - (cam.viewportWidth/2), 0);
        font.draw(sb, "PAUSED", 0, 55);
        font.draw(sb, "PRESS R TO GO BACK TO GAME", 0, 35);
        sb.end();

    }
}
