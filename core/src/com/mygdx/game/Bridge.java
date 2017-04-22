package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by Dino on 03/04/2017.
 */

public class Bridge {

    public static final int DEFAULT_SPEED = 80;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 200;
    public static final int GRID_SIZE = 32;


    private int speed;
    private int goalSpeed;
    private float imageSize;
    private boolean speedFixed;

    private int rowNum;
    private int colNum;

    private int bridgeWidth;
    private BridgeGenerator generator;

    private float initialCameraY;

    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;


    /**
     *
     * @param rowNum number of rows on screen
     * @param bridgeWidth width of the bridge, min value 0
     */
    public Bridge(int rowNum, int bridgeWidth, SpriteBatch batch){

        // for logcat output
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.rowNum = rowNum;

        //initialise imageScale
        this.resize(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());

        this.colNum = (int)(Gdx.app.getGraphics().getHeight()/imageSize) * 2;

        this.bridgeWidth = bridgeWidth;

        this.generator = new BridgeGenerator(this.bridgeWidth, this.rowNum, this.colNum, GRID_SIZE);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, this.rowNum * GRID_SIZE, (this.colNum / 2) * GRID_SIZE);
        camera.update();

        initialCameraY = camera.position.y;

        tiledMapRenderer = new OrthogonalTiledMapRenderer(this.generator.map);

        //hacky way to prevent lag on the first swap
        this.generator.swapNextMap();
    }

    public void updateAndRender(float deltaTime, SpriteBatch batch){

        if (camera.position.y - initialCameraY >= camera.viewportHeight) {
            //swap new map in
            Gdx.app.debug("updateAndRender", "SWAP");
            this.generator.swapNextMap();
            camera.position.y = initialCameraY;
        }else
            camera.position.y += 100.0f * deltaTime;

        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void resize(int width, int height){
        imageSize = width/ rowNum;
    }

    public void setSpeed(int goalSpeed){
        this.goalSpeed = goalSpeed;
    }

    public void getSpeedFixed(boolean speedFixed){
        this.speedFixed = speedFixed;
    }
}
