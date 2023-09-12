package com.mygdx.game.screen;

import static com.mygdx.game.util.Constants.PIXELS_TO_METERS;
import static com.mygdx.game.util.Constants.VIEWPORT_HEIGHT;
import static com.mygdx.game.util.Constants.VIEWPORT_HEIGHT_PIXEL;
import static com.mygdx.game.util.Constants.VIEWPORT_WIDTH;
import static com.mygdx.game.util.Constants.VIEWPORT_WIDTH_PIXEL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AsteroidGame;
import com.mygdx.game.util.AsteroidUtil;

public class MenuScreen implements Screen {

    private final AsteroidGame game;
    private final ScreenViewport screenViewport;
    public MenuScreen(AsteroidGame game) {
        this.game = game;

        screenViewport = new ScreenViewport();
        screenViewport.setWorldSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        screenViewport.setScreenSize(VIEWPORT_WIDTH_PIXEL, VIEWPORT_HEIGHT_PIXEL);
        screenViewport.setUnitsPerPixel(1f / PIXELS_TO_METERS);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0, 0.2f, 1);

        screenViewport.apply(true);

        float screenWidthPixel = screenViewport.getScreenWidth();
        float screenHeightPixel = screenViewport.getScreenHeight();

        game.getBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        game.getBatch().begin();

        game.getFont().draw(game.getBatch(), "ASTEROID", screenWidthPixel / 2, (screenHeightPixel* 3) / 4);

        game.getFont().draw(game.getBatch(), "Controls:", screenWidthPixel / 2, screenHeightPixel / 2);
        game.getFont().draw(game.getBatch(), "W to thrust forward, A/D to thrust sideways", screenWidthPixel / 2, screenHeightPixel / 2 - 50);
        game.getFont().draw(game.getBatch(), "Left mouse button to shoot", screenWidthPixel / 2, screenHeightPixel / 2 - 100);

        game.getFont().draw(game.getBatch(), "Tap anywhere to start", screenWidthPixel / 2, screenHeightPixel / 4);

        game.getBatch().end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

        screenViewport.update(width, height, true);
        screenViewport.setWorldSize(AsteroidUtil.pixelToMeter(width), AsteroidUtil.pixelToMeter(height));
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
