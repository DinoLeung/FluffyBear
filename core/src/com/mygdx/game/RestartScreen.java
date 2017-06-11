package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by dino on 10/6/17.
 */

public class RestartScreen implements Screen {

    final GameLauncher launcher;

    private int score;

    private OrthographicCamera camera;

    public RestartScreen(final GameLauncher launcher, int score){

        this.launcher = launcher;

        this.score = score;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //#1988964
        Gdx.gl.glClearColor(25/255f, 137/255f, 100/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        launcher.batch.setProjectionMatrix(camera.combined);

        launcher.batch.begin();

        String you = "YOU SCORED";
        GlyphLayout You = new GlyphLayout(launcher.fontMid, you);
        String score = String.valueOf(this.score);
        GlyphLayout Score = new GlyphLayout(launcher.fontUltraBig, score);

        String tap = "TAP ANYWHERE";
        GlyphLayout Tap = new GlyphLayout(launcher.fontSmall, tap);
        String to = "TO RESTART";
        GlyphLayout To = new GlyphLayout(launcher.fontSmall, to);

        float height = Score.height;

        launcher.fontMid.draw(launcher.batch, You,
                (Gdx.app.getGraphics().getWidth()-You.width)/2,
                (Gdx.app.getGraphics().getHeight()-height)/2 + height*2);
        launcher.fontUltraBig.draw(launcher.batch, Score,
                (Gdx.app.getGraphics().getWidth()-Score.width)/2,
                (Gdx.app.getGraphics().getHeight()-height)/2 + height);

        launcher.fontSmall.draw(launcher.batch, Tap,
                (Gdx.app.getGraphics().getWidth()-Tap.width)/2,
                (Gdx.app.getGraphics().getHeight()-height)/2 - height);
        launcher.fontSmall.draw(launcher.batch, To,
                (Gdx.app.getGraphics().getWidth()-To.width)/2,
                (Gdx.app.getGraphics().getHeight()-height)/2 - height - To.height*2);

        launcher.batch.end();

        if (Gdx.input.justTouched()) {
            launcher.setScreen(new MainGame(launcher));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

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
    public void dispose() {

    }
}
