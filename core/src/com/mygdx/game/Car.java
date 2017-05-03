package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by dino on 29/4/17.
 */

public class Car {

    private float initialCameraY;
    private Stage stage;

    private Texture leftTexture;
    private Texture rightTexture;

    private Sprite playerSprite;
    Vector2 playerDelta;
    Rectangle playerDeltaRectangle;

    private boolean facingLeft = false;
    private Image testImage = null;
    public Car(Stage stage) {

        this.stage = stage;

        this.leftTexture = new Texture("Car/L.png");
        this.rightTexture = new Texture("Car/R.png");
        this.testImage = new Image(rightTexture);
        testImage.setSize(MainGame.GRID_SIZE, MainGame.GRID_SIZE);
        this.stage.addActor(testImage);

        this.initialCameraY = stage.getViewport().getCamera().position.y;

        this.testImage.setX(this.stage.getViewport().getWorldWidth()/2);
        this.testImage.setY(this.stage.getViewport().getWorldHeight()/2);

    }

    public void updateAndRender(float deltaTime){

        if (stage.getViewport().getCamera().position.y - initialCameraY >= stage.getViewport().getCamera().viewportHeight)
            //bring the car back to the original point when map refresh
            testImage.setY(testImage.getY() - stage.getHeight());
        else
            //keep moving the car with the camera
            testImage.setY(testImage.getY() + (100.0f * deltaTime));

    }

    public void resize(int width, int height){

    }
}
