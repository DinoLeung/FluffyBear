package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by dino on 10/6/17.
 * Font file from http://www.dafont.com/3dventure.font
 */

public class GameLauncher extends Game {

    public SpriteBatch batch;
    public BitmapFont fontBig;
    public BitmapFont fontMid;
    public BitmapFont fontSmall;
    public BitmapFont fontUltraSmall;
    public BitmapFont fontUltraBig;

    @Override
    public void create() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Font/3Dventure.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.incremental = true;
        parameter.size = (int)(Gdx.app.getGraphics().getWidth() / 3);
        fontUltraBig = generator.generateFont(parameter);
        parameter.size = (int)(Gdx.app.getGraphics().getWidth() / 6);
        fontBig = generator.generateFont(parameter);
        parameter.size = (int)(Gdx.app.getGraphics().getWidth() / 8);
        fontMid = generator.generateFont(parameter);
        parameter.size = (int)(Gdx.app.getGraphics().getWidth() / 10);
        fontSmall = generator.generateFont(parameter);
        parameter.size = (int)(Gdx.app.getGraphics().getWidth() / 20);
        fontUltraSmall = generator.generateFont(parameter);

        generator.dispose();

        batch = new SpriteBatch();
        this.setScreen(new StartScreen(this));
    }

    @Override
    public void render(){
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
