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

    /**
     *
     * @param rowNum number of row on map
     * @param colNum number of column on map
     * @param bridgeWidth width of the bridge, min value 0
     * @param stage
     */
    public Bridge(int rowNum, int colNum, int bridgeWidth, Stage stage){

        // for logcat output
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.stage = stage;

        this.rowNum = rowNum;
        this.colNum = colNum;

        this.bridgeWidth = bridgeWidth;

        this.generator = new BridgeGenerator(this.bridgeWidth, this.rowNum, this.colNum, MyGdxGame.GRID_SIZE);

        initialCameraY = stage.getViewport().getCamera().position.y;

        tiledMapRenderer = new OrthogonalTiledMapRenderer(this.generator.map);

        //hacky way to prevent lag on the first swap
//        this.generator.swapNextMap();
    }

    public void updateAndRender(float deltaTime){

        if (stage.getViewport().getCamera().position.y - initialCameraY >= stage.getViewport().getCamera().viewportHeight) {
            //swap new map in
            Gdx.app.debug("updateAndRender", "SWAP");
            this.generator.swapNextMap();
            //bring the camera back to the original point
            stage.getViewport().getCamera().position.y = initialCameraY;
        }else
            //keep moving the camera
            stage.getViewport().getCamera().position.y += 100.0f * deltaTime;

        stage.getViewport().getCamera().update();
        tiledMapRenderer.setView((OrthographicCamera)stage.getViewport().getCamera());
        tiledMapRenderer.render();
    }

    public void resize(int width, int height){

    }

}
