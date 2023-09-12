package com.mygdx.game.ojbect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.util.AsteroidUtil;

public class Asteroid implements Pool.Poolable {
    private static final float MIN_ASTEROID_POWER = 0.5f;
    private static final float MAX_ASTEROID_POWER = 1f;
    private static final float MIN_ROTATION = 0.5f;
    private static final float MAX_ROTATION = 3f;
    private Body body;
    private Fixture fixture;
    private Sprite sprite;
    private boolean alive;

    private float rotationAngle;
    public Asteroid() {
        alive = false;
    }

    public void update(SpriteBatch batch, float viewportWidthMeters, float viewportHeightMeters) {
        AsteroidUtil.containBodyOnScreen(body, viewportWidthMeters, viewportHeightMeters, sprite.getWidth(), sprite.getHeight());
        sprite.setPosition(AsteroidUtil.meterToPixel(body.getPosition().x) - sprite.getWidth() / 2, AsteroidUtil.meterToPixel(body.getPosition().y) - sprite.getHeight() / 2);
        sprite.rotate(rotationAngle);
        sprite.draw(batch);
    }

    public void init(float screenWidthMeters, float screenHeightMeters, Texture texture, World world, BodyDef def, FixtureDef fixtureDef) {
        Vector2 powerVector = getRandomPowerVector();
        Vector2 startingPosition = AsteroidUtil.getRandomPositionOnScreen(screenWidthMeters, screenHeightMeters);

        sprite = new Sprite(texture);
        sprite.setOrigin(texture.getWidth() / 2f, texture.getHeight() / 2f);

        CircleShape circleShape = new CircleShape();
        float imageWidthPixel = texture.getWidth();
        float imageHeightPixel = texture.getHeight();
        float imageWidthMeter = AsteroidUtil.pixelToMeter(imageWidthPixel);
        float imageHeightMeter = AsteroidUtil.pixelToMeter(imageHeightPixel);
        circleShape.setRadius(imageWidthMeter > imageHeightMeter ? imageHeightMeter / 2 : imageWidthMeter / 2);

        body = world.createBody(def);
        fixtureDef.shape = circleShape;
        fixture = body.createFixture(fixtureDef);
        body.setFixedRotation(true);

        circleShape.dispose();
        alive = false;
        body.setUserData(this);

        body.setActive(true);
        body.setTransform(startingPosition, 0f);
        body.applyLinearImpulse(powerVector.x * body.getMass(), powerVector.y * body.getMass(), body.getPosition().x, body.getPosition().y, true);
        alive = true;

        rotationAngle = MathUtils.random.nextFloat(MIN_ROTATION, MAX_ROTATION);
    }

    @Override
    public void reset() {
        alive = false;
        body.setLinearVelocity(Vector2.Zero);
        body.setActive(false);
        body.destroyFixture(fixture);
    }

    public void destroy() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    private static Vector2 getRandomPowerVector() {
        return new Vector2(generateRandomPower(), generateRandomPower());
    }

    private static float generateRandomPower() {
        return MathUtils.random.nextFloat(-1f, 1f) * MathUtils.random.nextFloat(MIN_ASTEROID_POWER, MAX_ASTEROID_POWER);
    }
}
