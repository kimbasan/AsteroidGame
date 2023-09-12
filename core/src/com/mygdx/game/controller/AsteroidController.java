package com.mygdx.game.controller;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.ojbect.Asteroid;
import com.mygdx.game.ojbect.factory.AsteroidFactory;

public class AsteroidController {
    private static final int STARTING_ASTEROIDS_SIZE = 5;
    private static final float ASTEROID_SPAWN_TIME = 5f;
    private final Array<Asteroid> activeAsteroids;
    private final Pool<Asteroid> asteroidPool;
    private final AsteroidFactory factory;
    private float asteroidSpawnTimer = 0f;
    private boolean asteroidDestroyedByBullet = false;

    public AsteroidController(World world) {
        activeAsteroids = new Array<>();
        factory = new AsteroidFactory(world);
        asteroidPool = new Pool<Asteroid>() {
            @Override
            protected Asteroid newObject() {
                return factory.createAsteroid();
            }
        };
        asteroidPool.fill(STARTING_ASTEROIDS_SIZE);
    }

    public void render(SpriteBatch batch, float screenWidth, float screenHeight) {

        for (int i = activeAsteroids.size; --i >= 0; ) {
            Asteroid asteroid = activeAsteroids.get(i);
            if (asteroid.isAlive()) {
                asteroid.update(batch, screenWidth, screenHeight);
            } else {
                asteroidPool.free(asteroid);
                activeAsteroids.removeIndex(i);
            }
        }

        asteroidSpawnTimer += Gdx.graphics.getDeltaTime();
        if (asteroidSpawnTimer >= ASTEROID_SPAWN_TIME) {
            asteroidSpawnTimer = 0f;
            addAsteroid(screenWidth, screenHeight);
        }
        if (asteroidDestroyedByBullet) {
            asteroidDestroyedByBullet = false;
            addAsteroid(screenWidth, screenHeight);
        }
    }

    public void createStartingAsteroids(float screenWidth, float screenHeight) {
        for (int i = 0; i < STARTING_ASTEROIDS_SIZE; i++) {
            addAsteroid(screenWidth, screenHeight);
        }
    }

    public void addAsteroid(float screenWidth, float screenHeight) {
        Asteroid instance = asteroidPool.obtain();
        factory.init(instance, screenWidth, screenHeight);
        activeAsteroids.add(instance);
    }


    public void reset(float screenWidth, float screenHeight) {
        asteroidSpawnTimer = 0;
        for (int i = activeAsteroids.size; --i >= 0; ) {
            Asteroid asteroid = activeAsteroids.get(i);
            asteroid.destroy();
            asteroidPool.free(asteroid);
            activeAsteroids.removeIndex(i);
        }
        createStartingAsteroids(screenWidth, screenHeight);
    }

    public void setAsteroidShot() {
        asteroidDestroyedByBullet = true;
    }
}
