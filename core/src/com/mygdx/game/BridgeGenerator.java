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
 */

public class BridgeGenerator {

    private int bridgeWidth;
    private int mapWidth;
    private int mapHeight;
    private int gridSize;

    private TiledMapTileLayer.Cell[] bridgeL;
    private TiledMapTileLayer.Cell[] bridgeR;

    TiledMap map;

    TiledMapTileLayer layer;

    Array<Integer> farLeft;
    Array<Boolean> direction;

    TiledMapTileLayer nextLayer;

    Random r;

    public BridgeGenerator(int bridgeWidth, int mapWidth, int mapHeight, int gridSize){

        // for logcat output
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

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
        map.getLayers().remove(0);
        map.getLayers().add(nextLayer);
        layer = nextLayer;
        //ready the next map to swap in
        generateNextMap();
    }

    //private method to generate a screen long extension on the original map
    private void generateNextMap(){

        //create a empty map layer
        nextLayer = new TiledMapTileLayer(mapWidth, mapHeight, gridSize, gridSize);

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
        layer = new TiledMapTileLayer(mapWidth, mapHeight, gridSize, gridSize);

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
                lastFarLeft = generateRight(h, lastFarLeft);

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
        //add layer into map
        map.getLayers().add(layer);
    }

    //private method drawing a row of bridge on the map toward left on original layer
    private int generateLeft(int h, int lastFarLeft){
        int temp = 0;
        layer.setCell(lastFarLeft + temp++, h, bridgeL[0]);
        layer.setCell(lastFarLeft + temp++, h, bridgeL[1]);
        for (int i = 0; i<bridgeWidth; i++)
            layer.setCell(lastFarLeft + temp++, h, bridgeL[2]);
        layer.setCell(lastFarLeft + temp++, h, bridgeL[3]);
        layer.setCell(lastFarLeft + temp++, h, bridgeL[4]);

        return lastFarLeft;
    }

    //private method drawing a row of bridge on the map toward right on original layer
    private int generateRight(int h, int lastFarLeft){
        int temp = 0;
        layer.setCell(lastFarLeft + temp++, h, bridgeR[0]);
        layer.setCell(lastFarLeft + temp++, h, bridgeR[1]);
        for (int i = 0; i<bridgeWidth; i++)
            layer.setCell(lastFarLeft + temp++, h, bridgeR[2]);
        layer.setCell(lastFarLeft + temp++, h, bridgeR[3]);
        layer.setCell(lastFarLeft + temp++, h, bridgeR[4]);

        return lastFarLeft;
    }

    //private method drawing a row of bridge on the map toward left on next layer
    private int generateLeftNext(int h, int lastFarLeft){
        int temp = 0;
        nextLayer.setCell(lastFarLeft + temp++, h, bridgeL[0]);
        nextLayer.setCell(lastFarLeft + temp++, h, bridgeL[1]);
        for (int i = 0; i<bridgeWidth; i++)
            nextLayer.setCell(lastFarLeft + temp++, h, bridgeL[2]);
        nextLayer.setCell(lastFarLeft + temp++, h, bridgeL[3]);
        nextLayer.setCell(lastFarLeft + temp++, h, bridgeL[4]);

        return lastFarLeft;
    }

    //private method drawing a row of bridge on the map toward right on next layer
    private int generateRightNext(int h, int lastFarLeft){
        int temp = 0;
        nextLayer.setCell(lastFarLeft + temp++, h, bridgeR[0]);
        nextLayer.setCell(lastFarLeft + temp++, h, bridgeR[1]);
        for (int i = 0; i<bridgeWidth; i++)
            nextLayer.setCell(lastFarLeft + temp++, h, bridgeR[2]);
        nextLayer.setCell(lastFarLeft + temp++, h, bridgeR[3]);
        nextLayer.setCell(lastFarLeft + temp++, h, bridgeR[4]);

        return lastFarLeft;
    }
}
