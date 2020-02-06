package com.mygdx.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.sprites.Ball;
import com.mygdx.game.sprites.ComputerPaddle;
import com.mygdx.game.sprites.GameSprite;
import com.mygdx.game.sprites.PlayerPaddle;

public class PlayState extends State {

    private Array<GameSprite> sprites;
    private Texture backGround;
    private PlayerPaddle player1Paddle;
    private PlayerPaddle player2Paddle;
    private Ball ball;
    private int player1Points = 0, player2Points = 0;
    private String winnerString = " ";

    BitmapFont font = new BitmapFont();

    public PlayState(GameStateManager gsm) {
        super(gsm);
        sprites = new Array<GameSprite>();
        player1Paddle = new PlayerPaddle(650,50);
        sprites.add(player1Paddle);
        player2Paddle = new PlayerPaddle(50,50);
        sprites.add(player2Paddle);
        ball = new Ball(550, 150);
        sprites.add(ball);
        setRandomSpeedToBall();

        backGround = new Texture("Gray.png");
    }

    private void setRandomSpeedToBall(){
        //Set speed to ball
        int randomSign1 = Math.random()>0.5 ? 1 : -1;
        int randomSign2 = Math.random()>0.5 ? 1 : -1;
        ball.setVelocity(new Vector3(randomSign1*400 - 200,
                randomSign2*400 - 200, 0));
    }

    protected void checkForPoints(){
        if(ball.getPosition().x < 10){
            ball.setPosition(new Vector3(250, 150, 0));
            player1Points++;
            setRandomSpeedToBall();
        }
        if(ball.getPosition().x > 700){
            ball.setPosition(new Vector3(250, 150, 0));
            player2Points++;
            setRandomSpeedToBall();
        }
    }

    protected void checkWinner(){
        if(player1Points >= 5){
            ball.setVelocity(new Vector3(0,0,0));
            winnerString = "player1 has Won!";
        }
        if(player2Points >= 5){
            ball.setVelocity(new Vector3(0,0,0));
            winnerString = "player2 has Won!";
        }
    }


    protected void handleInput(float dt) {
        //Up-button
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player1Paddle.moveUp(dt);
        }
        //Down-button
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            player1Paddle.moveDown(dt);
        }

        //W-button
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            player2Paddle.moveUp(dt);
        }
        //S-button
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            player2Paddle.moveDown(dt);
        }
        //Escape to pause
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gsm.push(new PauseState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        for(GameSprite sprite : sprites){
            sprite.step(dt);
            sprite.edgeBounceCheckRoutine();
        }
        this.handleCollision();
        checkForPoints();
        checkWinner();
        this.handleInput(dt);
    }

    public void handleCollision(){
        //Check collision for both paddles
        collisionHandler(ball, player1Paddle);
        collisionHandler(ball, player2Paddle);
    }

    public void collisionHandler(GameSprite sprite1, GameSprite sprite2){
            if (!(areCollidingVertically(sprite1, sprite2) && areCollidingHorizontally(sprite1, sprite2))){
                return;
            }
            System.out.println("Before stepBack!");
            while (areCollidingVertically(sprite1, sprite2) && areCollidingHorizontally(sprite1, sprite2)){
                sprite1.stepBack(0.005);
            }
            System.out.println("Done with stepBack!");
            if (areCollidingVertically(sprite1, sprite2)){
                horizontalBallBounce(sprite1, sprite2);
            }
            if (areCollidingHorizontally(sprite1, sprite2)){
                verticalBallBounce(sprite1, sprite2);
            }
    }

    public boolean areCollidingVertically(GameSprite sprite1, GameSprite sprite2){
        double h1 = sprite1.getTexture().getHeight();
        double w1 = sprite1.getTexture().getWidth();
        double h2 = sprite2.getTexture().getHeight();
        double w2 = sprite2.getTexture().getWidth();

        double x1 = sprite1.getPosition().x;
        double y1 = sprite1.getPosition().y;
        double x2 = sprite2.getPosition().x;
        double y2 = sprite2.getPosition().y;

        return y2-y1<h1 && y1-y2<h2;
    }
    public boolean areCollidingHorizontally(GameSprite sprite1, GameSprite sprite2){
        double h1 = sprite1.getTexture().getHeight();
        double w1 = sprite1.getTexture().getWidth();
        double h2 = sprite2.getTexture().getHeight();
        double w2 = sprite2.getTexture().getWidth();

        double x1 = sprite1.getPosition().x;
        double y1 = sprite1.getPosition().y;
        double x2 = sprite2.getPosition().x;
        double y2 = sprite2.getPosition().y;

        return x2-x1 < w1 && x1-x2 < w2;
    }

    public void horizontalBallBounce(GameSprite ball, GameSprite sprite2){
        //Change ball velocity direction
        System.out.println("HorizontalBallBounce!");
        ball.setVelocity(new Vector3(-ball.getVelocity().x, ball.getVelocity().y, ball.getVelocity().z));
    }
    public void verticalBallBounce(GameSprite ball, GameSprite sprite2){
        //Change ball velocity direction
        System.out.println("VerticalBallBounce!");
        ball.setVelocity(new Vector3(ball.getVelocity().x, -ball.getVelocity().y, ball.getVelocity().z));
    }


    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(backGround, cam.position.x - (cam.viewportWidth/2), 0);

        for(GameSprite sprite : sprites){
            sb.draw(sprite.getTexture(), sprite.getPosition().x, sprite.getPosition().y);
            font.draw(sb, Math.round(sprite.getPosition().x) + ", " + Math.round(sprite.getPosition().y),
                    0, MyGdxGame.HEIGHT-sprites.indexOf(sprite, true)*15);
        }
        font.draw(sb, "Player 1 points: " + player1Points + "       Player 2 points: " + player2Points +
                        "        " + winnerString,
                0, 15);
        font.draw(sb, "Press ESC to pause",
                MyGdxGame.WIDTH-130, 15);
        sb.end();
    }
}
