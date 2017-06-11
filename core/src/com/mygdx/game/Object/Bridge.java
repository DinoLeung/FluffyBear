package com.mygdx.game.Object;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.Util.BridgeGenerator;
import com.mygdx.game.MainGame;

/**
 * Created by Dino on 03/04/2017.
 */

public class Bridge {

    final Stage stage;

    private int rowNum;
    private int colNum;

    private int bridgeWidth;
    private BridgeGenerator generator;

    private float initialCameraY;

    TiledMapRenderer tiledMapRenderer;

    private float farLeft;
    private int previousIndex = 0;
    private float pixelCount = 0;


    /**
     *
     * @param rowNum number of row on map
     * @param colNum number of column on map
     * @param bridgeWidth width of the bridge, min value 0
     * @param stage
     */
    public Bridge(int rowNum, int colNum, int bridgeWidth, final Stage stage){

        // for logcat output
//        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.stage = stage;

        this.rowNum = rowNum;
        this.colNum = colNum;

        this.bridgeWidth = bridgeWidth;

        this.generator = new BridgeGenerator(this.bridgeWidth, this.rowNum, this.colNum, MainGame.GRID_SIZE);

        this.initialCameraY = stage.getViewport().getCamera().position.y;

        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(this.generator.map);

    }

    public float getFarLeft(){
        return this.farLeft;
    }

    private void updateFarLeft(float deltaTime){

        int index = (int)this.stage.getViewport().getCamera().position.y / MainGame.GRID_SIZE;
        index += 1;

        if (index != this.previousIndex){
            this.previousIndex = index;
            this.pixelCount = 0;
        }

        this.pixelCount += deltaTime;

        int currentFarLeft = this.generator.getCollisionFarLeft().get(index) * MainGame.GRID_SIZE;
        boolean currentTowardLeft = this.generator.getCollisionDirection().get(index);

        int previous = this.generator.getCollisionFarLeft().get(index - 1);

        if (currentTowardLeft) {
            currentFarLeft += MainGame.GRID_SIZE * 2;
            this.farLeft = currentFarLeft - this.pixelCount;
        }
        else
            this.farLeft = currentFarLeft + this.pixelCount;
    }

    public void updateAndRender(float deltaTime){

        if (this.stage.getViewport().getCamera().position.y - initialCameraY >=
                this.stage.getViewport().getCamera().viewportHeight) {
            //swap new map in
//            Gdx.app.debug("updateAndRender", "SWAP");
            this.generator.swapNextMap();
            //bring the camera back to the original point
            this.stage.getViewport().getCamera().position.y = initialCameraY;
        }else
            //keep moving the camera
            this.stage.getViewport().getCamera().position.y += 100.0f * deltaTime;

        //the bridge runs 2 grids horizontally each vertical grid
        this.updateFarLeft(200.0f * deltaTime);

//        Gdx.app.log("LOG CAMERA Y", String.valueOf(stage.getViewport().getCamera().position.y));

        this.stage.getViewport().getCamera().update();
        this.tiledMapRenderer.setView((OrthographicCamera)this.stage.getViewport().getCamera());
        this.tiledMapRenderer.render();
    }

}
