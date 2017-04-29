package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by dino on 29/4/17.
 */

public class Car {

    private Stage stage;
    private Sprite leftSprite;
    private Sprite rightSprite;

    public Car(Stage stage) {
        this.stage = stage;

        leftSprite = new Sprite(new Texture("Car/L.png"));
        rightSprite = new Sprite(new Texture("Car/R.png"));

    }

    public void updateAndRender(float deltaTime){

    }

    public void resize(int width, int height){

    }
}
