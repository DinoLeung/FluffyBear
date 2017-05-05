package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by dino on 29/4/17.
 * Car texture from https://opengameart.org/content/roguelike-modern-city-pack
 */

public class Car {

    private float initialCameraY;
    private Stage stage;

    private Texture leftTexture;
    private Texture rightTexture;

//    private Drawable leftDrawable;
//    private Drawable rightDrawable;

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
        this.testImage.setSize(MainGame.GRID_SIZE, MainGame.GRID_SIZE);
        this.stage.addActor(testImage);

        this.initialCameraY = stage.getViewport().getCamera().position.y;

        this.testImage.setX(this.stage.getViewport().getWorldWidth()/2);
        this.testImage.setY(this.stage.getViewport().getWorldHeight()/2);

    }

    public void changeDirection(){
        facingLeft = !facingLeft;
        if(facingLeft)
            testImage.setDrawable(new TextureRegionDrawable(new TextureRegion(leftTexture)));
        else
            testImage.setDrawable(new TextureRegionDrawable(new TextureRegion(rightTexture)));
//        this.testImage.setSize(MainGame.GRID_SIZE, MainGame.GRID_SIZE);
    }

    public void updateAndRender(float deltaTime){
        if (stage.getViewport().getCamera().position.y - initialCameraY >= stage.getViewport().getCamera().viewportHeight)
            //bring the car back to the original point when map refresh
            testImage.setY(testImage.getY() - stage.getHeight());
        else
            //keep moving the car with the camera
            testImage.setY(testImage.getY() + (100.0f * deltaTime));

        if (facingLeft)
            //move toward left
            testImage.setX(testImage.getX() - (200.0f * deltaTime));
        else
            //love toward right
            testImage.setX(testImage.getX() + (200.0f * deltaTime));
    }

    public void resize(int width, int height){

    }
}
