package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Dino on 03/04/2017.
 */

public class Bridge {

    private Stage stage;

    private int rowNum;
    private int colNum;

    private int bridgeWidth;
    private BridgeGenerator generator;

    private float initialCameraY;

    TiledMapRenderer tiledMapRenderer;
//    OrthographicCamera camera;


    /**
     *
     * @param rowNum number of rows on screen
     * @param bridgeWidth width of the bridge, min value 0
     */
    public Bridge(int rowNum, int colNum, int bridgeWidth, Stage stage){

        // for logcat output
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.stage = stage;

        this.rowNum = rowNum;
        this.colNum = colNum;

        //initialise imageScale
//        this.resize(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());

        this.bridgeWidth = bridgeWidth;

        this.generator = new BridgeGenerator(this.bridgeWidth, this.rowNum, this.colNum, MyGdxGame.GRID_SIZE);

//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, this.rowNum * MyGdxGame.GRID_SIZE, (this.colNum / 2) * MyGdxGame.GRID_SIZE);
//        camera.update();

        initialCameraY = stage.getViewport().getCamera().position.y;

        tiledMapRenderer = new OrthogonalTiledMapRenderer(this.generator.map);

        //hacky way to prevent lag on the first swap
        this.generator.swapNextMap();
    }

    public void updateAndRender(float deltaTime){

        if (stage.getViewport().getCamera().position.y - initialCameraY >= stage.getViewport().getCamera().viewportHeight) {
            //swap new map in
            Gdx.app.debug("updateAndRender", "SWAP");
            this.generator.swapNextMap();
            stage.getViewport().getCamera().position.y = initialCameraY;
        }else
            stage.getViewport().getCamera().position.y += 100.0f * deltaTime;

        stage.getViewport().getCamera().update();
        tiledMapRenderer.setView((OrthographicCamera)stage.getViewport().getCamera());
        tiledMapRenderer.render();
    }

    public void resize(int width, int height){

    }

//    public void setSpeed(int goalSpeed){
//        this.goalSpeed = goalSpeed;
//    }
//
//    public void getSpeedFixed(boolean speedFixed){
//        this.speedFixed = speedFixed;
//    }
}
