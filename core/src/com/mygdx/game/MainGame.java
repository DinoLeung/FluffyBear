package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends ApplicationAdapter {
    public static final int DEFAULT_SPEED = 80;
    public static final int ACCELERATION = 50;
    public static final int GOAL_REACH_ACCELERATION = 200;
    public static final int GRID_SIZE = 32;

    private int speed;
    private int goalSpeed;
    private boolean speedFixed;

//	SpriteBatch batch;
//	ScrollingBackground scrollingBackground;
    Bridge bridge;
    Car car;

	Stage stage;
    Viewport viewport;
    OrthographicCamera camera;
    private int rowNum = 12;
    private int colNum;
    private int bridgeWidth = 2;
    private float imageSize;
	
	@Override
	public void create () {

        imageSize = Gdx.app.getGraphics().getWidth() / rowNum;
        colNum = (int)(Gdx.app.getGraphics().getHeight() / imageSize) * 2;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, this.rowNum * GRID_SIZE, (this.colNum / 2) * GRID_SIZE);
		stage = new Stage();

        stage.getViewport().setScreenSize(Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
        stage.getViewport().setWorldSize(this.rowNum * GRID_SIZE, (this.colNum / 2) * GRID_SIZE);
        stage.getViewport().setCamera(camera);


//		scrollingBackground = new ScrollingBackground();
        bridge = new Bridge(rowNum, colNum, bridgeWidth, stage);
        car = new Car(stage);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //Allows transparent sprites/tiles
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.act(Gdx.graphics.getDeltaTime());
//		this.scrollingBackground.updateAndRender(Gdx.graphics.getDeltaTime(), batch);

        this.stage.getBatch().begin();
        //pass the delta time over, so that the bridge can be move over time
        float deltaTime = Gdx.graphics.getDeltaTime();
        this.bridge.updateAndRender(deltaTime);
        this.car.updateAndRender(deltaTime);
        this.stage.getBatch().end();

        //stage has to be draw last, otherwise the car will be drawn under the bridge
        this.stage.draw();
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
//		this.scrollingBackground.resize(width,height);
        this.bridge.resize(width, height);
        this.car.resize(width, height);
	}
}
