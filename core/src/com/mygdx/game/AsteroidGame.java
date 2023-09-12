package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.screen.MenuScreen;

import java.util.ArrayList;
import java.util.List;

import sun.font.TrueTypeFont;

public class AsteroidGame extends Game {
	private SpriteBatch batch;
	private BitmapFont font;

	private List<Screen> disposableScreens;

	public SpriteBatch getBatch() {
		return batch;
	}

	public BitmapFont getFont() {
		return font;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		disposableScreens = new ArrayList<>();

		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
		for (Screen screen : disposableScreens) {
			screen.dispose();
		}
	}

	public void addScreen(Screen screen) {
		disposableScreens.add(screen);
	}
}
