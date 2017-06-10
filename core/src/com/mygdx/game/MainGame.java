package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Point;

public class MainGame implements Screen{
    public static final int DEFAULT_SPEED = 80;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 200;
    public static final int GRID_SIZE = 32;

    final GameLauncher launcher;

    private int speed;
    private int goalSpeed;
    private boolean speedFixed;

    Bridge bridge;
    Car car;

    Stage stage;
    Viewport viewport;
    OrthographicCamera camera;
    private int rowNum = 12;
    private int colNum;
    private int bridgeWidth = 2;
    private float imageSize;

    private boolean gameState = true;

    public MainGame(final GameLauncher launcher){
        this.launcher = launcher;
        this.create();
    }

    public void create () {
        // for logcat output
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        imageSize = Gdx.app.getGraphics().getWidth() / rowNum;
        colNum = (int)(Gdx.app.getGraphics().getHeight() / imageSize) * 2;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, this.rowNum * GRID_SIZE, (this.colNum / 2) * GRID_SIZE);
        stage = new Stage();

        stage.getViewport().setScreenSize(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        stage.getViewport().setWorldSize(this.rowNum * GRID_SIZE, (this.colNum / 2) * GRID_SIZE);
        stage.getViewport().setCamera(camera);

        bridge = new Bridge(rowNum, colNum, bridgeWidth, stage);
        car = new Car(stage);
    }

    @Override
    public void render (float delta) {
        if (this.gameState){
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //Allows transparent sprites/tiles
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            this.stage.act(delta);

            //change the car direction when screen is touched
            if (Gdx.input.justTouched())
                this.car.changeDirection();

            this.stage.getBatch().begin();
            //pass the delta time over, so that the bridge can be move over time
            float deltaTime = delta / 3;
            this.bridge.updateAndRender(deltaTime);
            this.car.updateAndRender(deltaTime);
            this.stage.getBatch().end();

            //check collision, more like checking if the car is in range of the bridge length
            if (!collision())
                this.gameState = false;

            //stage has to be draw last, otherwise the car will be drawn under the bridge
            this.stage.draw();

        }
    }

    private boolean collision(){
        int bridgeLeft = this.bridge.getFarLeft();
        int bridgeRight = bridgeLeft + ((2 + this.bridgeWidth) * this.GRID_SIZE);

        Rectangle r =car.getPlayerDeltaRectangle();

        Gdx.app.log("COLLISION LEFT", String.valueOf(bridgeLeft) + " " + String.valueOf(r.x ));
        Gdx.app.log("COLLISION RIGHT", String.valueOf(bridgeRight) + " " + String.valueOf(r.x + (this.GRID_SIZE * 0.25)));

        return ((bridgeLeft < r.x ) &&
                (bridgeRight > r.x + (this.GRID_SIZE * 0.25)));
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height){
//		super.resize(width, height);
        this.imageSize = width / rowNum;
        this.colNum = (int)(height / imageSize) * 2;

        this.stage.getViewport().update(width, height, true);
        this.bridge.resize(width, height);
        this.car.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        stage.dispose();
    }
}
