package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by dino on 10/6/17.
 */

public class GameLauncher extends Game {

    public SpriteBatch batch;
    public BitmapFont fontSmall;
    public BitmapFont fontBig;
    public BitmapFont font;

    @Override
    public void create() {

//        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("3Dventure.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 12;
//        fontSmall = generator.generateFont(parameter);
//        parameter.size = 20;
//        fontBig = generator.generateFont(parameter);
//        generator.dispose();
        font = new BitmapFont();
        batch = new SpriteBatch();
        this.setScreen(new StartScreen(this));
//        this.setScreen(new MainGame(this));
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
