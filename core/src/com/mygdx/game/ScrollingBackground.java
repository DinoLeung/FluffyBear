package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Dino on 31/03/2017.
 * Water texture from https://opengameart.org/content/lpc-animated-water-and-fire
 */

public class ScrollingBackground {

    public static final int DEFAULT_SPEED = 80;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 200;


    private Texture image;
    private float y1, y2;
    private int speed;
    private int goalSpeed;
    private float imageScale;
    private boolean speedFixed;

    public ScrollingBackground(){
        image = new Texture("River.png");

        //initialise imageScale
        this.resize(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());

        y1 = 0;
        y2 = image.getHeight() * imageScale;

        speed = 0;
        goalSpeed = DEFAULT_SPEED;

        speedFixed = true;
    }

    public void updateAndRender(float deltaTime, SpriteBatch batch){
        //speed adjustment
        if (speed < goalSpeed){
            speed += GOAL_REACH_ACCELERATION * deltaTime;
            if (speed > goalSpeed)
                speed = goalSpeed;
        }else if (speed > goalSpeed){
            speed -= GOAL_REACH_ACCELERATION * deltaTime;
            if (speed < goalSpeed)
                speed = goalSpeed;
        }

        if (!speedFixed)
            speed += ACCELERATION * deltaTime;

        y1 -= speed * deltaTime;
        y2 -= speed * deltaTime;

        //if image reach bottom of the screen and is not visible, put it back on top
        if (y1 + image.getHeight() * imageScale <= 0)
            y1 = y2 + image.getHeight() * imageScale;
        if (y2 + image.getHeight() * imageScale <= 0)
            y2 = y1 + image.getHeight() * imageScale;

        //render
        batch.draw(image, 0, y1, Gdx.graphics.getWidth(), image.getHeight() * imageScale);
        batch.draw(image, 0, y2, Gdx.graphics.getWidth(), image.getHeight() * imageScale);
    }

    public void resize(int width, int height){
        imageScale = width / image.getWidth();
    }

    public void setSpeed(int goalSpeed){
        this.goalSpeed = goalSpeed;
    }

    public void getSpeedFixed(boolean speedFixed){
        this.speedFixed = speedFixed;
    }
}
