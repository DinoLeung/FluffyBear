package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Created by dino on 10/6/17.
 */

public class StartScreen implements Screen{

    final GameLauncher launcher;

    OrthographicCamera camera;


    public StartScreen(final GameLauncher launcher){

        this.launcher = launcher;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(25/255f, 137/255f, 100/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        launcher.batch.setProjectionMatrix(camera.combined);

        launcher.batch.begin();
//        launcher.font.draw(launcher.batch, "Welcome!!!", 100, 150);

        String tap = "TAP";
        GlyphLayout Tap = new GlyphLayout(launcher.fontBig, tap);
        String anywhere = "ANYWHERE";
        GlyphLayout Anywhere = new GlyphLayout(launcher.fontBig, anywhere);
        String to = "TO";
        GlyphLayout To = new GlyphLayout(launcher.fontBig, to);
        String start = "START";
        GlyphLayout Start = new GlyphLayout(launcher.fontBig, start);

        float height = To.height;

        launcher.fontBig.draw(launcher.batch, Tap,
                (Gdx.app.getGraphics().getWidth()-Tap.width)/2,
                (Gdx.app.getGraphics().getHeight()-height)/2 + height*4);
        launcher.fontBig.draw(launcher.batch, Anywhere,
                (Gdx.app.getGraphics().getWidth()-Anywhere.width)/2,
                (Gdx.app.getGraphics().getHeight()-height)/2 + height*2);
        launcher.fontBig.draw(launcher.batch, To,
                (Gdx.app.getGraphics().getWidth()-To.width)/2,
                (Gdx.app.getGraphics().getHeight()-height)/2);
        launcher.fontBig.draw(launcher.batch, Start,
                (Gdx.app.getGraphics().getWidth()-Start.width)/2,
                (Gdx.app.getGraphics().getHeight()-height)/2 - height*2);

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
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
