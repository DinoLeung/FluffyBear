package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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

public class MainGame extends ApplicationAdapter implements InputProcessor {
    public static final int DEFAULT_SPEED = 80;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 200;
    public static final int GRID_SIZE = 32;

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
	
	@Override
	public void create () {
//    batch = new SpriteBatch();
        // for logcat output
//        Gdx.app.setLogLevel(Application.LOG_DEBUG);

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

        Gdx.input.setInputProcessor(this);

	}

//    SpriteBatch batch;
	@Override
	public void render () {
        if (this.gameState){
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //Allows transparent sprites/tiles
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            this.stage.act(Gdx.graphics.getDeltaTime());

            this.stage.getBatch().begin();
            //pass the delta time over, so that the bridge can be move over time
            float deltaTime = Gdx.graphics.getDeltaTime() / 10;
            this.bridge.updateAndRender(deltaTime);
            this.car.updateAndRender(deltaTime);
            this.stage.getBatch().end();

            //check collision
            if (!collision())
                this.gameState = false;

            //stage has to be draw last, otherwise the car will be drawn under the bridge
            this.stage.draw();

        }
	}

    private int[][] getPositions(){

        final Rectangle r =car.getPlayerDeltaRectangle();
        return new int[][]{
                new int[]{(int)r.x, (int)(r.y+r.height)},
//                    new int[]{(int)(r.x+r.width), (int)r.y},
//                    new int[]{(int)r.x, (int)(r.y)},
                new int[]{(int)(r.x+r.width), (int)(r.y+r.height)}
        };

    }

	private boolean collision(){
        int bridgeLeft = this.bridge.getFarLeft();
        int bridgeRight = bridgeLeft + ((2 + this.bridgeWidth) * this.GRID_SIZE);

        Rectangle r =car.getPlayerDeltaRectangle();

        return ((bridgeLeft < r.x) && (bridgeRight > (r.x + r.width)));
    }
	
	@Override
	public void dispose () {
		stage.dispose();
	}

	@Override
	public void resize(int width, int height){
		super.resize(width, height);
        this.imageSize = width / rowNum;
        this.colNum = (int)(height / imageSize) * 2;

		this.stage.getViewport().update(width, height, true);
        this.bridge.resize(width, height);
        this.car.resize(width, height);
	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.car.changeDirection();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
