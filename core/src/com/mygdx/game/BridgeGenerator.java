package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by Dino on 03/04/2017.
 * Bridge texture from https://opengameart.org/content/lpc-style-wood-bridges-and-steel-flooring
 * Water texture from https://opengameart.org/content/basic-map-32x32-by-silver-iv
 */

public class BridgeGenerator {

    private int bridgeWidth;
    private int mapWidth;
    private int mapHeight;
    private int gridSize;

    private TiledMapTileLayer.Cell[] bridgeL;
    private TiledMapTileLayer.Cell[] bridgeR;

    public TiledMap map;

    private TiledMapTileLayer BridgeLayer;

    private Array<Integer> farLeft;
    private Array<Boolean> direction;

    private Array<Integer> collisionFarLeft;
    private Array<Boolean> collisionDirection;

    private TiledMapTileLayer nextBridgeLayer;

    private Random r;

    public BridgeGenerator(int bridgeWidth, int mapWidth, int mapHeight, int gridSize){

        // for logcat output
//        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        this.bridgeWidth = bridgeWidth;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.gridSize = gridSize;

        map = new TiledMap();

        //initialise a random object determining turnings
        this.r = new Random();

        //initialise 2 arrays contain parts of the bridge
        this.bridgeL = new TiledMapTileLayer.Cell[8];
        this.bridgeR = new TiledMapTileLayer.Cell[8];
        for (int i = 0; i < 8; i++){
            bridgeL[i] = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("Bridge/L" + String.valueOf(i+1) + ".png"))));
            bridgeR[i] = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("Bridge/R" + String.valueOf(i+1) + ".png"))));
        }

        //generate initial map
        generateNewMap();
        //ready the next map to swap in
        generateNextMap();
    }

    public void swapNextMap(){
        //swap new layer into map
        map.getLayers().remove(map.getLayers().getIndex("bridge"));
        map.getLayers().add(nextBridgeLayer);
        BridgeLayer = nextBridgeLayer;
        //ready the next map to swap in
        generateNextMap();
    }

    public Array<Integer> getCollisionFarLeft(){
        return collisionFarLeft;
    }
    public Array<Boolean> getCollisionDirection(){
        return collisionDirection;
    }

    private TiledMapTileLayer generateBackgroundLayer(){
        //create a empty map layer
        TiledMapTileLayer backgroundLayer = new TiledMapTileLayer(mapWidth, mapHeight, gridSize, gridSize);

        TiledMapTileLayer.Cell[] riverL = new TiledMapTileLayer.Cell[3];
        TiledMapTileLayer.Cell[] riverR = new TiledMapTileLayer.Cell[3];
        TiledMapTileLayer.Cell riverM = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("River/M.png"))));

        for(int i = 0; i < 3; i++){
            riverL[i] = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("River/L" + String.valueOf(i+1) + ".png"))));
            riverR[i] = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(new TextureRegion(new Texture("River/R" + String.valueOf(i+1) + ".png"))));
        }
        for (int h = 0; h < mapHeight; h++){
            for (int w = 0; w < mapWidth; w++){
                if (w == 0)
                    backgroundLayer.setCell(w, h, riverL[0]);
                else if (w == 1)
                    backgroundLayer.setCell(w, h, riverL[2]);
                else if (w == mapWidth - 2)
                    backgroundLayer.setCell(w, h, riverR[2]);
                else if (w == mapWidth - 1)
                    backgroundLayer.setCell(w, h, riverR[0]);
                else
                    backgroundLayer.setCell(w, h, riverM);
            }
        }

        return backgroundLayer;
    }

    //private method to generate a screen long extension on the original map
    private void generateNextMap(){

        //a copy of the far left data for the collision detection
        this.collisionFarLeft = new Array<Integer>(farLeft);
        this.collisionDirection = new Array<Boolean>(direction);

        //create a empty map layer
        nextBridgeLayer = new TiledMapTileLayer(mapWidth, mapHeight, gridSize, gridSize);
        nextBridgeLayer.setName("bridge");

        //remove first half
        direction.removeRange(0, mapHeight/2 -1);
        farLeft.removeRange(0, mapHeight/2 -1);

        //copy the second half
        int h = 0;
        for (; h < direction.size; h++)
            if(direction.get(h))
                generateLeftNext(h, farLeft.get(h));
            else
                generateRightNext(h, farLeft.get(h));


        //generate new turnings
        int lastFarLeft = farLeft.peek();
        boolean towardLeft = direction.peek();

        //generate the next one turning data
        if (towardLeft){ //going left

            //decide direction for next
            if (r.nextBoolean()) //keep going left
                //check if reach far left
                if (lastFarLeft <= 0) //turn right
                    towardLeft = false;
                else //continue on left
                    lastFarLeft -= 2;
            else //turn right next
                towardLeft = false;

        }else{ //going right

            //decide direction for next
            if (r.nextBoolean()) //keep going right
                //check if reach far right
                if (lastFarLeft + bridgeWidth + 4 >= mapWidth) //turn left
                    towardLeft = true;
                else //continue on right
                    lastFarLeft += 2;
            else //turn left
                towardLeft = true;
        }

        //draw new turnings and generate other turnings data
        for (; h < mapHeight; h++){

            farLeft.add(lastFarLeft);
            direction.add(towardLeft);

            if (towardLeft){ //going left

                //draw left
                lastFarLeft = generateLeftNext(h, lastFarLeft);

                //decide direction for next
                if (r.nextBoolean()) //keep going left
                    //check if reach far left
                    if (lastFarLeft <= 0) //turn right
                        towardLeft = false;
                    else //continue on left
                        lastFarLeft -= 2;
                else //turn right next
                    towardLeft = false;

            }else{ //going right

                //draw right
                lastFarLeft = generateRightNext(h, lastFarLeft);

                //decide direction for next
                if (r.nextBoolean()) //keep going right
                    //check if reach far right
                    if (lastFarLeft + bridgeWidth + 4 >= mapWidth) //turn left
                        towardLeft = true;
                    else //continue on right
                        lastFarLeft += 2;
                else //turn left
                    towardLeft = true;
            }
        }
    }

    //private method to generate a new map which is 2 times longer then the screen
    private void generateNewMap(){

        //create a empty map layer
        BridgeLayer = new TiledMapTileLayer(mapWidth, mapHeight, gridSize, gridSize);
        BridgeLayer.setName("bridge");

        farLeft = new Array<Integer>();
        direction = new Array<Boolean>();

        int lastFarLeft = 0;
        boolean towardLeft = false;

        for (int h = 0; h < mapHeight; h++){

            farLeft.add(lastFarLeft);
            direction.add(towardLeft);

            if (towardLeft){ //going left

                //draw left
                lastFarLeft = generateLeft(h, lastFarLeft);

                //decide direction for next
//                if (r.nextBoolean()) //keep going left
                    //check if reach far left
                    if (lastFarLeft <= 0) //turn right
                        towardLeft = false;
                    else //continue on left
                        lastFarLeft -= 2;
//                else //turn right next
//                    towardLeft = false;

            }else{ //going right

                //draw right
                lastFarLeft = generateRight(h, lastFarLeft);

                //decide direction for next
//                if (r.nextBoolean()) //keep going right
                    //check if reach far right
                    if (lastFarLeft + bridgeWidth + 4 >= mapWidth) //turn left
                        towardLeft = true;
                    else //continue on right
                        lastFarLeft += 2;
//                else //turn left
//                    towardLeft = true;
            }
        }
        //add background payer into map
        map.getLayers().add(generateBackgroundLayer());
        //add bridge layer into map
        map.getLayers().add(BridgeLayer);
//        map.getLayers().add(CollisionLayer);
    }

    //private method drawing a row of bridge on the map toward left on original layer
    private int generateLeft(int h, int lastFarLeft){
        int temp = 0;
        BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[0]);
        BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[1]);
        for (int i = 0; i<bridgeWidth; i++)
            BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[2]);
        BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[3]);
        BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[4]);

        return lastFarLeft;
    }

    //private method drawing a row of bridge on the map toward right on original layer
    private int generateRight(int h, int lastFarLeft){
        int temp = 0;
        BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[0]);
        BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[1]);
        for (int i = 0; i<bridgeWidth; i++)
            BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[2]);
        BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[3]);
        BridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[4]);

        return lastFarLeft;
    }

    //private method drawing a row of bridge on the map toward left on next layer
    private int generateLeftNext(int h, int lastFarLeft){
        int temp = 0;
        nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[0]);
        nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[1]);
        for (int i = 0; i<bridgeWidth; i++)
            nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[2]);
        nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[3]);
        nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeL[4]);

        return lastFarLeft;
    }

    //private method drawing a row of bridge on the map toward right on next layer
    private int generateRightNext(int h, int lastFarLeft){
        int temp = 0;
        nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[0]);
        nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[1]);
        for (int i = 0; i<bridgeWidth; i++)
            nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[2]);
        nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[3]);
        nextBridgeLayer.setCell(lastFarLeft + temp++, h, bridgeR[4]);

        return lastFarLeft;
    }
}
