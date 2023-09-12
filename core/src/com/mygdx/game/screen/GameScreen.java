package com.mygdx.game.screen;

import static com.mygdx.game.util.Constants.PIXELS_TO_METERS;
import static com.mygdx.game.util.Constants.VIEWPORT_HEIGHT;
import static com.mygdx.game.util.Constants.VIEWPORT_HEIGHT_PIXEL;
import static com.mygdx.game.util.Constants.VIEWPORT_WIDTH;
import static com.mygdx.game.util.Constants.VIEWPORT_WIDTH_PIXEL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.AsteroidGame;
import com.mygdx.game.controller.AsteroidController;
import com.mygdx.game.controller.BulletController;
import com.mygdx.game.controller.InputController;
import com.mygdx.game.ojbect.Score;
import com.mygdx.game.contactListener.PlayerCollision;
import com.mygdx.game.ojbect.Player;
import com.mygdx.game.util.AsteroidUtil;

public class GameScreen implements Screen {

    private final AsteroidGame game;
    private final World world;
    private final Player player;
    private final Box2DDebugRenderer debugRenderer;
    private final ScreenViewport viewport;
    private final Texture backgoundTexture;
    private final AsteroidController asteroidController;
    private final BulletController bulletController;
    private final InputController inputController;
    private static final String BACKGROUND_IMG = "Sprites/backgroundTile.png";
    private float worldTimer = 0f;

    private final Score score;
    public GameScreen(AsteroidGame game) {
        this.game = game;
        game.addScreen(this);

        viewport = new ScreenViewport();
        viewport.setWorldSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        viewport.setScreenSize(VIEWPORT_WIDTH_PIXEL, VIEWPORT_HEIGHT_PIXEL);
        viewport.setUnitsPerPixel(1f / PIXELS_TO_METERS);

        world = new World(new Vector2(0, 0), false);
        score = new Score();
        debugRenderer = new Box2DDebugRenderer();

        backgoundTexture = new Texture(Gdx.files.internal(BACKGROUND_IMG));
        backgoundTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        float screenWidthMeters = viewport.getWorldWidth();
        float screenHeightMeters = viewport.getWorldHeight();
        player = new Player(world, screenWidthMeters, screenHeightMeters);

        asteroidController = new AsteroidController(world);
        asteroidController.createStartingAsteroids(viewport.getWorldWidth(), viewport.getWorldHeight());

        bulletController = new BulletController(world);

        inputController = new InputController(player, bulletController, this);

        world.setContactListener(new PlayerCollision(score, asteroidController));
    }


    @Override
    public void show() {

    }

    private Vector3 getMousePosition() {
        Vector3 result = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.getCamera().unproject(result);
        return result;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply(true);

        float screenWidthMeters = viewport.getWorldWidth();
        float screenHeightMeters = viewport.getWorldHeight();
        float screenWidthPixel = viewport.getScreenWidth();
        float screenHeightPixel = viewport.getScreenHeight();

        if (player.isAlive()) {
            worldTimer += Gdx.graphics.getDeltaTime();
        }

        Vector3 mousePosition = getMousePosition();

        game.getBatch().setProjectionMatrix(new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        game.getBatch().begin();

        renderBackground();
        asteroidController.render(game.getBatch(), screenWidthMeters, screenHeightMeters);
        bulletController.render(game.getBatch(), screenWidthMeters, screenHeightMeters);

        if (player.isAlive()) {
            player.render(game.getBatch(),screenWidthMeters, screenHeightMeters);
        } else {
            game.getFont().draw(game.getBatch(), "GAME OVER", screenWidthPixel / 2, screenHeightPixel / 2);
            game.getFont().draw(game.getBatch(), "Final score: " + score.getScore(), screenWidthPixel / 2, screenHeightPixel / 2 - 100);
            game.getFont().draw(game.getBatch(), "Press R to restart", screenWidthPixel / 2, screenHeightPixel / 3);
        }

        game.getFont().draw(game.getBatch(), "Time survived: " + (int) worldTimer, screenWidthPixel / 2, screenHeightPixel - 100);
        game.getFont().draw(game.getBatch(), "Score: " + score.getScore(), screenWidthPixel / 2, screenHeightPixel - 150);
        game.getFont().draw(game.getBatch(), "PlayerLives X " + player.getHitPoints(), 100, screenHeightPixel - 100);

        game.getBatch().end();

        inputController.readInputs(mousePosition);

        // debug render
        //debugRenderer.render(world, viewport.getCamera().combined);

        world.step(1 / 60f, 6, 2);
    }

    public void resetGame() {
        worldTimer = 0;

        player.resetState(viewport.getWorldWidth(), viewport.getWorldHeight());

        asteroidController.reset(viewport.getWorldWidth(), viewport.getWorldHeight());
        bulletController.reset();

        score.reset();
    }

    private void renderBackground() {
        float screenWidthPixel = viewport.getScreenWidth();
        float screenHeightPixel = viewport.getScreenHeight();
        float uRight = screenWidthPixel / backgoundTexture.getWidth();
        float uTop = screenHeightPixel / backgoundTexture.getHeight();

        game.getBatch().draw(backgoundTexture,
                0f, 0f,
                screenWidthPixel, screenHeightPixel,
                0, 0, uRight, uTop);
    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height, true);
        viewport.setWorldSize(AsteroidUtil.pixelToMeter(width), AsteroidUtil.pixelToMeter(height));
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
        world.dispose();
        debugRenderer.dispose();

        backgoundTexture.dispose();

        player.dispose();
        bulletController.dispose();
    }
}
