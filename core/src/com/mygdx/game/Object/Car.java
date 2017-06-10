package com.mygdx.game.Object;

import com.badlogic.gdx.Application;
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
import com.mygdx.game.MainGame;

/**
 * Created by dino on 29/4/17.
 * Car texture from https://opengameart.org/content/roguelike-modern-city-pack
 */

public class Car {

    private float initialCameraY;
    final Stage stage;

    private Texture leftTexture;
    private Texture rightTexture;

    private Rectangle playerDeltaRectangle;

    public boolean facingLeft = false;
    private Image carImage = null;

    public Car(final Stage stage) {

        // for logcat output
//        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.stage = stage;

        this.leftTexture = new Texture("Car/L.png");
        this.rightTexture = new Texture("Car/R.png");
        this.carImage = new Image(rightTexture);
        this.carImage.setSize(MainGame.GRID_SIZE, MainGame.GRID_SIZE);
        this.stage.addActor(this.carImage);

        this.initialCameraY = stage.getViewport().getCamera().position.y;

        this.carImage.setX(this.stage.getViewport().getWorldWidth()/2);
        this.carImage.setY(this.stage.getViewport().getWorldHeight()/2);

        this.playerDeltaRectangle = new Rectangle(this.carImage.getX(),
                this.carImage.getY(),
                this.carImage.getImageWidth(),
                this.carImage.getImageHeight());
    }

    public void changeDirection(){
        facingLeft = !facingLeft;
        if(facingLeft)
            this.carImage.setDrawable(new TextureRegionDrawable(new TextureRegion(leftTexture)));
        else
            this.carImage.setDrawable(new TextureRegionDrawable(new TextureRegion(rightTexture)));
    }

    public void updateAndRender(float deltaTime){
        if (this.stage.getViewport().getCamera().position.y - initialCameraY >=
                this.stage.getViewport().getCamera().viewportHeight)
            //bring the car back to the original point when map refresh
            this.carImage.setY(this.carImage.getY() - this.stage.getHeight());
        else
            //keep moving the car with the camera
            this.carImage.setY(this.carImage.getY() + (100.0f * deltaTime));

        if (facingLeft)
            //move toward left
            this.carImage.setX(this.carImage.getX() - (200.0f * deltaTime));
        else
            //love toward right
            this.carImage.setX(this.carImage.getX() + (200.0f * deltaTime));

        this.playerDeltaRectangle.setPosition(this.carImage.getX(), this.carImage.getY());

//        Gdx.app.log("LOG CAR X", String.valueOf(this.playerDeltaRectangle.getX()));
//        Gdx.app.log("LOG CAR Y", String.valueOf(this.playerDeltaRectangle.getY()));
    }

    public Rectangle getPlayerDeltaRectangle(){
        return this.playerDeltaRectangle;
    }

}
