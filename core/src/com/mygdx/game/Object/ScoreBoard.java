package com.mygdx.game.Object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by dino on 10/6/17.
 */

public class ScoreBoard extends Label {

    private float initialCameraY;
    final  Stage stage;
    private BitmapFont font;

    /**
     *
     * @param stage
     * @param score
     * @param font
     */
    public ScoreBoard(final Stage stage, float score, BitmapFont font) {
        super(String.valueOf((int)score), new LabelStyle(font, Color.WHITE));
        this.stage = stage;
        this.font = font;

        stage.addActor(this);

        this.initialCameraY = stage.getViewport().getCamera().position.y;

        this.setX(this.stage.getViewport().getWorldWidth() - this.getWidth());
        this.setY(this.stage.getViewport().getWorldHeight() - this.getHeight());
    }

    public void setScore(float score){
        super.setText(String.valueOf((int)score));
    }

    public void updateAndRender(float deltaTime){

        //move to the left when the score has multiple digits
        this.setX(this.stage.getViewport().getWorldWidth() - this.getWidth() * (this.getText().length));

        if (this.stage.getViewport().getCamera().position.y - initialCameraY >=
                this.stage.getViewport().getCamera().viewportHeight)
            //bring the label back to the original point when map refresh
            this.setY(this.getY() - this.stage.getHeight());
        else
            //keep moving the label with the camera
            this.setY(this.getY() + (100.0f * deltaTime));
    }
}
