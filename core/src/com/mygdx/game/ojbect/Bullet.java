package com.mygdx.game.ojbect;

import static com.mygdx.game.util.Constants.PIXELS_TO_METERS;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.game.util.AsteroidUtil;

public class Bullet implements Pool.Poolable {

    private static final float BULLET_POWER = 2;
    public static final float SHOT_DISTANCE_FROM_PLAYER = 80f / PIXELS_TO_METERS;
    private final Body body;
    private final Fixture fixture;
    private final Sprite sprite;
    private boolean alive;

    public Bullet(World world, Texture bulletTexture, BodyDef bodyDef) {
        float imageWidth = bulletTexture.getWidth();
        float imageHeight = bulletTexture.getHeight();
        float imageWidthMeters = AsteroidUtil.pixelToMeter(imageWidth);
        float imageHeightMeters = AsteroidUtil.pixelToMeter(imageHeight);

        sprite = new Sprite(bulletTexture);
        sprite.setOrigin(bulletTexture.getWidth() / 2f , bulletTexture.getHeight() / 2f);

        body = world.createBody(bodyDef);
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(imageWidthMeters/2, imageHeightMeters/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.isSensor = true;

        fixture = body.createFixture(fixtureDef);
        alive = false;
        body.setUserData(this);
        body.setFixedRotation(true);
    }

    public void init(Vector2 startingPosition, float angleDegrees, Vector3 direction) {
        Vector2 shootingPosition = new Vector2(startingPosition.x + direction.x * SHOT_DISTANCE_FROM_PLAYER,
                startingPosition.y + direction.y * SHOT_DISTANCE_FROM_PLAYER);

        body.setActive(true);
        body.setTransform(shootingPosition, (float) Math.toRadians(angleDegrees));
        body.applyLinearImpulse(direction.x * body.getMass() * BULLET_POWER,
                direction.y * body.getMass() * BULLET_POWER, body.getPosition().x, body.getPosition().y, true);
        sprite.setRotation(angleDegrees);
        sprite.setPosition(AsteroidUtil.meterToPixel(body.getPosition().x) - sprite.getWidth() /2, AsteroidUtil.meterToPixel(body.getPosition().y) - sprite.getHeight() /2);
        alive = true;
    }

    public void update(SpriteBatch batch, float viewportWidth, float viewportHeight) {
        AsteroidUtil.containBodyOnScreen(body, viewportWidth, viewportHeight, sprite.getWidth(), sprite.getHeight());
        sprite.setPosition(AsteroidUtil.meterToPixel(body.getPosition().x) - sprite.getWidth() /2, AsteroidUtil.meterToPixel(body.getPosition().y) - sprite.getHeight() /2);
        sprite.draw(batch);
    }

    @Override
    public void reset() {
        alive = false;
        body.setLinearVelocity(Vector2.Zero);
        body.setActive(false);
    }

    public void destroy() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }
}
