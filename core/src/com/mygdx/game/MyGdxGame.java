package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
//	ScrollingBackground scrollingBackground;
    Bridge bridge;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
//		scrollingBackground = new ScrollingBackground();
        bridge = new Bridge(12, 2, this.batch);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
//		this.scrollingBackground.updateAndRender(Gdx.graphics.getDeltaTime(), batch);
        this.bridge.updateAndRender(Gdx.graphics.getDeltaTime(), batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	@Override
	public void resize(int width, int height){
		super.resize(width, height);
//		this.scrollingBackground.resize(width,height);
        this.bridge.resize(width,height);
	}
}
