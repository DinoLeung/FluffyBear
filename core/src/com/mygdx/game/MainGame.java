package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Object.Car;
import com.mygdx.game.Object.ScoreBoard;

/**
 * Created by Dino on 03/04/2017.
 * Water splash sound effect from https://opengameart.org/content/water-splash-yo-frankie
 */

public class MainGame implements Screen{
    public static final int GRID_SIZE = 32;

    final GameLauncher launcher;

    com.mygdx.game.Object.Bridge bridge;
    com.mygdx.game.Object.Car car;
    com.mygdx.game.Object.ScoreBoard scoreBoard;

    private Stage stage;
    OrthographicCamera camera;
    private int rowNum = 12;
    private int colNum;
    private int bridgeWidth = 2;
    private float imageSize;

    private boolean gameState = true;
    private float score = 0;

    private Sound sound;

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

        bridge = new com.mygdx.game.Object.Bridge(rowNum, colNum, bridgeWidth, stage);
        car = new Car(stage);
        scoreBoard = new ScoreBoard(stage, score, launcher.fontUltraSmall);

        sound = Gdx.audio.newSound(Gdx.files.internal("Sound/watersplash.mp3"));
    }

    @Override
    public void render (float delta) {
        if (this.gameState){
            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //Allows transparent sprites/tiles
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            this.stage.act(delta);

            //score counting
            score += delta;
            scoreBoard.setScore(score);

            //change the car direction when screen is touched
            if (Gdx.input.justTouched())
                this.car.changeDirection();

            this.stage.getBatch().begin();
            //increase delta time over time, moves objects faster to make the game harder
            float deltaTime = delta * (1 + (score / 100));
            this.bridge.updateAndRender(deltaTime);
            this.car.updateAndRender(deltaTime);
            this.scoreBoard.updateAndRender(deltaTime);
            this.stage.getBatch().end();

            //check collision, more like checking if the car is in range of the bridge length
            if (!collision())
                this.gameState = false;

            //stage has to be draw last, otherwise the car will be drawn under the bridge
            this.stage.draw();

        } else {
            //go to restart screen
            sound.play(1.0f);
            launcher.setScreen(new RestartScreen(launcher, (int)score));
            dispose();
        }
    }

    private boolean collision(){
        float bridgeLeft = this.bridge.getFarLeft();
        float bridgeRight = bridgeLeft + ((2 + this.bridgeWidth) * this.GRID_SIZE);

        Rectangle r =car.getPlayerDeltaRectangle();

//        Gdx.app.log("COLLISION LEFT", String.valueOf(bridgeLeft) + " " + String.valueOf(r.x ));
//        Gdx.app.log("COLLISION RIGHT", String.valueOf(bridgeRight) + " " + String.valueOf(r.x + (this.GRID_SIZE * 0.25)));

        return ((bridgeLeft < r.x) &&
                (bridgeRight > r.x+(this.GRID_SIZE)));
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
        sound.dispose();
    }
}
