package com.mygdx.game.ojbect.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.ojbect.Asteroid;

public class AsteroidFactory implements Disposable {

    private final FixtureDef fixtureDef;
    private final BodyDef bodyDef;

    private final Texture[] asteroidTextures;
    private final World world;
    public AsteroidFactory(World world) {
        this.world = world;
        fixtureDef = new FixtureDef();
        fixtureDef.density = 5f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.2f;
        bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        asteroidTextures = new Texture[] {
                new Texture(Gdx.files.internal("Sprites/Meteors/meteorBrown_big1.png")),
                new Texture(Gdx.files.internal("Sprites/Meteors/meteorBrown_big4.png")),
                new Texture(Gdx.files.internal("Sprites/Meteors/meteorBrown_med1.png")),
                new Texture(Gdx.files.internal("Sprites/Meteors/meteorBrown_med3.png")),
                new Texture(Gdx.files.internal("Sprites/Meteors/meteorGrey_big2.png")),
                new Texture(Gdx.files.internal("Sprites/Meteors/meteorGrey_big3.png")),
                new Texture(Gdx.files.internal("Sprites/Meteors/meteorGrey_med1.png")),
                new Texture(Gdx.files.internal("Sprites/Meteors/meteorGrey_med2.png"))
        };
    }

    public Asteroid createAsteroid() {
        return new Asteroid();
    }

    private Texture getRandomTexture() {
        return asteroidTextures[MathUtils.random.nextInt(0, asteroidTextures.length)];
    }

    @Override
    public void dispose() {
        for (Texture texture : asteroidTextures) {
            texture.dispose();
        }
    }

    public void init(Asteroid instance, float screenWidth, float screenHeight) {
        instance.init(screenWidth, screenHeight, getRandomTexture(), world, bodyDef, fixtureDef);
    }
}
